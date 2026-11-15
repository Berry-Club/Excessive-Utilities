package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toChunkPos
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.UUIDUtil
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
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

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)
		tag.putIntArray(OWNER_NBT, UUIDUtil.uuidToIntArray(owner))

		val destinationChunk = destinationChunk
		if (destinationChunk != null) {
			tag.putLong(DESTINATION_CHUNK_NBT, destinationChunk.pack())
		}
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)
		val ownerInts = tag.getIntArray(OWNER_NBT).orElse(IntArray(0))
		owner = if (ownerInts.size == 4) UUIDUtil.uuidFromIntArray(ownerInts) else UUID.randomUUID()
		destinationChunk = tag.getLong(DESTINATION_CHUNK_NBT).map { it.toChunkPos() }.orElse(null)
	}

	companion object {
		const val OWNER_NBT = "Owner"
		const val DESTINATION_CHUNK_NBT = "DestinationChunk"
	}

}