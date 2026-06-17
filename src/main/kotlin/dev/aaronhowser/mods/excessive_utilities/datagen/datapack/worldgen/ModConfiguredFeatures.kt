package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModFeatures
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration

object ModConfiguredFeatures {

	val DEEP_DARK_PILLAR = rk("deep_dark_pillar")
	val DEEP_DARK_STALACTITE = rk("deep_dark_stalactite")

	fun bootstrap(context: BootstrapContext<ConfiguredFeature<*, *>>) {

		context.register(
			DEEP_DARK_PILLAR,
			ConfiguredFeature(
				ModFeatures.DEEP_DARK_PILLAR.get(),
				FeatureConfiguration.NONE
			)
		)

		context.register(
			DEEP_DARK_STALACTITE,
			ConfiguredFeature(
				ModFeatures.DEEP_DARK_STALACTITE.get(),
				FeatureConfiguration.NONE
			)
		)

	}

	fun rk(name: String): ResourceKey<ConfiguredFeature<*, *>> {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, ExcessiveUtilities.modResource(name))
	}

}
