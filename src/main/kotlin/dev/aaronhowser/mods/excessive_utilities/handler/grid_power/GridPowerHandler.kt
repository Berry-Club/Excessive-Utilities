package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import net.minecraft.core.HolderLookup
import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class GridPowerHandler : SavedData() {

	private val grids: MutableMap<UUID, GPGrid> = mutableMapOf()

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		val listTag = tag.getList(GRID_IDS_NBT, Tag.TAG_INT_ARRAY.toInt())

		for (gridId in grids.keys) {
			val uuidTag = NbtUtils.createUUID(gridId)
			listTag.add(uuidTag)
		}

		tag.put(GRID_IDS_NBT, listTag)
		return tag
	}

	fun tick() {
		grids.values.forEach(GPGrid::tick)
		grids.entries.removeIf { (_, grid) -> grid.isEmpty() }
	}

	companion object {
		const val GRID_IDS_NBT = "GridIDs"
		const val SAVED_DATA_NAME = "eu_grid_power_handler"

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): GridPowerHandler {
			val handler = GridPowerHandler()

			val listTag = tag.getList(GRID_IDS_NBT, Tag.TAG_INT_ARRAY.toInt())
			for (i in listTag.indices) {
				val intArray = listTag.getIntArray(i)
				val uuid = UUIDUtil.uuidFromIntArray(intArray)
				handler.grids[uuid] = GPGrid(uuid)
			}

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