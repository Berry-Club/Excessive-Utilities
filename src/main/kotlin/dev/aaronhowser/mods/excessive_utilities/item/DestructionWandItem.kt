package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
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

			if (!usedStack.isCorrectToolForDrops(brokenState)) return

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

			val positions = getPositions(level, blockPos, face, wandItem.maxBlocks)
			if (positions.isEmpty()) return

			breakBlocks(usedStack, positions, level, player)
		}

		// Copied from ServerPlayerGameMode#destroyBlock
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

		private fun getPositions(level: Level, origin: BlockPos, face: Direction, maxCount: Int): List<BlockPos> {
			return emptyList()
		}
	}

}