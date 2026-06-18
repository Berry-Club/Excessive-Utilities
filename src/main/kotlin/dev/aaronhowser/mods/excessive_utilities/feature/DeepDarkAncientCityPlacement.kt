package dev.aaronhowser.mods.excessive_utilities.feature

import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import kotlin.math.sqrt

object DeepDarkAncientCityPlacement {

	fun isInReservedCityArea(seed: Long, x: Int, z: Int): Boolean {
		val chunkX = x.floorDiv(16)
		val chunkZ = z.floorDiv(16)
		val regionX = chunkX.floorDiv(SPACING)
		val regionZ = chunkZ.floorDiv(SPACING)

		for (nearbyRegionX in regionX - 1..regionX + 1) {
			for (nearbyRegionZ in regionZ - 1..regionZ + 1) {
				val cityCenter = getCityCenter(seed, nearbyRegionX, nearbyRegionZ)
				val dx = cityCenter.x - x
				val dz = cityCenter.z - z
				val distance = sqrt((dx * dx + dz * dz).toDouble())

				if (distance <= RESERVED_RADIUS) return true
			}
		}

		return false
	}

	private fun getCityCenter(seed: Long, regionX: Int, regionZ: Int): BlockPos {
		val random = RandomSource.create(seed + regionX * REGION_SEED_X + regionZ * REGION_SEED_Z + SALT)
		val chunkX = regionX * SPACING + random.nextInt(SPACING - SEPARATION)
		val chunkZ = regionZ * SPACING + random.nextInt(SPACING - SEPARATION)

		return BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8)
	}

	const val SPACING = 96
	const val SEPARATION = 72
	const val SALT = 18023247L

	private const val REGION_SEED_X = 341873128712L
	private const val REGION_SEED_Z = 132897987541L
	private const val RESERVED_RADIUS = 144

}
