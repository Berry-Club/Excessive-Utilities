package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.minecraft.world.phys.BlockHitResult
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

			isWandActive = true

		}

		private fun getPositions(level: Level, origin: BlockPos, face: Direction, maxCount: Int): List<BlockPos> {
			return emptyList()
		}
	}

}