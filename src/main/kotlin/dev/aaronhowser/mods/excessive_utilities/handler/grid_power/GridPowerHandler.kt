package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class GridPowerHandler : SavedData() {

	private val grids: MutableMap<UUID, GPGrid> = mutableMapOf()

	fun getGrid(player: Player): GPGrid {
		return getGrid(player.uuid)
	}

	fun getGrid(uuid: UUID): GPGrid {
		return grids.getOrPut(uuid) { GPGrid(uuid) }
	}

	fun tick(level: ServerLevel) {
		grids.values.forEach { grid -> grid.tick(level) }
		grids.entries.removeIf { (_, grid) -> grid.isEmpty() }
	}

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		return tag
	}

	companion object {
		const val SAVED_DATA_NAME = "eu_grid_power_handler"

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): GridPowerHandler {
			return GridPowerHandler()
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