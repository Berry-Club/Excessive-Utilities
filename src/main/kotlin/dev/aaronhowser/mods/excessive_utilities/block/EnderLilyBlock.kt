package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isFluid
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.ItemLike
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.LevelReader
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
					level.removeBlock(currentPos, false)
					return state
				}
			}
		}

		return super.updateShape(state, facing, facingState, level, currentPos, facingPos)
	}

	companion object {

		/** Does not remove the original */
		private fun teleportAway(level: LevelAccessor, lilyPos: BlockPos, lilyState: BlockState): Boolean {
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
					return true
				}
			}

			return false
		}

	}

}