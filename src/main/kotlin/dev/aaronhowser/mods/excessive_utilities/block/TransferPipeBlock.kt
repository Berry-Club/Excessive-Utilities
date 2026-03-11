package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.neoforged.neoforge.capabilities.Capabilities

class TransferPipeBlock : Block(Properties.of().strength(0.5f).noOcclusion()) {

	private fun updateConnections(level: Level, pos: BlockPos, blockedDirections: Int): BlockState {
		val state = defaultBlockState().setValue(BLOCKED_DIRECTIONS, blockedDirections)

		for (dir in Direction.entries) {

		}

		return state
	}

	companion object {
		val NORTH: BooleanProperty = BlockStateProperties.NORTH
		val EAST: BooleanProperty = BlockStateProperties.EAST
		val SOUTH: BooleanProperty = BlockStateProperties.SOUTH
		val WEST: BooleanProperty = BlockStateProperties.WEST
		val UP: BooleanProperty = BlockStateProperties.UP
		val DOWN: BooleanProperty = BlockStateProperties.DOWN
		val BLOCKED_DIRECTIONS: IntegerProperty = IntegerProperty.create("blocked_directions", 0x000000, 0x111111)

		private val CONNECTIONS: Array<BooleanProperty> = arrayOf(DOWN, UP, NORTH, SOUTH, WEST, EAST)

		private fun canConnectTo(level: Level, pipePos: BlockPos, direction: Direction): Boolean {
			val neighborPos = pipePos.relative(direction)

			val neighborBlock = level.getBlockState(neighborPos).block
			if (neighborBlock is TransferPipeBlock) return true
			if (neighborBlock is TransferNodeBlock) return true

			val hasItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, neighborPos, direction.opposite) != null
			if (hasItemHandler) return true

			val hasFluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, direction.opposite) != null
			if (hasFluidHandler) return true

			val hasEnergyHandler = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, direction.opposite) != null
			return hasEnergyHandler
		}

		fun toggleBlocked(level: Level, pipePos: BlockPos, direction: Direction) {
			val state = level.getBlockState(pipePos)
			val pipeBlock = state.block as? TransferPipeBlock ?: return

			val blockedDirections = state.getValue(BLOCKED_DIRECTIONS)
			val newBlockedDirections = blockedDirections xor (1 shl direction.ordinal)

			val newState = pipeBlock.updateConnections(level, pipePos, newBlockedDirections)
			level.setBlockAndUpdate(pipePos, newState)
		}
	}

}