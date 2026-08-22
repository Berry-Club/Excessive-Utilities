package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import dev.aaronhowser.mods.excessive_utilities.packet.server_to_client.UpdateGridPowerPacket
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class GridPowerHandler : SavedData() {

	private val playerGridTracker = PlayerGridTracker()
	private val gridGroupManager = GridGroupManager(::setDirty)
	private val gridPowerByGridUuid: MutableMap<UUID, GridPowerSnapshot> = mutableMapOf()
	private val lastGridPowerByPlayer: MutableMap<UUID, GridPowerSnapshot> = mutableMapOf()

	fun getPlayerGrid(player: Player): PlayerGrid {
		return getPlayerGrid(player.uuid)
	}

	fun getPlayerGrid(playerUuid: UUID): PlayerGrid {
		return playerGridTracker.getOrCreate(playerUuid)
	}

	fun getProducers(playerUuid: UUID): Set<GridPowerContribution> {
		val producers = mutableSetOf<GridPowerContribution>()

		for (memberUuid in gridGroupManager.getMemberUuids(playerUuid)) {
			val grid = playerGridTracker.get(memberUuid) ?: continue
			producers.addAll(grid.getProducers())
		}

		return producers
	}

	fun getConsumers(playerUuid: UUID): Set<GridPowerContribution> {
		val consumers = mutableSetOf<GridPowerContribution>()

		for (memberUuid in gridGroupManager.getMemberUuids(playerUuid)) {
			val grid = playerGridTracker.get(memberUuid) ?: continue
			consumers.addAll(grid.getConsumers())
		}

		return consumers
	}

	fun getGridPower(playerUuid: UUID): GridPowerSnapshot {
		val gridUuid = gridGroupManager.getEffectiveGridUuid(playerUuid)
		val gridPower = gridPowerByGridUuid[gridUuid]
		if (gridPower != null) return gridPower

		val calculatedGridPower = calculateGridPower(playerUuid)
		gridPowerByGridUuid[gridUuid] = calculatedGridPower
		return calculatedGridPower
	}

	private fun calculateGridPower(playerUuid: UUID): GridPowerSnapshot {
		var capacity = 0.0
		var usage = 0.0

		for (memberUuid in gridGroupManager.getMemberUuids(playerUuid)) {
			val grid = playerGridTracker.get(memberUuid) ?: continue
			capacity += grid.getCapacity()
			usage += grid.getUsage()
		}

		return GridPowerSnapshot(capacity, usage)
	}

	fun isOverloaded(playerUuid: UUID): Boolean {
		return getGridPower(playerUuid).isOverloaded
	}

	fun getMemberUuids(playerUuid: UUID): Set<UUID> {
		return gridGroupManager.getMemberUuids(playerUuid)
	}

	fun canInvite(playerUuid: UUID): Boolean {
		return gridGroupManager.canInvite(playerUuid)
	}

	fun isGroupOwner(playerUuid: UUID): Boolean {
		return gridGroupManager.isGroupOwner(playerUuid)
	}

	fun invite(inviterUuid: UUID, invitedUuid: UUID): GridGroupResult {
		return gridGroupManager.invite(inviterUuid, invitedUuid)
	}

	fun acceptInvitation(invitedUuid: UUID): GridGroupResult {
		val result = gridGroupManager.acceptInvitation(invitedUuid)
		clearGridPowerCacheIfSuccessful(result)
		return result
	}

	fun declineInvitation(invitedUuid: UUID): GridGroupResult {
		return gridGroupManager.declineInvitation(invitedUuid)
	}

	fun leaveGroup(playerUuid: UUID): GridGroupResult {
		val result = gridGroupManager.leave(playerUuid)
		clearGridPowerCacheIfSuccessful(result)
		return result
	}

	fun removeMember(ownerUuid: UUID, memberUuid: UUID): GridGroupResult {
		val result = gridGroupManager.removeMember(ownerUuid, memberUuid)
		clearGridPowerCacheIfSuccessful(result)
		return result
	}

	fun tick(level: ServerLevel) {
		playerGridTracker.tick()

		val representativePlayerByGridUuid = mutableMapOf<UUID, UUID>()
		for (playerUuid in playerGridTracker.getOwnerUuids()) {
			val gridUuid = gridGroupManager.getEffectiveGridUuid(playerUuid)
			representativePlayerByGridUuid.putIfAbsent(gridUuid, playerUuid)
		}

		val onlineUuids = mutableSetOf<UUID>()
		for (player in level.server.playerList.players) {
			onlineUuids.add(player.uuid)

			val gridUuid = gridGroupManager.getEffectiveGridUuid(player.uuid)
			representativePlayerByGridUuid.putIfAbsent(gridUuid, player.uuid)
		}

		gridPowerByGridUuid.keys.retainAll(representativePlayerByGridUuid.keys)
		for ((gridUuid, playerUuid) in representativePlayerByGridUuid) {
			gridPowerByGridUuid[gridUuid] = calculateGridPower(playerUuid)
		}

		for (playerUuid in onlineUuids) {
			val gridUuid = gridGroupManager.getEffectiveGridUuid(playerUuid)
			val gridPower = gridPowerByGridUuid.getValue(gridUuid)
			updateClient(playerUuid, gridPower, level)
		}

		lastGridPowerByPlayer.keys.retainAll(onlineUuids)
	}

	private fun clearGridPowerCacheIfSuccessful(result: GridGroupResult) {
		if (result.isSuccess) {
			gridPowerByGridUuid.clear()
		}
	}

	private fun updateClient(
		playerUuid: UUID,
		power: GridPowerSnapshot,
		level: ServerLevel
	) {
		if (lastGridPowerByPlayer[playerUuid] == power) return

		val player = level.server.playerList.getPlayer(playerUuid) ?: return
		UpdateGridPowerPacket(power.capacity, power.usage).messagePlayer(player)
		lastGridPowerByPlayer[playerUuid] = power
	}

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		gridGroupManager.save(tag)
		return tag
	}

	companion object {
		const val SAVED_DATA_NAME = "eu_grid_power_handler"

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): GridPowerHandler {
			val handler = GridPowerHandler()
			handler.gridGroupManager.load(tag)
			return handler
		}

		fun get(level: ServerLevel): GridPowerHandler {
			if (level != level.server.overworld()) {
				return get(level.server.overworld())
			}

			val storage = level.dataStorage
			val factory = Factory(::GridPowerHandler, ::load)
			return storage.computeIfAbsent(factory, SAVED_DATA_NAME)
		}
	}
}