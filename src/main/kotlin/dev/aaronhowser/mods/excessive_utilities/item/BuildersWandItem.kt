package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.block_walker.BlockWalker
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class BuildersWandItem(properties: Properties) : Item(properties) {

	companion object {
		fun propertiesWithVolume(volume: Int): Properties {
			return Properties()
				.stacksTo(1)
				.component(ModDataComponents.AMOUNT_BLOCKS, volume)
		}

		fun getPositions(
			level: Level,
			clickedBlockPos: BlockPos,
			clickedBlockState: BlockState,
			clickedFace: Direction,
			maxCount: Int
		): List<BlockPos> {
			val searchDirections =
				when (clickedFace.axis) {
					Direction.Axis.Y -> listOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)
					Direction.Axis.X -> listOf(Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH)
					else -> listOf(Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST)
				}

			val walker = BlockWalker
				.Builder(level)
				.searchOffsets(searchDirections.map(Direction::getNormal))
				.startPos(clickedBlockPos)
				.filter { level, pos, state ->
					val posAtFce = pos.relative(clickedFace)
					state.isBlock(clickedBlockState.block) && level.getBlockState(posAtFce).canBeReplaced()
				}
				.maxTotalBlocks(maxCount * 2)
				.build()

			val positions = walker
				.locateAllImmediately()
				.asSequence()
				.map { it.block.pos }
				.sortedBy { it.distSqr(clickedBlockPos) }
				.take(maxCount)
				.toList()

			return positions
		}
	}

}