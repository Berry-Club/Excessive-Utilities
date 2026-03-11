package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.neoforged.neoforge.capabilities.Capabilities


class TransferNodeBlock(
	val type: Type,
	val isRetrieval: Boolean
) : Block(
	Properties.of()
		.strength(1.5f, 6f)
		.requiresCorrectToolForDrops()
		.noOcclusion()
) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(NORTH, false)
				.setValue(EAST, false)
				.setValue(SOUTH, false)
				.setValue(WEST, false)
				.setValue(UP, false)
				.setValue(DOWN, false)
		)
	}

	fun canConnectTo(level: Level, pipePos: BlockPos, direction: Direction): Boolean {
		val neighborPos = pipePos.relative(direction)

		val neighborBlock = level.getBlockState(neighborPos).block
		if (neighborBlock is TransferPipeBlock) return true

		when (type) {
			Type.ITEM -> {
				val hasItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, neighborPos, direction.opposite) != null
				if (hasItemHandler) return true
			}

			Type.FLUID -> {
				val hasFluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, direction.opposite) != null
				if (hasFluidHandler) return true
			}

			Type.ENERGY -> {
				val hasEnergyHandler = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, direction.opposite) != null
				return hasEnergyHandler
			}
		}

		return false
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN)
	}

	companion object {
		val NORTH: BooleanProperty = BlockStateProperties.NORTH
		val SOUTH: BooleanProperty = BlockStateProperties.SOUTH
		val EAST: BooleanProperty = BlockStateProperties.EAST
		val WEST: BooleanProperty = BlockStateProperties.WEST
		val UP: BooleanProperty = BlockStateProperties.UP
		val DOWN: BooleanProperty = BlockStateProperties.DOWN

		// Same order as the Direction enum, so we can use the ordinal as an index
		val CONNECTIONS: Array<BooleanProperty> = arrayOf(DOWN, UP, NORTH, SOUTH, WEST, EAST)

	}

	enum class Type {
		ITEM, FLUID, ENERGY
	}

}