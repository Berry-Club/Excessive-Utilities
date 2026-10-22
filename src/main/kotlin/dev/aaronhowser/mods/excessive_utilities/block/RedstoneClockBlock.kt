package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.SignalGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.redstone.Orientation

class RedstoneClockBlock(properties: Properties) : Block(properties) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(POWERED, false)
				.setValue(ENABLED, false)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(POWERED, ENABLED)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
		return defaultBlockState()
			.setValue(POWERED, context.level.hasNeighborSignal(context.clickedPos))
	}

	override fun isSignalSource(state: BlockState): Boolean = true
	override fun shouldCheckWeakPower(state: BlockState, level: SignalGetter, pos: BlockPos, side: Direction): Boolean = false

	override fun getSignal(state: BlockState, level: BlockGetter, pos: BlockPos, direction: Direction): Int {
		val isPowered = state.getValue(POWERED)
		if (!isPowered) return 0

		val enabled = state.getValue(ENABLED)
		return if (enabled) 15 else 0
	}

	override fun neighborChanged(state: BlockState, level: Level, pos: BlockPos, neighborBlock: Block, orientation: Orientation?, movedByPiston: Boolean) {
		val wasPowered = state.getValue(POWERED)
		val shouldBePowered = level.hasNeighborSignal(pos)

		if (wasPowered != shouldBePowered) {
			level.setBlockAndUpdate(pos, state.setValue(POWERED, shouldBePowered))
			level.scheduleTick(pos, this, 1)
		}
	}

	override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
		if (level is ServerLevel && !state.isBlock(oldState.block)) {
			level.scheduleTick(pos, this, 1)
		}
	}

	override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
		val powered = state.getValue(POWERED)
		if (!powered) return

		val enabled = state.getValue(ENABLED)
		if (enabled) {
			level.setBlockAndUpdate(pos, state.setValue(ENABLED, false))
			level.scheduleTick(pos, this, 19)
		} else {
			level.setBlockAndUpdate(pos, state.setValue(ENABLED, true))
			level.scheduleTick(pos, this, 1)
		}
	}

	companion object {
		val POWERED: BooleanProperty = BlockStateProperties.POWERED
		val ENABLED: BooleanProperty = BlockStateProperties.ENABLED
	}

}










