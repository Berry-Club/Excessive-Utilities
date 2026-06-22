package dev.aaronhowser.mods.excessive_utilities.feature

import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.DepthsDimConstants
import net.minecraft.core.BlockPos
import net.minecraft.util.RandomSource
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration

class DepthsStalactiteFeature : Feature<NoneFeatureConfiguration>(NoneFeatureConfiguration.CODEC) {

	override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration>): Boolean {
		val origin = context.origin()
		val level = context.level()
		val mutablePos = BlockPos.MutableBlockPos()
		var placed = false

		val chunkMinX = origin.x.floorDiv(16) * 16
		val chunkMinZ = origin.z.floorDiv(16) * 16

		for (x in chunkMinX until chunkMinX + 16) {
			for (z in chunkMinZ until chunkMinZ + 16) {
				val random = RandomSource.create(level.seed + x * 341873128712L + z * 132897987541L)
				if (random.nextFloat() > STALACTITE_CHANCE) continue

				val ceilingY = findCeilingY(context, mutablePos, x, z) ?: continue
				val length = getStalactiteLength(random)
				val minY = maxOf(DepthsDimConstants.FLOOR_TOP + MIN_CLEARANCE_ABOVE_FLOOR, ceilingY - length)

				for (y in ceilingY - 1 downTo minY) {
					mutablePos.set(x, y, z)

					if (!level.isEmptyBlock(mutablePos) && !level.getBlockState(mutablePos).canBeReplaced()) break

					level.setBlock(mutablePos, Blocks.COBBLED_DEEPSLATE.defaultBlockState(), Block.UPDATE_CLIENTS)
					placed = true
				}
			}
		}

		return placed
	}

	private fun findCeilingY(context: FeaturePlaceContext<NoneFeatureConfiguration>, mutablePos: BlockPos.MutableBlockPos, x: Int, z: Int): Int? {
		val level = context.level()

		for (y in DepthsDimConstants.CEILING_BOTTOM + CEILING_SCAN_ABOVE downTo DepthsDimConstants.FLOOR_TOP + 1) {
			mutablePos.set(x, y, z)

			if (level.isEmptyBlock(mutablePos) || level.getBlockState(mutablePos).canBeReplaced()) continue

			mutablePos.set(x, y - 1, z)
			if (level.isEmptyBlock(mutablePos) || level.getBlockState(mutablePos).canBeReplaced()) return y
		}

		return null
	}

	private fun getStalactiteLength(random: RandomSource): Int {
		val baseLength = MIN_LENGTH + random.nextInt(BASE_LENGTH_RANGE)
		if (random.nextFloat() >= LONG_STALACTITE_CHANCE) return baseLength

		return baseLength + random.nextInt(LONG_EXTRA_LENGTH_RANGE)
	}

	companion object {
		const val STALACTITE_CHANCE = 0.3f
		const val LONG_STALACTITE_CHANCE = 0.05f

		const val MIN_LENGTH = 4
		const val BASE_LENGTH_RANGE = 7
		const val LONG_EXTRA_LENGTH_RANGE = 12

		const val MIN_CLEARANCE_ABOVE_FLOOR = 10
		const val CEILING_SCAN_ABOVE = 24
	}

}
