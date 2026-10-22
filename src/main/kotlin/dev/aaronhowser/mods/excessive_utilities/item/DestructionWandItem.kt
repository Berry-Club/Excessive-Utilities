package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.block_walker.BlockWalker
import dev.aaronhowser.mods.aaron.block_walker.WalkType
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.event.EventHooks
import net.neoforged.neoforge.event.level.block.BreakBlockEvent

class DestructionWandItem(properties: Properties) : Item(properties) {

	companion object {
		fun propertiesWithVolume(volume: Int): Properties {
			return Properties()
				.stacksTo(1)
				.component(ModDataComponents.AMOUNT_BLOCKS, volume)
		}

		private var isWandActive = false

		fun handleBreakBlockEvent(event: BreakBlockEvent) {
			if (isWandActive) return

			val player = event.player
			if (player.isClientSide) return

			val brokenState = event.state
			val usedStack = player.mainHandItem

			if (!player.hasInfiniteMaterials() &&
				brokenState.requiresCorrectToolForDrops()
				&& !usedStack.isCorrectToolForDrops(brokenState)
			) return

			var wandStack = ItemStack.EMPTY
			if (usedStack.item is DestructionWandItem) {
				wandStack = usedStack
			} else if (player.offhandItem.item is DestructionWandItem) {
				wandStack = player.offhandItem
			}

			if (wandStack.isEmpty) return

			val amount = wandStack.get(ModDataComponents.AMOUNT_BLOCKS) ?: return

			val hitResult = player.pick(
				player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE),
				1f,
				false
			)

			if (hitResult !is BlockHitResult) return

			val level = player.level()
			val blockPos = event.pos
			val face = if (player.isSecondaryUseActive) null else hitResult.direction

			val positions = getPositions(level, blockPos, brokenState.block, face, amount)
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

				val soundType = stateThere.getSoundType(level, pos, player)
				fun playSound() {
					level.playSound(
						null,
						pos,
						soundType.breakSound,
						SoundSource.BLOCKS,
						(soundType.volume + 1f) / 2f,
						soundType.pitch * 0.8f
					)
				}

				if (player.isCreative) {
					if (level.destroyBlock(pos, false, player, 512)) playSound()
					continue
				}

				usedStack.mineBlock(level, stateThere, pos, player)

				val canHarvest = stateThere.canHarvestBlock(level, pos, player)
				if (level.destroyBlock(pos, canHarvest, player, 512)) playSound()

				if (usedStack.isEmpty && !stackBeforeBreaking.isEmpty) {
					EventHooks.onPlayerDestroyItem(player, stackBeforeBreaking, InteractionHand.MAIN_HAND)
				}
			}

			isWandActive = false
		}

		fun getPositions(
			level: Level,
			startPos: BlockPos,
			originalBlock: Block,
			face: Direction?,
			maxCount: Int
		): List<BlockPos> {

			val searchDirections =
				if (face == null) {
					WalkType.ALL_SURROUNDING.neighborOffsets
				} else {
					when (face.axis) {
						Direction.Axis.Y -> listOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)
						Direction.Axis.X -> listOf(Direction.UP, Direction.DOWN, Direction.NORTH, Direction.SOUTH)
						else -> listOf(Direction.UP, Direction.DOWN, Direction.EAST, Direction.WEST)
					}.map { it.unitVec3i }
				}

			val walker = BlockWalker
				.Builder(level)
				.searchOffsets(searchDirections)
				.startPos(startPos)
				.filter { _, _, state -> state.isBlock(originalBlock) }
				.maxTotalBlocks(maxCount * 2)
				.build()

			val positions = walker
				.locateAllImmediately()
				.asSequence()
				.map { it.block.pos }
				.sortedBy { it.distSqr(startPos) }
				.take(maxCount)
				.toList()

			return positions
		}
	}

}




