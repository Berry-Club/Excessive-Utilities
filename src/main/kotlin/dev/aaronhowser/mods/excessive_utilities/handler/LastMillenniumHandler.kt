package dev.aaronhowser.mods.excessive_utilities.handler

import dev.aaronhowser.mods.aaron.misc.AaronUtil
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.saveddata.SavedData
import java.util.UUID

class LastMillenniumHandler : SavedData() {

	private val playerChunks: MutableMap<UUID, ChunkPos> = mutableMapOf()

	fun getChunk(uuid: UUID): ChunkPos {
		val existing = playerChunks[uuid]
		if (existing != null) {
			return existing
		}

		val nextIndex = playerChunks.size
		val (x, y) = AaronUtil.getGridSpiralPos(nextIndex)
		val chunkPos = ChunkPos(x * 16, y * 16)
		playerChunks[uuid] = chunkPos
		return chunkPos
	}

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		TODO("Not yet implemented")
	}

	companion object {

	}
}