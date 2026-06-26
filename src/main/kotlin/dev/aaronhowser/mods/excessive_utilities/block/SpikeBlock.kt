package dev.aaronhowser.mods.excessive_utilities.block

import com.mojang.authlib.GameProfile
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.common.util.FakePlayerFactory
import java.util.*

class SpikeBlock(
	val damagePerHit: Float,
	val canKill: Boolean = true,
	val dropsExperience: Boolean = false,
	val killsAsPlayer: Boolean = false,
	properties: Properties
) : Block(properties) {

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

	override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
		if (entity !is LivingEntity || level !is ServerLevel || level.gameTime % 10 != 0L) return

		if (!canKill && entity.health <= damagePerHit) {
			return
		}

		if (dropsExperience) {
			entity.lastHurtByPlayerTime = 100
		}

		if (killsAsPlayer) {
			val fakePlayer = FakePlayerFactory.get(
				level,
				GameProfile(UUID.fromString("21b80106-00e9-4bf0-b903-3a1caf2da923"), "EU_SpikeBlock_Killer")
			)

			entity.hurt(level.damageSources().playerAttack(fakePlayer), damagePerHit)
		} else {
			entity.hurt(level.damageSources().cactus(), damagePerHit)
		}
	}

	companion object {
		val FACING: DirectionProperty = BlockStateProperties.FACING

		val UP_SHAPE: VoxelShape = createShape(Direction.UP)
		val DOWN_SHAPE: VoxelShape = createShape(Direction.DOWN)
		val NORTH_SHAPE: VoxelShape = createShape(Direction.NORTH)
		val SOUTH_SHAPE: VoxelShape = createShape(Direction.SOUTH)
		val WEST_SHAPE: VoxelShape = createShape(Direction.WEST)
		val EAST_SHAPE: VoxelShape = createShape(Direction.EAST)

		private fun createShape(direction: Direction): VoxelShape {
			var shape = Shapes.empty()
			val layers = 20
			val layerHeight = 16.0 / layers
			val insetStep = 16.0 / (layers * 2.0)

			fun layerBox(distanceFromBase: Double, layerMaxDistance: Double, min: Double, max: Double): VoxelShape {
				return when (direction) {
					Direction.UP -> box(min, distanceFromBase, min, max, layerMaxDistance, max)
					Direction.DOWN -> box(min, 16.0 - layerMaxDistance, min, max, 16.0 - distanceFromBase, max)
					Direction.NORTH -> box(min, min, 16.0 - layerMaxDistance, max, max, 16.0 - distanceFromBase)
					Direction.SOUTH -> box(min, min, distanceFromBase, max, max, layerMaxDistance)
					Direction.WEST -> box(16.0 - layerMaxDistance, min, min, 16.0 - distanceFromBase, max, max)
					Direction.EAST -> box(distanceFromBase, min, min, layerMaxDistance, max, max)
				}
			}

			for (i in 0 until layers) {
				val distanceFromBase = i * layerHeight
				val layerMaxDistance = distanceFromBase + layerHeight
				val inset = i * insetStep
				val min = inset
				val max = 16.0 - inset

				shape = Shapes.or(
					shape,
					layerBox(distanceFromBase, layerMaxDistance, min, max)
				)
			}

			return shape
		}
	}

}
