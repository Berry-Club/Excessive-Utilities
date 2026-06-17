package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.*

object ModPlacedFeatures {

	val DEEP_DARK_PILLAR = rk("deep_dark_pillar")
	val DEEP_DARK_STALACTITE = rk("deep_dark_stalactite")

	fun bootstrap(context: BootstrapContext<PlacedFeature>) {

		val configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE)

		context.register(
			DEEP_DARK_PILLAR,
			PlacedFeature(
				configuredFeatures.getOrThrow(ModConfiguredFeatures.DEEP_DARK_PILLAR),
				listOf(
					BiomeFilter.biome(),
					CountPlacement.of(1),
					HeightRangePlacement.uniform(
						VerticalAnchor.absolute(DeepDarkConstants.FLOOR_TOP + 1),
						VerticalAnchor.absolute(DeepDarkConstants.FLOOR_TOP + 1),
					)
				)
			)
		)

		context.register(
			DEEP_DARK_STALACTITE,
			PlacedFeature(
				configuredFeatures.getOrThrow(ModConfiguredFeatures.DEEP_DARK_STALACTITE),
				listOf(
					BiomeFilter.biome(),
					CountPlacement.of(1),
					HeightRangePlacement.uniform(
						VerticalAnchor.absolute(DeepDarkConstants.CEILING_BOTTOM),
						VerticalAnchor.absolute(DeepDarkConstants.CEILING_BOTTOM),
					)
				)
			)
		)

	}

	fun rk(name: String): ResourceKey<PlacedFeature> {
		return ResourceKey.create(Registries.PLACED_FEATURE, ExcessiveUtilities.modResource(name))
	}

}
