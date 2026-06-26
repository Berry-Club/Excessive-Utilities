package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModFeatures
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration

object ModConfiguredFeatures {

	val DEPTHS_PILLAR = rk("depths_pillar")
	val DEPTHS_STALACTITE = rk("depths_stalactite")
	val DEPTHS_SCULK_VEIN = rk("depths_sculk_vein")
	val RED_ORCHID = rk("red_orchid")

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

	}

	fun rk(name: String): ResourceKey<ConfiguredFeature<*, *>> {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, ExcessiveUtilities.modResource(name))
	}

}
