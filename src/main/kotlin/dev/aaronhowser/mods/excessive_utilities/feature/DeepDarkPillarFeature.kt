package dev.aaronhowser.mods.excessive_utilities.feature

import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.DeepDarkConstants
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.sqrt

class DeepDarkPillarFeature : Feature<NoneFeatureConfiguration>(NoneFeatureConfiguration.CODEC) {

	override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration>): Boolean {
		val origin = context.origin()
		val level = context.level()

		val chunkX = floor(origin.x / 16.0).toInt()
		val chunkZ = floor(origin.z / 16.0).toInt()

		var placed = false

		for (regionX in chunkX.floorDiv(REGION_SIZE_IN_CHUNKS) - 1..chunkX.floorDiv(REGION_SIZE_IN_CHUNKS) + 1) {
			for (regionZ in chunkZ.floorDiv(REGION_SIZE_IN_CHUNKS) - 1..chunkZ.floorDiv(REGION_SIZE_IN_CHUNKS) + 1) {
				val pillar = getPillarCenter(level.seed, regionX, regionZ)

				if (DeepDarkAncientCityPlacement.isInReservedCityArea(level.seed, pillar.x, pillar.z)) continue
				if (!pillarCanTouchChunk(pillar, chunkX, chunkZ)) continue

				placed = placePillarChunk(pillar, chunkX, chunkZ, context) || placed
			}
		}

		return placed
	}

	private fun getPillarCenter(seed: Long, regionX: Int, regionZ: Int): BlockPos {
		val random = RandomSource.create(seed + regionX * 65535L + regionZ)
		val x = regionX * REGION_SIZE_IN_BLOCKS + MIN_CENTER_OFFSET + random.nextInt(RANDOM_CENTER_RANGE)
		val z = regionZ * REGION_SIZE_IN_BLOCKS + MIN_CENTER_OFFSET + random.nextInt(RANDOM_CENTER_RANGE)

		return BlockPos(x, 0, z)
	}

	private fun pillarCanTouchChunk(pillar: BlockPos, chunkX: Int, chunkZ: Int): Boolean {
		val minX = chunkX * 16
		val minZ = chunkZ * 16
		val maxX = minX + 15
		val maxZ = minZ + 15

		return pillar.x + MAX_RADIUS >= minX
				&& pillar.x - MAX_RADIUS <= maxX
				&& pillar.z + MAX_RADIUS >= minZ
				&& pillar.z - MAX_RADIUS <= maxZ
	}

	private fun placePillarChunk(pillar: BlockPos, chunkX: Int, chunkZ: Int, context: FeaturePlaceContext<NoneFeatureConfiguration>): Boolean {
		val level = context.level()
		val mutablePos = BlockPos.MutableBlockPos()
		var placed = false

		for (x in chunkX * 16..chunkX * 16 + 15) {
			for (z in chunkZ * 16..chunkZ * 16 + 15) {
				val distanceFromCenter = getDistanceFromCenter(pillar, x, z)
				if (distanceFromCenter > MAX_RADIUS + 1) continue

				for (y in DeepDarkConstants.FLOOR_TOP + 1 until DeepDarkConstants.CEILING_BOTTOM) {
					if (!shouldPlacePillarBlock(distanceFromCenter, x, y, z, level.seed)) continue

					mutablePos.set(x, y, z)
					if (level.isEmptyBlock(mutablePos) || level.getBlockState(mutablePos).canBeReplaced()) {
						level.setBlock(mutablePos, Blocks.COBBLED_DEEPSLATE.defaultBlockState(), Block.UPDATE_CLIENTS)
						placed = true
					}
				}
			}
		}

		return placed
	}

	private fun getDistanceFromCenter(pillar: BlockPos, x: Int, z: Int): Double {
		val dx = pillar.x - x
		val dz = pillar.z - z

		return sqrt((dx * dx + dz * dz).toDouble())
	}

	private fun shouldPlacePillarBlock(distanceFromCenter: Double, x: Int, y: Int, z: Int, seed: Long): Boolean {
		val radius = getRadiusAtY(y)

		return distanceFromCenter <= radius
				|| distanceFromCenter <= radius + 1 && randomFloat(seed, x, y, z) < FUZZY_EDGE_CHANCE
	}

	private fun randomFloat(seed: Long, x: Int, y: Int, z: Int): Float {
		return RandomSource.create(seed + x * 341873128712L + y * 132897987541L + z * 42317861L).nextFloat()
	}

	private fun getRadiusAtY(y: Int): Double {
		val distanceFromFloor = y - DeepDarkConstants.FLOOR_TOP
		val distanceFromCeiling = DeepDarkConstants.CEILING_BOTTOM - y
		val distanceFromEnd = min(distanceFromFloor, distanceFromCeiling).toDouble()

		if (distanceFromEnd >= BULGE_HEIGHT) return MIDDLE_RADIUS

		val endAmount = (BULGE_HEIGHT - distanceFromEnd) / BULGE_HEIGHT
		return MIDDLE_RADIUS + END_BULGE_RADIUS * endAmount
	}

	companion object {
		const val REGION_SIZE_IN_CHUNKS = 4
		const val REGION_SIZE_IN_BLOCKS = REGION_SIZE_IN_CHUNKS * 16

		const val MIN_CENTER_OFFSET = 8
		const val RANDOM_CENTER_RANGE = REGION_SIZE_IN_BLOCKS - MIN_CENTER_OFFSET * 2

		const val MIDDLE_RADIUS = 4.0
		const val END_BULGE_RADIUS = 4.0
		const val BULGE_HEIGHT = 12.0
		const val FUZZY_EDGE_CHANCE = 0.5f

		const val MAX_RADIUS = MIDDLE_RADIUS + END_BULGE_RADIUS
	}

}
