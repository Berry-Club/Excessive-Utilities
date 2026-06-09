package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Rarity
import net.minecraft.world.level.levelgen.placement.BiomeFilter
import net.minecraft.world.level.levelgen.placement.InSquarePlacement
import net.minecraft.world.level.levelgen.placement.PlacedFeature
import net.minecraft.world.level.levelgen.placement.RarityFilter

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
					RarityFilter.onAverageOnceEvery(100)
				)
			)
		)

	}

	fun rk(name: String): ResourceKey<PlacedFeature> {
		return ResourceKey.create(Registries.PLACED_FEATURE, ExcessiveUtilities.modResource(name))
	}

}