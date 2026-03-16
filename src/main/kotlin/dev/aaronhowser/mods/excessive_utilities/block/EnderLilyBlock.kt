package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isFluid
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.util.RandomSource
import net.minecraft.world.level.*
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.neoforged.neoforge.common.Tags
import kotlin.jvm.optionals.getOrDefault

class EnderLilyBlock : CropBlock(Properties.ofFullCopy(Blocks.WHEAT)) {

	override fun getBaseSeedId(): ItemLike = ModItems.ENDER_LILY.get()

	override fun mayPlaceOn(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
		return state.isFaceSturdy(level, pos, Direction.UP)
	}

	override fun canSurvive(state: BlockState, level: LevelReader, pos: BlockPos): Boolean {
		val posBelow = pos.below()
		val stateBelow = level.getBlockState(posBelow)
		return stateBelow.isFaceSturdy(level, posBelow, Direction.UP)
	}

	override fun isValidBonemealTarget(level: LevelReader, pos: BlockPos, state: BlockState): Boolean {
		return state.getValue(AGE) > 0
	}

	override fun performBonemeal(level: ServerLevel, random: RandomSource, pos: BlockPos, state: BlockState) {
		val newAge = state.getValue(AGE) - 1
		if (newAge < 0) return

		val newState = state.setValue(AGE, newAge)
		level.setBlock(pos, newState, 2)

		level.playSound(
			null,
			pos,
			SoundEvents.ENDERMAN_HURT,
			SoundSource.BLOCKS,
			1f,
			1f
		)
	}

	override fun growCrops(level: Level, pos: BlockPos, state: BlockState) {
		super.growCrops(level, pos, state)

		if (level.getBlockState(pos.below()).isBlock(Tags.Blocks.END_STONES)) {
			super.growCrops(level, pos, state)
		}
	}

	override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
		if (!ServerConfig.CONFIG.funnyEnderLilyTeleporting.get()) return

		for (dir in Direction.entries) {
			val posThere = pos.relative(dir)
			val stateThere = level.getBlockState(posThere)
			val fluidStateThere = level.getFluidState(posThere)

			val shouldTeleport = fluidStateThere.isFluid(Tags.Fluids.WATER)
					|| stateThere.getOptionalValue(BlockStateProperties.WATERLOGGED).getOrDefault(false)
					|| stateThere.getOptionalValue(BlockStateProperties.MOISTURE).getOrDefault(0) > 0

			if (shouldTeleport) {
				val success = teleportAway(level, pos, state)
				if (success) {
					return
				}
			}
		}

	}

	override fun updateShape(
		state: BlockState,
		facing: Direction,
		facingState: BlockState,
		level: LevelAccessor,
		currentPos: BlockPos,
		facingPos: BlockPos
	): BlockState {
		if (ServerConfig.CONFIG.funnyEnderLilyTeleporting.get()) {
			val shouldTeleportAway = level.getFluidState(facingPos).isFluid(Tags.Fluids.WATER)
					|| facingState.getOptionalValue(BlockStateProperties.WATERLOGGED).getOrDefault(false)
					|| facingState.getOptionalValue(BlockStateProperties.MOISTURE).getOrDefault(0) > 0

			if (shouldTeleportAway) {
				val success = teleportAway(level, currentPos, state)
				if (success) {
					return state
				}
			}
		}

		return super.updateShape(state, facing, facingState, level, currentPos, facingPos)
	}

	companion object {

		/** Removes the original */
		fun teleportAway(level: LevelAccessor, lilyPos: BlockPos, lilyState: BlockState): Boolean {
			val mutable = lilyPos.mutable()
			val radius = 16

			for (i in 0 until 1000) {
				mutable.set(
					lilyPos.x + level.random.nextRange(-radius, radius),
					lilyPos.y + level.random.nextRange(-radius, radius),
					lilyPos.z + level.random.nextRange(-radius, radius)
				)

				val stateThere = level.getBlockState(mutable)
				if (stateThere.canBeReplaced() && lilyState.canSurvive(level, mutable)) {
					level.setBlock(mutable, lilyState, 3)

					level.playSound(
						null,
						lilyPos,
						SoundEvents.ENDERMAN_TELEPORT,
						SoundSource.BLOCKS,
						1f,
						1f
					)

					level.removeBlock(lilyPos, false)
					return true
				}
			}

			return false
		}

	}

}