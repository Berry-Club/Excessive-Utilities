package dev.aaronhowser.mods.excessive_utilities.feature

import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.DepthsDimConstants
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration

class DepthsSculkVeinFeature : Feature<NoneFeatureConfiguration>(NoneFeatureConfiguration.CODEC) {

	override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration>): Boolean {
		val origin = context.origin()
		val chunkX = origin.x.floorDiv(16)
		val chunkZ = origin.z.floorDiv(16)

		var placed = false

		for (regionX in chunkX.floorDiv(REGION_SIZE_IN_CHUNKS) - 1..chunkX.floorDiv(REGION_SIZE_IN_CHUNKS) + 1) {
			for (regionZ in chunkZ.floorDiv(REGION_SIZE_IN_CHUNKS) - 1..chunkZ.floorDiv(REGION_SIZE_IN_CHUNKS) + 1) {
				placed = placeRegionVeinsInChunk(context, regionX, regionZ, chunkX, chunkZ) || placed
			}
		}

		return placed
	}

	private fun placeRegionVeinsInChunk(context: FeaturePlaceContext<NoneFeatureConfiguration>, regionX: Int, regionZ: Int, chunkX: Int, chunkZ: Int): Boolean {
		val random = RandomSource.create(context.level().seed + regionX * 341873128712L + regionZ * 132897987541L + SEED_SALT)
		var placed = false

		repeat(VEINS_PER_REGION) {
			val startX = regionX * REGION_SIZE_IN_BLOCKS + random.nextInt(REGION_SIZE_IN_BLOCKS)
			val startY = MIN_VEIN_Y + random.nextInt(MAX_VEIN_Y - MIN_VEIN_Y + 1)
			val startZ = regionZ * REGION_SIZE_IN_BLOCKS + random.nextInt(REGION_SIZE_IN_BLOCKS)
			val length = MIN_VEIN_LENGTH + random.nextInt(VEIN_LENGTH_RANGE)

			placed = placeVein(context, chunkX, chunkZ, random, startX, startY, startZ, length) || placed
		}

		return placed
	}

	private fun placeVein(
		context: FeaturePlaceContext<NoneFeatureConfiguration>,
		chunkX: Int,
		chunkZ: Int,
		random: RandomSource,
		startX: Int,
		startY: Int,
		startZ: Int,
		length: Int
	): Boolean {
		var x = startX
		var y = startY
		var z = startZ
		var xStep = randomStep(random)
		var yStep = randomStep(random)
		var zStep = randomStep(random)
		if (xStep == 0 && zStep == 0) xStep = randomSign(random)
		var placed = false

		for (step in 0 until length) {
			placed = placeVeinNode(context, chunkX, chunkZ, random, x, y, z) || placed

			if (step > 0 && step % BRANCH_SPACING == 0 && random.nextFloat() < BRANCH_CHANCE) {
				placed = placeBranch(context, chunkX, chunkZ, random, x, y, z) || placed
			}

			if (random.nextFloat() < DIRECTION_CHANGE_CHANCE) xStep = randomStep(random)
			if (random.nextFloat() < VERTICAL_CHANGE_CHANCE) yStep = randomStep(random)
			if (random.nextFloat() < DIRECTION_CHANGE_CHANCE) zStep = randomStep(random)
			if (xStep == 0 && zStep == 0) zStep = randomSign(random)

			x += xStep
			z += zStep
			if (random.nextFloat() < VERTICAL_MOVE_CHANCE) y += yStep

			y = y.coerceIn(MIN_VEIN_Y, MAX_VEIN_Y)
		}

		return placed
	}

	private fun placeBranch(
		context: FeaturePlaceContext<NoneFeatureConfiguration>,
		chunkX: Int,
		chunkZ: Int,
		random: RandomSource,
		startX: Int,
		startY: Int,
		startZ: Int
	): Boolean {
		var x = startX
		var y = startY
		var z = startZ
		var xStep = randomStep(random)
		var yStep = randomStep(random)
		var zStep = randomStep(random)
		if (xStep == 0 && zStep == 0) xStep = randomSign(random)
		var placed = false
		val length = MIN_BRANCH_LENGTH + random.nextInt(BRANCH_LENGTH_RANGE)

		for (step in 0 until length) {
			placed = placeVeinNode(context, chunkX, chunkZ, random, x, y, z) || placed

			if (step % 4 == 0) {
				xStep = randomStep(random)
				yStep = randomStep(random)
				zStep = randomStep(random)
				if (xStep == 0 && zStep == 0) zStep = randomSign(random)
			}

			x += xStep
			z += zStep
			if (random.nextFloat() < BRANCH_VERTICAL_MOVE_CHANCE) y += yStep

			y = y.coerceIn(MIN_VEIN_Y, MAX_VEIN_Y)
		}

		return placed
	}

	private fun placeVeinNode(context: FeaturePlaceContext<NoneFeatureConfiguration>, chunkX: Int, chunkZ: Int, random: RandomSource, x: Int, y: Int, z: Int): Boolean {
		val radius = if (random.nextFloat() < WIDE_NODE_CHANCE) WIDE_NODE_RADIUS else VEIN_RADIUS
		var placed = false

		for (xOffset in -radius..radius) {
			for (yOffset in -radius..radius) {
				for (zOffset in -radius..radius) {
					val distance = kotlin.math.abs(xOffset) + kotlin.math.abs(yOffset) + kotlin.math.abs(zOffset)
					if (distance > radius) continue
					if (distance == radius && random.nextFloat() > EDGE_BLOCK_CHANCE) continue

					placed = tryPlaceSculk(context, chunkX, chunkZ, x + xOffset, y + yOffset, z + zOffset) || placed
				}
			}
		}

		return placed
	}

	private fun tryPlaceSculk(context: FeaturePlaceContext<NoneFeatureConfiguration>, chunkX: Int, chunkZ: Int, x: Int, y: Int, z: Int): Boolean {
		if (x !in chunkX * 16..chunkX * 16 + 15 || z !in chunkZ * 16..chunkZ * 16 + 15) return false
		if (y !in MIN_VEIN_Y..MAX_VEIN_Y) return false

		val pos = BlockPos(x, y, z)
		val state = context.level().getBlockState(pos)
		if (state.block != Blocks.DEEPSLATE && state.block != Blocks.COBBLED_DEEPSLATE) return false

		context.level().setBlock(pos, Blocks.SCULK.defaultBlockState(), Block.UPDATE_CLIENTS)
		return true
	}

	private fun randomStep(random: RandomSource): Int {
		return random.nextInt(3) - 1
	}

	private fun randomSign(random: RandomSource): Int {
		return if (random.nextBoolean()) 1 else -1
	}

	companion object {
		const val REGION_SIZE_IN_CHUNKS = 4
		const val REGION_SIZE_IN_BLOCKS = REGION_SIZE_IN_CHUNKS * 16

		const val VEINS_PER_REGION = 18
		const val MIN_VEIN_LENGTH = 72
		const val VEIN_LENGTH_RANGE = 57

		const val BRANCH_SPACING = 18
		const val BRANCH_CHANCE = 0.55f
		const val MIN_BRANCH_LENGTH = 14
		const val BRANCH_LENGTH_RANGE = 19

		const val VEIN_RADIUS = 1
		const val WIDE_NODE_CHANCE = 0.28f
		const val WIDE_NODE_RADIUS = 2
		const val EDGE_BLOCK_CHANCE = 0.75f

		const val DIRECTION_CHANGE_CHANCE = 0.22f
		const val VERTICAL_CHANGE_CHANCE = 0.35f
		const val VERTICAL_MOVE_CHANCE = 0.55f
		const val BRANCH_VERTICAL_MOVE_CHANCE = 0.65f

		const val MIN_VEIN_Y = DepthsDimConstants.MIN_Y + 4
		const val MAX_VEIN_Y = DepthsDimConstants.FLOOR_TOP + DepthsDimConstants.BLEND_THICKNESS + 4

		const val SEED_SALT = 81237291L
	}

}
