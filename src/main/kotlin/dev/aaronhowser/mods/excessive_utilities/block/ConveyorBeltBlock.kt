package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraft.world.entity.InsideBlockEffectApplier
import kotlin.math.abs

class ConveyorBeltBlock(properties: Properties) : Block(properties) {

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

	override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity, effectApplier: InsideBlockEffectApplier, isPrecise: Boolean) {
		if (entity.isCrouching) return

		moveEntityUp(state, pos, entity)
		moveEntityForward(state, entity)

	}

	companion object {
		val FACING: EnumProperty<Direction> = BlockStateProperties.HORIZONTAL_FACING
		val SHAPE: VoxelShape = box(0.1, 0.1, 0.1, 15.9, 15.9, 15.9)

		private fun moveEntityUp(state: BlockState, pos: BlockPos, entity: Entity) {
			if (!entity.onGround() || !entity.horizontalCollision) return

			val direction = state.getValue(FACING)
			val entityPos = BlockPos.containing(entity.position().add(0.0, 0.2, 0.0))
			val blockInFront = entityPos.relative(direction)

			if (entity.level().getBlockState(blockInFront).isBlock(ModBlocks.CONVEYOR_BELT)) {
				entity.setPos(entity.x, pos.y + 1.2, entity.z)
			}
		}

		private fun moveEntityForward(state: BlockState, entity: Entity) {
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
	}

}











