package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.defaultBlockState
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

class DropOfEvilItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level
		val pos = context.clickedPos
		val blockState = level.getBlockState(pos)
		if (!blockState.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)) return InteractionResult.PASS

		level.setBlockAndUpdate(pos, ModBlocks.CURSED_EARTH.defaultBlockState())

		return InteractionResult.SUCCESS
	}

}