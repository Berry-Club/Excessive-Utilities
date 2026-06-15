package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.*

object ModPlacedFeatures {

	val DEEP_DARK_PILLAR = rk("deep_dark_pillar")

	fun bootstrap(context: BootstrapContext<PlacedFeature>) {

		val configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE)

		context.register(
			DEEP_DARK_PILLAR,
			PlacedFeature(
				configuredFeatures.getOrThrow(ModConfiguredFeatures.DEEP_DARK_PILLAR),
				listOf(
					BiomeFilter.biome(),
					InSquarePlacement.spread(),
					HeightRangePlacement.uniform(
						VerticalAnchor.absolute(71),
						VerticalAnchor.absolute(71),
					),
					RarityFilter.onAverageOnceEvery(100)
				)
			)
		)

	}

	fun rk(name: String): ResourceKey<PlacedFeature> {
		return ResourceKey.create(Registries.PLACED_FEATURE, ExcessiveUtilities.modResource(name))
	}

}