package dev.aaronhowser.mods.excessive_utilities.feature

import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.DeepDarkConstants
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.WorldGenLevel
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
		val regionX = Math.floorDiv(chunkX, REGION_CHUNKS)
		val regionZ = Math.floorDiv(chunkZ, REGION_CHUNKS)
		val chunkMinX = chunkX shl 4
		val chunkMinZ = chunkZ shl 4
		val chunkMaxX = chunkMinX + 15
		val chunkMaxZ = chunkMinZ + 15

		var placed = false
		val mutablePos = BlockPos.MutableBlockPos()

		for (candidateRegionX in (regionX - 1)..(regionX + 1)) {
			for (candidateRegionZ in (regionZ - 1)..(regionZ + 1)) {
				val regionRandom = RandomSource.create(level.seed + candidateRegionX * 65535L + candidateRegionZ)
				val spireX = candidateRegionX * REGION_SIZE + REGION_MARGIN + regionRandom.nextInt(REGION_SIZE - REGION_MARGIN * 2)
				val spireZ = candidateRegionZ * REGION_SIZE + REGION_MARGIN + regionRandom.nextInt(REGION_SIZE - REGION_MARGIN * 2)

				if (spireX + PILLAR_RADIUS < chunkMinX || spireX - PILLAR_RADIUS > chunkMaxX) continue
				if (spireZ + PILLAR_RADIUS < chunkMinZ || spireZ - PILLAR_RADIUS > chunkMaxZ) continue

				if (placeSpire(level, mutablePos, chunkMinX, chunkMaxX, chunkMinZ, chunkMaxZ, spireX, spireZ)) {
					placed = true
				}
			}
		}

		return placed
	}

	private fun placeSpire(
		level: WorldGenLevel,
		mutablePos: BlockPos.MutableBlockPos,
		chunkMinX: Int,
		chunkMaxX: Int,
		chunkMinZ: Int,
		chunkMaxZ: Int,
		spireX: Int,
		spireZ: Int
	): Boolean {
		var placed = false

		for (x in chunkMinX..chunkMaxX) {
			for (z in chunkMinZ..chunkMaxZ) {
				val dx = spireX - x
				val dz = spireZ - z
				val distanceSquared = dx * dx + dz * dz
				if (distanceSquared >= PILLAR_RADIUS_SQUARED) continue

				val spireDistance = sqrt(distanceSquared.toDouble())

				for (y in (DeepDarkConstants.FLOOR_TOP + 1) until DeepDarkConstants.CEILING_BOTTOM) {
					val distanceToShell = min(y - DeepDarkConstants.FLOOR_TOP, DeepDarkConstants.CEILING_BOTTOM - y)
					var threshold = spireDistance

					if (distanceToShell < FLARE_HEIGHT) {
						threshold -= sqrt((FLARE_HEIGHT - distanceToShell).toDouble()) * 1.8
					}

					if (threshold <= CORE_RADIUS || threshold <= OUTER_RADIUS && shouldPlaceOuterBlock(x, y, z, spireX, spireZ)) {
						mutablePos.set(x, y, z)
						if (level.isEmptyBlock(mutablePos) || level.getBlockState(mutablePos).canBeReplaced()) {
							level.setBlock(mutablePos, Blocks.COBBLESTONE.defaultBlockState(), Block.UPDATE_CLIENTS)
							placed = true
						}
					}
				}
			}
		}

		return placed
	}

	private fun shouldPlaceOuterBlock(x: Int, y: Int, z: Int, spireX: Int, spireZ: Int): Boolean {
		var hash = x * 341873128712L
		hash = hash xor (y * 132897987541L)
		hash = hash xor (z * 42317861L)
		hash = hash xor (spireX * 65535L)
		hash = hash xor spireZ.toLong()
		hash = hash xor (hash ushr 33)
		hash *= -49064778989728563L
		hash = hash xor (hash ushr 33)
		return hash and 1L == 0L
	}

	companion object {
		private const val REGION_CHUNKS = 7
		private const val REGION_SIZE = REGION_CHUNKS * 16
		private const val REGION_MARGIN = 32
		private const val PILLAR_RADIUS = 18
		private const val PILLAR_RADIUS_SQUARED = PILLAR_RADIUS * PILLAR_RADIUS
		private const val CORE_RADIUS = 10
		private const val OUTER_RADIUS = 17
		private const val FLARE_HEIGHT = 18
	}

}
