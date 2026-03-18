package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDyeName
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.state.BlockState

class PaintbrushItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val player = context.player ?: return InteractionResult.PASS

		val level = context.level
		if (level.isClientSide || !level.mayInteract(player, context.clickedPos)) return InteractionResult.PASS

		val pos = context.clickedPos
		val blockState = level.getBlockState(pos)

		val stack = context.itemInHand

		if (player.isSecondaryUseActive) {
			if (setBrushColor(blockState, stack)) {
				return InteractionResult.SUCCESS
			}
		} else {
			if (setBlockColor(blockState, stack)) {
				return InteractionResult.SUCCESS
			}
		}

		return InteractionResult.PASS
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties =
			{
				Properties()
					.stacksTo(1)
					.component(ModDataComponents.COLOR, DyeColor.WHITE)
			}

		private fun setBrushColor(blockState: BlockState, brushStack: ItemStack): Boolean {
			for (dyeColor in DyeColor.entries) {
				val tag = TagKey.create(
					Registries.BLOCK,
					ResourceLocation.fromNamespaceAndPath(
						"c",
						"dyed/${dyeColor.getDyeName()}"
					)
				)

				if (blockState.isBlock(tag)) {
					brushStack.set(ModDataComponents.COLOR, dyeColor)
					return true
				}
			}

			return false
		}

		private fun setBlockColor(blockState: BlockState, brushStack: ItemStack): Boolean {
			return false
		}
	}

}