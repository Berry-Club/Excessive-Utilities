package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.QuantumQuarryActuatorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty

class QuantumQuarryActuatorBlock : Block(Properties.ofFullCopy(Blocks.OBSIDIAN)), EntityBlock {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(FACING, Direction.UP)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(FACING)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
		val pos = context.clickedPos
		val level = context.level

		val quarryDirection = Direction.entries.find { direction ->
			val adjacentPos = pos.relative(direction)
			val adjacentState = level.getBlockState(adjacentPos)
			adjacentState.isBlock(ModBlocks.QUANTUM_QUARRY)
		}

		return if (quarryDirection != null) {
			defaultBlockState().setValue(FACING, quarryDirection.opposite)
		} else {
			null
		}
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		val actuator = QuantumQuarryActuatorBlockEntity(pos, state)

		val quarryDirection = state.getValue(FACING).opposite
		val quarryPos = pos.relative(quarryDirection)
		actuator.quantumQuarryPos = quarryPos

		return actuator
	}

	override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
		val adjacentQuantumQuarries = Direction.entries.count { direction ->
			val adjacentPos = pos.relative(direction)
			val adjacentState = level.getBlockState(adjacentPos)
			adjacentState.isBlock(ModBlocks.QUANTUM_QUARRY)
		}

		return adjacentQuantumQuarries == 1
	}

	override fun updateShape(
		state: BlockState,
		direction: Direction,
		neighborState: BlockState,
		level: LevelAccessor,
		pos: BlockPos,
		neighborPos: BlockPos
	): BlockState {
		return if (canSurvive(state, level, pos)) {
			state
		} else {
			Blocks.AIR.defaultBlockState()
		}
	}

	companion object {
		val FACING: DirectionProperty = BlockStateProperties.FACING
	}

}