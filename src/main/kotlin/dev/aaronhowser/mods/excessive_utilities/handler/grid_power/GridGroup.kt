package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import java.util.*

class GridGroup(
	val groupUuid: UUID,
	ownerUuid: UUID,
	memberUuids: Collection<UUID>
) {

	var ownerUuid: UUID = ownerUuid
		private set

	private val mutableMemberUuids: MutableSet<UUID> = memberUuids.toMutableSet()

	val memberUuids: Set<UUID>
		get() = mutableMemberUuids

	fun addMember(playerUuid: UUID): Boolean {
		return mutableMemberUuids.add(playerUuid)
	}

	fun removeMember(playerUuid: UUID): Boolean {
		return mutableMemberUuids.remove(playerUuid)
	}

	fun transferOwnership(playerUuid: UUID) {
		require(mutableMemberUuids.contains(playerUuid))
		ownerUuid = playerUuid
	}
}