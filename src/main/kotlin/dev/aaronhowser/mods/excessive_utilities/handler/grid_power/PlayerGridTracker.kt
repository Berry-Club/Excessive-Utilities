package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import java.util.*

class PlayerGridTracker {

	private val playerGrids: MutableMap<UUID, PlayerGrid> = mutableMapOf()

	fun getOrCreate(playerUuid: UUID): PlayerGrid {
		return playerGrids.getOrPut(playerUuid) { PlayerGrid(playerUuid) }
	}

	fun get(playerUuid: UUID): PlayerGrid? {
		return playerGrids[playerUuid]
	}

	fun getOwnerUuids(): Set<UUID> {
		return playerGrids.keys
	}

	fun tick() {
		for (grid in playerGrids.values) {
			grid.tick()
		}

		playerGrids.entries.removeIf { (_, grid) -> grid.isEmpty() }
	}

}