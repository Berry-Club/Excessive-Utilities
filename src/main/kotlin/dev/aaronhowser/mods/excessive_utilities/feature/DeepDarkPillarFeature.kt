package dev.aaronhowser.mods.excessive_utilities.feature

import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.DeepDarkConstants
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.ModDimensionTypes
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen.ModNoiseSettings
import net.minecraft.core.Direction
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration

class DeepDarkPillarFeature : Feature<NoneFeatureConfiguration>(NoneFeatureConfiguration.CODEC) {

	override fun place(context: FeaturePlaceContext<NoneFeatureConfiguration>): Boolean {
		val origin = context.origin()
		val level = context.level()

		val pos = origin.mutable()

		while (pos.y < DeepDarkConstants.FLOOR_TOP) {
			level.setBlock(pos, Blocks.DIAMOND_BLOCK.defaultBlockState(), Block.UPDATE_CLIENTS)
			pos.move(Direction.UP)
		}

		return true
	}

}