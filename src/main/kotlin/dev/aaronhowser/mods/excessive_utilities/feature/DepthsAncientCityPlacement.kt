package dev.aaronhowser.mods.excessive_utilities.feature

import net.minecraft.core.BlockPos
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.levelgen.LegacyRandomSource
import net.minecraft.world.level.levelgen.WorldgenRandom
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType
import kotlin.math.sqrt

object DepthsAncientCityPlacement {

	fun isInReservedCityArea(seed: Long, x: Int, z: Int): Boolean {
		val chunkX = x.floorDiv(16)
		val chunkZ = z.floorDiv(16)
		val regionX = chunkX.floorDiv(SPACING)
		val regionZ = chunkZ.floorDiv(SPACING)

		for (nearbyRegionX in regionX - 1..regionX + 1) {
			for (nearbyRegionZ in regionZ - 1..regionZ + 1) {
				val cityCenter = getCityCenterForSpreadRegion(seed, nearbyRegionX, nearbyRegionZ)
				val dx = cityCenter.x - x
				val dz = cityCenter.z - z
				val distance = sqrt((dx * dx + dz * dz).toDouble())

				if (distance <= RESERVED_RADIUS) return true
			}
		}

		return false
	}

	private fun getCityCenterForSpreadRegion(seed: Long, spreadRegionX: Int, spreadRegionZ: Int): BlockPos {
		val chunkPos = getPotentialStructureChunk(seed, spreadRegionX, spreadRegionZ)

		return BlockPos(chunkPos.x * 16 + 8, 0, chunkPos.z * 16 + 8)
	}

	private fun getPotentialStructureChunk(seed: Long, spreadRegionX: Int, spreadRegionZ: Int): ChunkPos {
		val random = WorldgenRandom(LegacyRandomSource(0L))
		random.setLargeFeatureWithSalt(seed, spreadRegionX, spreadRegionZ, SALT)
		val randomRange = SPACING - SEPARATION
		val chunkX = spreadRegionX * SPACING + RandomSpreadType.LINEAR.evaluate(random, randomRange)
		val chunkZ = spreadRegionZ * SPACING + RandomSpreadType.LINEAR.evaluate(random, randomRange)

		return ChunkPos(chunkX, chunkZ)
	}

	const val SPACING = 96
	const val SEPARATION = 72
	const val SALT = 18023247

	private const val RESERVED_RADIUS = 144

}
