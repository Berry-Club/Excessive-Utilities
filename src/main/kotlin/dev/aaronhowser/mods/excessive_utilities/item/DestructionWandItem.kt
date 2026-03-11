package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.block_walker.BlockWalker
import dev.aaronhowser.mods.aaron.block_walker.ConnectedBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.event.EventHooks
import net.neoforged.neoforge.event.level.BlockEvent

class DestructionWandItem(
	val maxBlocks: Int,
	properties: Properties
) : Item(properties) {

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)

		private var isWandActive = false

		fun handleBreakBlockEvent(event: BlockEvent.BreakEvent) {
			if (isWandActive) return

			val player = event.player
			if (player.isClientSide) return

			val brokenState = event.state
			val usedStack = player.mainHandItem

			if (!player.hasInfiniteMaterials() &&
				brokenState.requiresCorrectToolForDrops()
				&& !usedStack.isCorrectToolForDrops(brokenState)
			) return

			var wandItem: DestructionWandItem? = null
			if (usedStack.item is DestructionWandItem) {
				wandItem = usedStack.item as DestructionWandItem
			} else if (player.offhandItem.item is DestructionWandItem) {
				wandItem = player.offhandItem.item as DestructionWandItem
			}

			if (wandItem == null) return

			val hitResult = player.pick(
				player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE),
				1f,
				false
			)

			if (hitResult !is BlockHitResult) return

			val level = player.level()
			val blockPos = event.pos
			val face = hitResult.direction

			val positions = getPositions(level, blockPos, brokenState.block, face, wandItem.maxBlocks)
			if (positions.isEmpty()) return

			breakBlocks(usedStack, positions, level, player)
		}

		/**
		 * @see net.minecraft.server.level.ServerPlayerGameMode.destroyBlock
		 */
		private fun breakBlocks(usedStack: ItemStack, positions: List<BlockPos>, level: Level, player: Player) {
			isWandActive = true
			val stackBeforeBreaking = usedStack.copy()

			for (pos in positions) {
				if (!level.mayInteract(player, pos)) continue

				val blockEntityThere = level.getBlockEntity(pos)
				if (blockEntityThere != null) continue

				val stateThere = level.getBlockState(pos)
				usedStack.mineBlock(level, stateThere, pos, player)

				val canHarvest = stateThere.canHarvestBlock(level, pos, player)
				val shouldRemove = stateThere.onDestroyedByPlayer(level, pos, player, canHarvest, level.getFluidState(pos))
				if (shouldRemove) {
					stateThere.block.destroy(level, pos, stateThere)
				}

				if (canHarvest && shouldRemove) {
					stateThere.block.playerDestroy(level, player, pos, stateThere, null, usedStack)
				}

				if (usedStack.isEmpty && !stackBeforeBreaking.isEmpty) {
					EventHooks.onPlayerDestroyItem(player, stackBeforeBreaking, InteractionHand.MAIN_HAND)
				}
			}

			isWandActive = false
		}

		private fun getPositions(
			level: Level,
			startPos: BlockPos,
			originalBlock: Block,
			face: Direction,
			maxCount: Int
		): List<BlockPos> {
			val searchDirections = if (face.axis == Direction.Axis.Y) {
				listOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)
			} else {
				listOf(Direction.UP, Direction.DOWN)
			}

			val walker = BlockWalker
				.Builder(level)
				.searchOffsets(searchDirections.map(Direction::getNormal))
				.searchFromTail(true)
				.startPos(startPos)
				.filter { _, _, state -> state.isBlock(originalBlock) }
				.maxTotalBlocks(maxCount * 3)
				.build()

			val positions = walker
				.locateAllImmediately()
				.asSequence()
				.sortedBy(ConnectedBlock::distance)
				.map { it.block.pos }
				.take(maxCount)
				.toList()

			return positions
		}
	}

}