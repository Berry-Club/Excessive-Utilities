package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.block_walker.BlockWalker
import dev.aaronhowser.mods.aaron.block_walker.WalkType
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.defaultBlockState
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.block.Block

class DropOfEvilItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level
		val pos = context.clickedPos
		val blockState = level.getBlockState(pos)
		if (!blockState.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)) return InteractionResult.PASS

		val blockWalker = BlockWalker(
			level = level,
			walkType = WalkType.ALL_SURROUNDING,
			startPos = pos,
			filter = { level, pos, state ->
				state.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE) && level.isEmptyBlock(pos.above())
			},
			maxDistance = 5,
			maxTotalBlocks = 1000,
			onFinished = { walkedBlocks ->
				for (block in walkedBlocks) {
					val (pos, state) = block.block

					if (state.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)) {
						level.setBlock(pos, ModBlocks.CURSED_EARTH.defaultBlockState(), Block.UPDATE_ALL)
					}
				}
			})

		blockWalker.start(3)

		return InteractionResult.SUCCESS
	}

}