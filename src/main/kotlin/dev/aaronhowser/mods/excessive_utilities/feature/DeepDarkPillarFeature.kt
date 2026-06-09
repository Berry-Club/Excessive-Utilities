package dev.aaronhowser.mods.excessive_utilities.feature

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration

class DeepDarkPillarFeature : Feature<NoneFeatureConfiguration>(NoneFeatureConfiguration.CODEC) {

	override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration>): Boolean {
		val origin = context.origin()
		val level = context.level()

		val volume = BlockPos.betweenClosed(
			origin.offset(-5, -100, -5),
			origin.offset(5, 100, 5),
		)

		for (pos in volume) {
			level.setBlock(pos, Blocks.STONE.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE)
		}

		return true
	}

}