package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.MagnumTorchCarrier
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class MagnumTorchBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.ENDER_ANCHOR.get(), pos, blockState) {

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