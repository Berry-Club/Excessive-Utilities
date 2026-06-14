package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.oneIn
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
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
		actuallyTick(level, pos, state, random, fastSpreading = false)
	}

	// Manually called when initially created
	override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
		actuallyTick(level, pos, state, random, fastSpreading = true)
	}

	private fun actuallyTick(
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

		if (fastSpreading) {
			doFastSpread(level, pos, random)
		}
	}

	private fun doFastSpread(
		level: ServerLevel,
		pos: BlockPos,
		random: RandomSource
	) {
		val candidates = BlockPos.betweenClosed(
			pos.offset(-1, -2, -1),
			pos.offset(1, 2, 1)
		)
	}

	private fun trySpread(
		level: ServerLevel,
		fromPos: BlockPos,
		targetPos: BlockPos,
		random: RandomSource,
		fastSpreading: Boolean
	): Boolean {
		if (!level.isLoaded(targetPos)) return false

		val targetState = level.getBlockState(targetPos)
		if (!targetState.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)) return false

		val posAbove = targetPos.above()
		val stateAbove = level.getBlockState(posAbove)
		if (stateAbove.getLightBlock(level, posAbove) > 2) return false

		val decayForTarget = getDecayForPos(level, targetPos, random)
		if (decayForTarget > MAX_DECAY) return false

		level.setBlockAndUpdate(
			targetPos,
			defaultBlockState().setValue(DECAY, decayForTarget)
		)

		return true
	}

	// Decay starts at 0 and goes up the farther out.
	// If it would be over max, then it shouldn't place it there at all
	private fun getDecayForPos(
		level: Level,
		pos: BlockPos,
		random: RandomSource
	): Int {
		val neighbors = BlockPos.betweenClosed(
			pos.offset(-1, -1, -1),
			pos.offset(1, 1, 1)
		)

		var lowestDecay = MAX_DECAY

		for (neighbor in neighbors) {
			val stateThere = level.getBlockState(neighbor)
			if (!stateThere.isBlock(this)) continue

			val decayThere = stateThere.getValue(DECAY)
			if (decayThere < lowestDecay) {
				lowestDecay = decayThere
			}
		}

		return lowestDecay + 1 + random.nextInt(2)
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
		const val MAX_DECAY = 15
		val DECAY: IntegerProperty = IntegerProperty.create("decay", 0, MAX_DECAY)

		private fun isFireNearby(level: Level, pos: BlockPos, random: RandomSource): Boolean {
			val randomNearby = BlockPos.randomInCube(random, 10, pos, 4)
			return randomNearby.any { level.getBlockState(it).isBlock(BlockTags.FIRE) }
		}
	}

}