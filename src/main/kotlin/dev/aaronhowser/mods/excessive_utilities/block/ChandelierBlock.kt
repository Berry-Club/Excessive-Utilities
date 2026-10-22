package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block_entity.ChandelierBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.util.RandomSource
import net.minecraft.world.level.ScheduledTickAccess

class ChandelierBlock(properties: Properties) : Block(properties), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return ChandelierBlockEntity(pos, state)
	}

	override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
		return canSupportCenter(level, pos.above(), Direction.DOWN)
	}

	override fun updateShape(
		state: BlockState,
		level: LevelReader,
		 ticks: ScheduledTickAccess,
		pos: BlockPos,
		 directionToNeighbour: Direction,
		 neighbourPos: BlockPos,
		 neighbourState: BlockState,
		 random: RandomSource
	): BlockState {
		return if (canSurvive(state, level, pos)) {
			state
		} else {
			Blocks.AIR.defaultBlockState()
		}
	}

}










