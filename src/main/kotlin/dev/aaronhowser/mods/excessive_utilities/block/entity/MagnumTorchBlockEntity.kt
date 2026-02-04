package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.MagnumTorchCarrier
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class MagnumTorchBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.MAGNUM_TORCH.get(), pos, blockState) {

	override fun onLoad() {
		super.onLoad()
		val level = this.level
		if (level is MagnumTorchCarrier) {
			level.getMagnumTorchPositions().add(this.worldPosition.asLong())
		}
	}

	override fun setRemoved() {
		super.setRemoved()
		val level = this.level
		if (level is MagnumTorchCarrier) {
			level.getMagnumTorchPositions().remove(this.worldPosition.asLong())
		}
	}

	override fun clearRemoved() {
		super.clearRemoved()
		val level = this.level
		if (level is MagnumTorchCarrier) {
			level.getMagnumTorchPositions().add(this.worldPosition.asLong())
		}
	}


	companion object {
		fun MagnumTorchCarrier.getMagnumTorchPositions(): LongOpenHashSet = this.`eu$getMagnumTorchBlockPositions`()

		fun preventMonsterSpawn(level: ServerLevel, pos: BlockPos): Boolean {
			if (level !is MagnumTorchCarrier) return false

			val radius = 16
			val positions = level.getMagnumTorchPositions()

			return positions
				.asSequence()
				.map(BlockPos::of)
				.any { it.closerThan(pos, radius.toDouble()) }
		}
	}

}