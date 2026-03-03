package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block.entity.QuantumQuarryActuatorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class QuantumQuarryActuatorBlock : Block(Properties.ofFullCopy(Blocks.OBSIDIAN)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return QuantumQuarryActuatorBlockEntity(pos, state)
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

}