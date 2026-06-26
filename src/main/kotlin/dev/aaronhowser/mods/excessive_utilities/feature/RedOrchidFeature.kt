package dev.aaronhowser.mods.excessive_utilities.feature

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.shuffle
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration
import net.neoforged.neoforge.common.Tags

class RedOrchidFeature : Feature<NoneFeatureConfiguration>(NoneFeatureConfiguration.CODEC) {

	// I hate this lmao
	// But I'm pretty sure the alternative is mixing in to the ore placement thing
	// and THAT sounds like a horrible idea
	override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration>): Boolean {
		val origin = context.origin()
		val chunkPos = ChunkPos(origin)

		val level = context.level()

		val random = context.random()
		val candidates = mutableListOf<BlockPos>()

		val minY = maxOf(level.minBuildHeight, MIN_REDSTONE_ORE_Y)
		val maxY = minOf(level.maxBuildHeight - 2, MAX_REDSTONE_ORE_Y)

		val volume = BlockPos.betweenClosed(
			chunkPos.minBlockX, minY, chunkPos.minBlockZ,
			chunkPos.maxBlockX, maxY, chunkPos.maxBlockZ
		)

		for (pos in volume) {
			if (!level.getBlockState(pos).`is`(Tags.Blocks.ORES_REDSTONE)) continue

			val orchidPos = pos.above()
			if (!level.isEmptyBlock(orchidPos)) continue

			candidates += orchidPos.immutable()
		}

		if (candidates.isEmpty()) return false
		candidates.shuffle(random)

		var placed = false
		val redOrchid = ModBlocks.RED_ORCHID.get()
		val fullyGrownRedOrchid = redOrchid.defaultBlockState()
			.setValue(CropBlock.AGE, redOrchid.maxAge)

		for (orchidPos in candidates.take(MAX_ORCHIDS_PER_CHUNK)) {
			if (!fullyGrownRedOrchid.canSurvive(level, orchidPos)) continue

			level.setBlock(orchidPos, fullyGrownRedOrchid, Block.UPDATE_CLIENTS)
			placed = true
		}

		return placed
	}

	companion object {
		const val MAX_ORCHIDS_PER_CHUNK = 3
		const val MIN_REDSTONE_ORE_Y = -64
		const val MAX_REDSTONE_ORE_Y = 16
	}

}
