package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.block_walker.BlockWalker
import dev.aaronhowser.mods.aaron.block_walker.WalkType
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.defaultBlockState
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.scheduleTaskInTicks
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

		val player = context.player

		val filter = BlockWalker.BlockFilter { level, pos, state ->
			state.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)
					&& level.isEmptyBlock(pos.above())
					&& (player == null || level.mayInteract(player, pos))
		}

		val blockWalker = BlockWalker(
			level = level,
			walkType = WalkType.ALL_SURROUNDING,
			startPos = pos,
			filter = filter,
			maxDistance = 5,
			maxTotalBlocks = 1000,
			onWalked = { cb ->
				val (posBlock, distance) = cb

				level.scheduleTaskInTicks(distance * 3) {
					val pos = posBlock.pos
					val stateThereNow = level.getBlockState(pos)
					if (filter.test(level, pos, stateThereNow)) {
						level.setBlockAndUpdate(pos, ModBlocks.CURSED_EARTH.defaultBlockState())
					}
				}
			}
		)

		blockWalker.start(10000)

		return InteractionResult.SUCCESS
	}

}