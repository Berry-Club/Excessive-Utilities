package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BooleanProperty

class MoonStoreOreBlock : Block(Properties.ofFullCopy(Blocks.IRON_ORE)) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(VISIBLE, false)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(VISIBLE)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
		return defaultBlockState()
			.setValue(VISIBLE, shouldBeVisible(context.level))
	}

	override fun isRandomlyTicking(state: BlockState): Boolean {
		return true
	}

	override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
		val wasVisible = state.getValue(VISIBLE)
		val shouldBeVisible = shouldBeVisible(level)

		if (wasVisible != shouldBeVisible) {
			level.setBlockAndUpdate(pos, state.setValue(VISIBLE, shouldBeVisible))
		}
	}

	companion object {
		val VISIBLE: BooleanProperty = BooleanProperty.create("visible")
		fun shouldBeVisible(level: Level) = level.isNight
	}

}