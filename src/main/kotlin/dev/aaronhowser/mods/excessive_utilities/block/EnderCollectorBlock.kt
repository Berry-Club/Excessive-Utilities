package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
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
				.setValue(ENABLED, false)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(ENABLED)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
		return defaultBlockState()
			.setValue(ENABLED, context.level.hasNeighborSignal(context.clickedPos))
	}

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return SHAPE
	}

	companion object {
		val ENABLED: BooleanProperty = BlockStateProperties.ENABLED
		val SHAPE: VoxelShape = box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
	}

}