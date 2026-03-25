package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class EnderCollectorBlock : Block(Properties.ofFullCopy(Blocks.IRON_BLOCK)) {


	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(POWERED, false)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(POWERED)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
		return defaultBlockState()
			.setValue(POWERED, context.level.hasNeighborSignal(context.clickedPos))
	}

	override fun neighborChanged(state: BlockState, level: Level, pos: BlockPos, neighborBlock: Block, neighborPos: BlockPos, movedByPiston: Boolean) {
		val wasPowered = state.getValue(POWERED)
		val shouldBePowered = level.hasNeighborSignal(pos)

		if (wasPowered != shouldBePowered) {
			level.setBlockAndUpdate(pos, state.setValue(POWERED, shouldBePowered))
		}
	}

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return SHAPE
	}

	companion object {
		val POWERED: BooleanProperty = BlockStateProperties.POWERED
		val SHAPE: VoxelShape = box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
	}

}