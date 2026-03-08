package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class EnderFluxCrystalBlock : Block(Properties.ofFullCopy(Blocks.OBSIDIAN)) {

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
		return defaultBlockState()
			.setValue(FACING, context.nearestLookingDirection.opposite)
	}

	//TODO: Rotate the shape based on the facing direction
	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return UP_SHAPE
	}

	companion object {
		val FACING: DirectionProperty = BlockStateProperties.FACING

		private val BOTTOM_WIDTH_BOUNDS = 1.0 to 15.0
		private val BOTTOM_HEIGHT_BOUNDS = 0.0 to 7.0
		private val TOP_WIDTH_BOUNDS = 4.0 to 12.0
		private val TOP_HEIGHT_BOUNDS = 7.0 to 15.0

		val UP_SHAPE: VoxelShape =
			Shapes.or(
				box(
					BOTTOM_WIDTH_BOUNDS.first, BOTTOM_HEIGHT_BOUNDS.first, BOTTOM_WIDTH_BOUNDS.first,
					BOTTOM_WIDTH_BOUNDS.second, BOTTOM_HEIGHT_BOUNDS.second, BOTTOM_WIDTH_BOUNDS.second
				),
				box(
					TOP_WIDTH_BOUNDS.first, TOP_HEIGHT_BOUNDS.first, TOP_WIDTH_BOUNDS.first,
					TOP_WIDTH_BOUNDS.second, TOP_HEIGHT_BOUNDS.second, TOP_WIDTH_BOUNDS.second
				)
			)

	}

}