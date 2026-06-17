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
		val regionX = chunkX shr 2
		val regionZ = chunkZ shr 2
		val chunkMinX = chunkX shl 4
		val chunkMinZ = chunkZ shl 4
		val chunkMaxX = chunkMinX + 15
		val chunkMaxZ = chunkMinZ + 15

		var placed = false
		val mutablePos = BlockPos.MutableBlockPos()

		for (candidateRegionX in (regionX - 1)..(regionX + 1)) {
			for (candidateRegionZ in (regionZ - 1)..(regionZ + 1)) {
				val regionRandom = RandomSource.create(level.seed + candidateRegionX * 65535L + candidateRegionZ)
				val spireX = candidateRegionX * 64 + 8 + regionRandom.nextInt(48)
				val spireZ = candidateRegionZ * 64 + 8 + regionRandom.nextInt(48)

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

					if (distanceToShell < 9) {
						threshold -= sqrt(9.0 - distanceToShell)
					}

					if (threshold <= 4 || threshold <= 5 && shouldPlaceOuterBlock(x, y, z, spireX, spireZ)) {
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
		private const val PILLAR_RADIUS = 16
		private const val PILLAR_RADIUS_SQUARED = PILLAR_RADIUS * PILLAR_RADIUS
	}

}
