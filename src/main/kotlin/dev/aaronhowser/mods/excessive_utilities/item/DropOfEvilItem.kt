package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.block_walker.BlockWalker
import dev.aaronhowser.mods.aaron.block_walker.WalkType
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.defaultBlockState
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.aaron.scheduler.SchedulerExtensions.scheduleTaskInTicks
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.util.Mth
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level

class DropOfEvilItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level
		val pos = context.clickedPos
		val blockState = level.getBlockState(pos)

		val player = context.player

		if (!blockState.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)) {
			placeCursedEarth(player, level, pos)
			return InteractionResult.SUCCESS
		}

		return InteractionResult.SUCCESS
	}

	companion object {
		fun placeCursedEarth(
			player: Player?,
			level: Level,
			pos: BlockPos
		) {
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

					if (distance == 0 || level.random.chance(0.7)) {
						val delay = Mth.ceil(distance * 3 * level.random.nextRange(0.7, 2.0))
						level.scheduleTaskInTicks(delay) {
							val pos = posBlock.pos
							val stateThereNow = level.getBlockState(pos)
							if (filter.test(level, pos, stateThereNow)) {
								level.setBlockAndUpdate(pos, ModBlocks.CURSED_EARTH.defaultBlockState())
							}
						}
					}
				}
			)

			blockWalker.start(10000)
		}
	}

}