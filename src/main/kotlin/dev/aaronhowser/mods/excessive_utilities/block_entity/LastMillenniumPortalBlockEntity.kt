package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getUuidOrNull
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toChunkPos
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.util.*

class LastMillenniumPortalBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.LAST_MILLENNIUM_PORTAL.get(), pos, blockState) {

	var owner: UUID = UUID.randomUUID()
		private set

	var destinationChunk: ChunkPos? = null
		private set

	fun setOwner(owner: UUID) {
		this.owner = owner
		destinationChunk = null
		setChanged()
	}

	fun setDestinationChunk(destinationChunk: ChunkPos) {
		if (this.destinationChunk == destinationChunk) return

		this.destinationChunk = destinationChunk
		setChanged()
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		tag.putUUID(OWNER_NBT, owner)

		val destinationChunk = destinationChunk
		if (destinationChunk != null) {
			tag.putLong(DESTINATION_CHUNK_NBT, destinationChunk.toLong())
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)
		owner = tag.getUuidOrNull(OWNER_NBT) ?: UUID.randomUUID()
		destinationChunk = if (tag.contains(DESTINATION_CHUNK_NBT, Tag.TAG_LONG.toInt())) {
			tag.getLong(DESTINATION_CHUNK_NBT).toChunkPos()
		} else {
			null
		}
	}

	companion object {
		const val OWNER_NBT = "Owner"
		const val DESTINATION_CHUNK_NBT = "DestinationChunk"
	}

}