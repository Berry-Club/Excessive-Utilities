package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.AaronExtensions.defaultBlockState
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

class AngelBlockItem(properties: Properties) : BlockItem(ModBlocks.ANGEL_BLOCK.get(), properties) {

	override fun onItemUseFirst(stack: ItemStack, context: UseOnContext): InteractionResult {
		val player = context.player ?: return InteractionResult.PASS

		if (placeFromPlayer(player, stack)) {
			return InteractionResult.SUCCESS
		}

		return InteractionResult.PASS
	}

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
		val stack = player.getItemInHand(usedHand)

		return if (placeFromPlayer(player, stack)) {
			InteractionResultHolder.success(stack)
		} else {
			InteractionResultHolder.pass(stack)
		}
	}

	companion object {
		private fun placeFromPlayer(player: Player, stack: ItemStack): Boolean {
			var pos = player.eyePosition
			val playerBox = player.boundingBox

			fun isInsideBlockCube(position: Vec3): Boolean {
				val blockPos = BlockPos.containing(position)
				val blockCube = AABB(blockPos)
				return playerBox.intersects(blockCube)
			}

			while (isInsideBlockCube(pos)) {
				pos = pos.add(player.lookAngle.scale(0.25))
			}

			val targetPos = BlockPos.containing(pos)
			val level = player.level()

			if (level.getBlockState(targetPos).canBeReplaced()) {
				level.setBlockAndUpdate(targetPos, ModBlocks.ANGEL_BLOCK.defaultBlockState())
				stack.consume(1, player)
				return true
			}

			return false
		}

	}

}