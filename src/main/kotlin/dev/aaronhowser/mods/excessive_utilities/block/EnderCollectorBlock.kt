package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block_entity.EnderCollectorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class EnderCollectorBlock : Block(Properties.ofFullCopy(Blocks.IRON_BLOCK)), EntityBlock {


	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(POWERED, false)
				.setValue(FACING, Direction.DOWN)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(POWERED, FACING)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
		return defaultBlockState()
			.setValue(POWERED, context.level.hasNeighborSignal(context.clickedPos))
			.setValue(FACING, context.clickedFace)
	}

	override fun neighborChanged(state: BlockState, level: Level, pos: BlockPos, neighborBlock: Block, neighborPos: BlockPos, movedByPiston: Boolean) {
		val wasPowered = state.getValue(POWERED)
		val shouldBePowered = level.hasNeighborSignal(pos)

		if (wasPowered != shouldBePowered) {
			level.setBlockAndUpdate(pos, state.setValue(POWERED, shouldBePowered))
		}
	}

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		val facing = state.getValue(FACING)
		return when (facing.axis) {
			Direction.Axis.Y -> SHAPE_Y
			Direction.Axis.X -> SHAPE_X
			Direction.Axis.Z -> SHAPE_Z
		}
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return EnderCollectorBlockEntity(pos, state)
	}

	override fun <T : BlockEntity> getTicker(
		level: Level,
		state: BlockState,
		blockEntityType: BlockEntityType<T>
	): BlockEntityTicker<T>? {
		return BaseEntityBlock.createTickerHelper(
			blockEntityType,
			ModBlockEntityTypes.ENDER_COLLECTOR.get(),
			EnderCollectorBlockEntity::tick
		)
	}

	companion object {
		val POWERED: BooleanProperty = BlockStateProperties.POWERED
		val FACING: DirectionProperty = BlockStateProperties.FACING

		val SHAPE_Y: VoxelShape = box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0)
		val SHAPE_X: VoxelShape = box(0.0, 1.0, 1.0, 16.0, 15.0, 15.0)
		val SHAPE_Z: VoxelShape = box(1.0, 1.0, 0.0, 15.0, 15.0, 16.0)
	}

}