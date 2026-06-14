package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.oneIn
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.IntegerProperty

class CursedEarthBlockNew : Block(Properties.ofFullCopy(Blocks.GRASS_BLOCK)) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(DECAY, 0)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(DECAY)
	}

	override fun isRandomlyTicking(state: BlockState): Boolean = true

	// Slow spreading and mob spawning
	override fun randomTick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
		performTick(level, pos, state, random, fastSpreading = false)
	}

	// Manually called when initially created
	override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
		performTick(level, pos, state, random, fastSpreading = true)
	}

	private fun performTick(
		level: ServerLevel,
		pos: BlockPos,
		state: BlockState,
		random: RandomSource,
		fastSpreading: Boolean
	) {
		val lightAbove = level.getRawBrightness(pos.above(), 0)
		if (lightAbove >= 9) {
			val handledFire = handleFire(level, pos, random)
			if (handledFire) return
		}
	}

	private fun handleFire(
		level: ServerLevel,
		pos: BlockPos,
		random: RandomSource
	): Boolean {
		val stateAbove = level.getBlockState(pos.above())
		val isFireAbove = stateAbove.isBlock(BlockTags.FIRE)

		if (isFireAbove && random.oneIn(5)) {
			level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState())
			return true
		}

		val isFireNearby = isFireAbove || isFireNearby(level, pos, random)
		if (!isFireNearby) return false

		burnNearbyCursedEarth(level, pos, random)
		return true
	}

	private fun burnNearbyCursedEarth(
		level: ServerLevel,
		pos: BlockPos,
		random: RandomSource
	) {
		val nearbyPositions = BlockPos.randomInCube(random, 40, pos, 4)

		for (targetPos in nearbyPositions) {
			val stateThere = level.getBlockState(targetPos)
			if (!stateThere.isBlock(this)) continue

			val posAbove = targetPos.above()
			val stateAbove = level.getBlockState(posAbove)
			if (stateAbove.isFlammable(level, posAbove, Direction.UP)) {
				level.setBlockAndUpdate(posAbove, Blocks.FIRE.defaultBlockState())
			} else {
				level.setBlockAndUpdate(targetPos, Blocks.DIRT.defaultBlockState())
			}
		}
	}

	companion object {
		val DECAY: IntegerProperty = IntegerProperty.create("decay", 0, 15)

		private fun isFireNearby(level: Level, pos: BlockPos, random: RandomSource): Boolean {
			val randomNearby = BlockPos.randomInCube(random, 10, pos, 4)
			return randomNearby.any { level.getBlockState(it).isBlock(BlockTags.FIRE) }
		}
	}

}