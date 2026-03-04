package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import kotlin.math.abs

class ConveyorBeltBlock : Block(Properties.ofFullCopy(Blocks.IRON_BLOCK)) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(FACING)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
		return defaultBlockState()
			.setValue(FACING, context.horizontalDirection)
	}

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return SHAPE
	}

	//TODO: Step up blocks if needed
	override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
		if (entity.isCrouching) return

		val movementDirection = state.getValue(FACING)

		val currentVelocity = entity.deltaMovement
		val movementInDirection = if (movementDirection.axis == Direction.Axis.Z) {
			abs(currentVelocity.z)
		} else {
			abs(currentVelocity.x)
		}

		val requiredSpeed = ServerConfig.CONFIG.conveyorBeltSpeed.get()
		if (movementInDirection < requiredSpeed) {
			val difference = requiredSpeed - movementInDirection
			val newVelocity = when (movementDirection) {
				Direction.NORTH -> currentVelocity.add(0.0, 0.0, -difference)
				Direction.SOUTH -> currentVelocity.add(0.0, 0.0, difference)
				Direction.WEST -> currentVelocity.add(-difference, 0.0, 0.0)
				Direction.EAST -> currentVelocity.add(difference, 0.0, 0.0)
				else -> currentVelocity
			}

			entity.deltaMovement = newVelocity
		}

	}

	companion object {
		val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
		val SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 15.9, 16.0)
	}

}