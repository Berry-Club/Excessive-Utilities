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
			.setValue(FACING, context.clickedFace)
	}

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return when (state.getValue(FACING)) {
			Direction.UP -> UP_SHAPE
			Direction.DOWN -> DOWN_SHAPE
			Direction.NORTH -> NORTH_SHAPE
			Direction.SOUTH -> SOUTH_SHAPE
			Direction.WEST -> WEST_SHAPE
			Direction.EAST -> EAST_SHAPE
		}
	}

	companion object {
		val FACING: DirectionProperty = BlockStateProperties.FACING

		private val BOTTOM_WIDTH_BOUNDS = 1.0 to 15.0
		private val BOTTOM_HEIGHT_BOUNDS = 0.0 to 7.0
		private val TOP_WIDTH_BOUNDS = 4.0 to 12.0
		private val TOP_HEIGHT_BOUNDS = 7.0 to 15.0

		val UP_SHAPE: VoxelShape =
			createShape(Direction.UP)

		val DOWN_SHAPE: VoxelShape =
			createShape(Direction.DOWN)

		val NORTH_SHAPE: VoxelShape =
			createShape(Direction.NORTH)

		val SOUTH_SHAPE: VoxelShape =
			createShape(Direction.SOUTH)

		val WEST_SHAPE: VoxelShape =
			createShape(Direction.WEST)

		val EAST_SHAPE: VoxelShape =
			createShape(Direction.EAST)

		private fun createShape(direction: Direction): VoxelShape {
			fun directionalBox(widthBounds: Pair<Double, Double>, heightBounds: Pair<Double, Double>): VoxelShape {
				val min = widthBounds.first
				val max = widthBounds.second
				val from = heightBounds.first
				val to = heightBounds.second

				return when (direction) {
					Direction.UP -> box(min, from, min, max, to, max)
					Direction.DOWN -> box(min, 16.0 - to, min, max, 16.0 - from, max)
					Direction.NORTH -> box(min, min, 16.0 - to, max, max, 16.0 - from)
					Direction.SOUTH -> box(min, min, from, max, max, to)
					Direction.WEST -> box(16.0 - to, min, min, 16.0 - from, max, max)
					Direction.EAST -> box(from, min, min, to, max, max)
				}
			}

			return Shapes.or(
				directionalBox(BOTTOM_WIDTH_BOUNDS, BOTTOM_HEIGHT_BOUNDS),
				directionalBox(TOP_WIDTH_BOUNDS, TOP_HEIGHT_BOUNDS)
			)
		}

	}

}
