package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import java.util.*

class GridGroupManager(
	private val onChanged: () -> Unit
) {

	private val groups: MutableMap<UUID, GridGroup> = mutableMapOf()
	private val groupUuidByPlayer: MutableMap<UUID, UUID> = mutableMapOf()
	private val invitations: MutableMap<UUID, UUID> = mutableMapOf()

	fun getEffectiveGridUuid(playerUuid: UUID): UUID {
		return groupUuidByPlayer[playerUuid] ?: playerUuid
	}

	fun getMemberUuids(playerUuid: UUID): Set<UUID> {
		val groupUuid = groupUuidByPlayer[playerUuid] ?: return setOf(playerUuid)
		val group = groups[groupUuid] ?: return setOf(playerUuid)
		return group.memberUuids
	}

	fun canInvite(playerUuid: UUID): Boolean {
		val groupUuid = groupUuidByPlayer[playerUuid] ?: return true
		return groups[groupUuid]?.ownerUuid == playerUuid
	}

	fun isGroupOwner(playerUuid: UUID): Boolean {
		val groupUuid = groupUuidByPlayer[playerUuid] ?: return false
		return groups[groupUuid]?.ownerUuid == playerUuid
	}

	fun invite(inviterUuid: UUID, invitedUuid: UUID): GridGroupResult {
		if (inviterUuid == invitedUuid) {
			return result(GridGroupResult.Status.CANNOT_TARGET_SELF)
		}

		if (getMemberUuids(inviterUuid).contains(invitedUuid)) {
			return result(GridGroupResult.Status.ALREADY_IN_SAME_GROUP)
		}

		if (groupUuidByPlayer.containsKey(invitedUuid)) {
			return result(GridGroupResult.Status.TARGET_IN_ANOTHER_GROUP)
		}

		val inviterGroupUuid = groupUuidByPlayer[inviterUuid]
		if (inviterGroupUuid != null) {
			val inviterGroup = groups[inviterGroupUuid]
			if (inviterGroup == null || inviterGroup.ownerUuid != inviterUuid) {
				return result(GridGroupResult.Status.NOT_GROUP_OWNER)
			}
		}

		invitations[invitedUuid] = inviterUuid
		return result(GridGroupResult.Status.SUCCESS)
	}

	fun acceptInvitation(invitedUuid: UUID): GridGroupResult {
		val inviterUuid = invitations.remove(invitedUuid)
			?: return result(GridGroupResult.Status.NO_INVITATION)

		if (groupUuidByPlayer.containsKey(invitedUuid)) {
			return result(GridGroupResult.Status.INVALID_INVITATION)
		}

		if (getMemberUuids(inviterUuid).contains(invitedUuid)) {
			return result(GridGroupResult.Status.INVALID_INVITATION)
		}

		val inviterGroupUuid = groupUuidByPlayer[inviterUuid]
		if (inviterGroupUuid != null) {
			val inviterGroup = groups[inviterGroupUuid]
			if (inviterGroup == null || inviterGroup.ownerUuid != inviterUuid) {
				return result(GridGroupResult.Status.INVALID_INVITATION)
			}
		}

		val groupUuid = inviterGroupUuid ?: createGroup(inviterUuid)
		val group = groups.getValue(groupUuid)
		group.addMember(invitedUuid)
		groupUuidByPlayer[invitedUuid] = groupUuid
		onChanged()
		return result(GridGroupResult.Status.SUCCESS, inviterUuid)
	}

	fun declineInvitation(invitedUuid: UUID): GridGroupResult {
		val inviterUuid = invitations.remove(invitedUuid)
			?: return result(GridGroupResult.Status.NO_INVITATION)

		return result(GridGroupResult.Status.SUCCESS, inviterUuid)
	}

	fun leave(playerUuid: UUID): GridGroupResult {
		val groupUuid = groupUuidByPlayer[playerUuid]
			?: return result(GridGroupResult.Status.NOT_IN_SHARED_GROUP)
		val group = groups[groupUuid]
			?: return result(GridGroupResult.Status.NOT_IN_SHARED_GROUP)

		groupUuidByPlayer.remove(playerUuid)
		group.removeMember(playerUuid)
		invitations.entries.removeIf { it.value == playerUuid }

		if (group.memberUuids.size == 1) {
			dissolve(group)
		} else if (group.ownerUuid == playerUuid) {
			val newOwnerUuid = group.memberUuids.minBy { it.toString() }
			group.transferOwnership(newOwnerUuid)
		}

		onChanged()
		return result(GridGroupResult.Status.SUCCESS)
	}

	fun removeMember(ownerUuid: UUID, memberUuid: UUID): GridGroupResult {
		if (ownerUuid == memberUuid) {
			return result(GridGroupResult.Status.CANNOT_TARGET_SELF)
		}

		val groupUuid = groupUuidByPlayer[ownerUuid]
			?: return result(GridGroupResult.Status.NOT_IN_SHARED_GROUP)
		val group = groups[groupUuid]
			?: return result(GridGroupResult.Status.NOT_IN_SHARED_GROUP)

		if (group.ownerUuid != ownerUuid) {
			return result(GridGroupResult.Status.NOT_GROUP_OWNER)
		}

		if (!group.removeMember(memberUuid)) {
			return result(GridGroupResult.Status.TARGET_NOT_IN_GROUP)
		}

		groupUuidByPlayer.remove(memberUuid)
		if (group.memberUuids.size == 1) {
			dissolve(group)
		}

		onChanged()
		return result(GridGroupResult.Status.SUCCESS)
	}

	fun save(tag: CompoundTag) {
		val groupList = ListTag()

		for (group in groups.values) {
			val groupTag = CompoundTag()
			groupTag.putUUID(GROUP_UUID_NBT, group.groupUuid)
			groupTag.putUUID(OWNER_UUID_NBT, group.ownerUuid)

			val memberList = ListTag()
			for (memberUuid in group.memberUuids) {
				val memberTag = CompoundTag()
				memberTag.putUUID(MEMBER_UUID_NBT, memberUuid)
				memberList.add(memberTag)
			}

			groupTag.put(MEMBERS_NBT, memberList)
			groupList.add(groupTag)
		}

		tag.put(GROUPS_NBT, groupList)
	}

	fun load(tag: CompoundTag) {
		val groupList = tag.getList(GROUPS_NBT, Tag.TAG_COMPOUND.toInt())

		for (groupIndex in groupList.indices) {
			val groupTag = groupList.getCompound(groupIndex)
			if (!groupTag.hasUUID(GROUP_UUID_NBT)) continue

			val groupUuid = groupTag.getUUID(GROUP_UUID_NBT)
			if (groups.containsKey(groupUuid)) continue

			val memberUuids = loadMemberUuids(groupTag)
			if (memberUuids.size < 2) continue

			val ownerUuid = loadOwnerUuid(groupTag, memberUuids)
			val group = GridGroup(groupUuid, ownerUuid, memberUuids)
			groups[groupUuid] = group

			for (memberUuid in memberUuids) {
				groupUuidByPlayer[memberUuid] = groupUuid
			}
		}
	}

	private fun loadMemberUuids(groupTag: CompoundTag): Set<UUID> {
		val memberList = groupTag.getList(MEMBERS_NBT, Tag.TAG_COMPOUND.toInt())
		val memberUuids = mutableSetOf<UUID>()

		for (memberIndex in memberList.indices) {
			val memberTag = memberList.getCompound(memberIndex)
			if (!memberTag.hasUUID(MEMBER_UUID_NBT)) continue

			val memberUuid = memberTag.getUUID(MEMBER_UUID_NBT)
			if (groupUuidByPlayer.containsKey(memberUuid)) continue
			memberUuids.add(memberUuid)
		}

		return memberUuids
	}

	private fun loadOwnerUuid(groupTag: CompoundTag, memberUuids: Set<UUID>): UUID {
		if (groupTag.hasUUID(OWNER_UUID_NBT)) {
			val ownerUuid = groupTag.getUUID(OWNER_UUID_NBT)
			if (memberUuids.contains(ownerUuid)) return ownerUuid
		}

		return memberUuids.first()
	}

	private fun createGroup(ownerUuid: UUID): UUID {
		val groupUuid = UUID.randomUUID()
		groups[groupUuid] = GridGroup(groupUuid, ownerUuid, setOf(ownerUuid))
		groupUuidByPlayer[ownerUuid] = groupUuid
		return groupUuid
	}

	private fun dissolve(group: GridGroup) {
		for (memberUuid in group.memberUuids) {
			groupUuidByPlayer.remove(memberUuid)
		}

		groups.remove(group.groupUuid)
	}

	private fun result(
		status: GridGroupResult.Status,
		inviterUuid: UUID? = null
	): GridGroupResult {
		return GridGroupResult(status, inviterUuid)
	}

	companion object {
		const val GROUPS_NBT = "Groups"
		const val GROUP_UUID_NBT = "GroupUUID"
		const val OWNER_UUID_NBT = "OwnerUUID"
		const val MEMBERS_NBT = "Members"
		const val MEMBER_UUID_NBT = "MemberUUID"
	}
}