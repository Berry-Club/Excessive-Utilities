package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.block_walker.BlockWalker
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult

class BuildersWandItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level
		if (level !is ServerLevel) return InteractionResult.SUCCESS

		val player = context.player ?: return InteractionResult.SUCCESS
		val clickedPos = context.clickedPos
		val clickedState = level.getBlockState(clickedPos)
		val clickedFace = context.clickedFace

		val wandStack = context.itemInHand
		val volume = wandStack.get(ModDataComponents.AMOUNT_BLOCKS) ?: return InteractionResult.FAIL

		val positions = getPositions(level, clickedPos, clickedState, clickedFace, volume)
		if (positions.isEmpty()) return InteractionResult.FAIL

		val block = clickedState.block
		var success = false

		for (pos in positions) {
			if (!level.mayInteract(player, pos)) continue

			var stack = ItemStack.EMPTY

			if (player.hasInfiniteMaterials()) {
				stack = block.getDefaultInstance()
			} else {
				for (slot in 0 until player.inventory.containerSize) {
					val stackThere = player.inventory.getItem(slot)
					val itemThere = stackThere.item

					if (itemThere is BlockItem && itemThere.block == block) {
						stack = stackThere
						break
					}
				}
			}

			if (stack.isEmpty) continue

			val placeContext = BlockPlaceContext(
				level,
				player,
				context.hand,
				stack,
				BlockHitResult(pos.center, clickedFace.opposite, pos, false)
			)

			val stateToPlace = block.getStateForPlacement(placeContext) ?: continue
			level.setBlockAndUpdate(pos, stateToPlace)
			stateToPlace.block.setPlacedBy(level, pos, stateToPlace, player, stack)

			stack.consume(1, player)
			success = true
		}

		return if (success) InteractionResult.SUCCESS else InteractionResult.FAIL
	}

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
				.map { it.relative(clickedFace) }
				.toList()

			return positions
		}
	}

}