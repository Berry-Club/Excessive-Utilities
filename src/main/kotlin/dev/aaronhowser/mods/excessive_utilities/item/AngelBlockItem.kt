package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.AaronExtensions.tell
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext

class AngelBlockItem(properties: Properties) : BlockItem(ModBlocks.ANGEL_BLOCK.get(), properties) {

	override fun onItemUseFirst(stack: ItemStack, context: UseOnContext): InteractionResult {
		context.player?.tell("Test")
		return super.onItemUseFirst(stack, context)
	}

}