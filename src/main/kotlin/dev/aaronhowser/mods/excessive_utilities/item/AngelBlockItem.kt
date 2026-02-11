package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3

class AngelBlockItem(properties: Properties) : BlockItem(ModBlocks.ANGEL_BLOCK.get(), properties) {

	override fun onItemUseFirst(stack: ItemStack, context: UseOnContext): InteractionResult {
		val player = context.player ?: return InteractionResult.PASS

		val posToPlace = getPositionToPlace(player)
		val placeContext = BlockPlaceContext(
			context.level,
			context.player,
			context.hand,
			context.itemInHand,
			BlockHitResult(posToPlace, context.clickedFace, BlockPos.containing(posToPlace), true)
		)

		return place(placeContext)
	}

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
		val posToPlace = getPositionToPlace(player)
		val placeContext = BlockPlaceContext(
			level,
			player,
			usedHand,
			player.getItemInHand(usedHand),
			BlockHitResult(posToPlace, player.direction, BlockPos.containing(posToPlace), true)
		)

		return InteractionResultHolder(place(placeContext), player.getItemInHand(usedHand))
	}

	companion object {
		private fun getPositionToPlace(player: Player): Vec3 {
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

			return pos
		}

	}

}