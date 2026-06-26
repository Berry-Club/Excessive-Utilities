package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.defaultBlockState
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.EnderLilyBlock
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModFeatures
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.CropBlock
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider

object ModConfiguredFeatures {

	val DEPTHS_PILLAR = rk("depths_pillar")
	val DEPTHS_STALACTITE = rk("depths_stalactite")
	val DEPTHS_SCULK_VEIN = rk("depths_sculk_vein")
	val RED_ORCHID = rk("red_orchid")
	val ENDER_LILY_PATCH = rk("ender_lily_patch")

	fun bootstrap(context: BootstrapContext<ConfiguredFeature<*, *>>) {

		context.register(
			DEPTHS_PILLAR,
			ConfiguredFeature(
				ModFeatures.DEPTHS_PILLAR.get(),
				FeatureConfiguration.NONE
			)
		)

		context.register(
			DEPTHS_STALACTITE,
			ConfiguredFeature(
				ModFeatures.DEPTHS_STALACTITE.get(),
				FeatureConfiguration.NONE
			)
		)

		context.register(
			DEPTHS_SCULK_VEIN,
			ConfiguredFeature(
				ModFeatures.DEPTHS_SCULK_VEIN.get(),
				FeatureConfiguration.NONE
			)
		)

		context.register(
			RED_ORCHID,
			ConfiguredFeature(
				ModFeatures.RED_ORCHID.get(),
				FeatureConfiguration.NONE
			)
		)

		context.register(
			ENDER_LILY_PATCH,
			ConfiguredFeature(
				Feature.RANDOM_PATCH,
				RandomPatchConfiguration(
					6,
					4,
					2,
					PlacementUtils.onlyWhenEmpty(
						Feature.SIMPLE_BLOCK,
						SimpleBlockConfiguration(
							BlockStateProvider.simple(
								ModBlocks.ENDER_LILY.defaultBlockState()
									.setValue(CropBlock.AGE, CropBlock.MAX_AGE)
							)
						)
					)
				)
			)
		)

	}

	fun rk(name: String): ResourceKey<ConfiguredFeature<*, *>> {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, ExcessiveUtilities.modResource(name))
	}

}
