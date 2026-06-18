package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.features.MiscOverworldFeatures
import net.minecraft.data.worldgen.features.OreFeatures
import net.minecraft.data.worldgen.placement.PlacementUtils
import net.minecraft.resources.ResourceKey
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.*

object ModPlacedFeatures {

	val DEEP_DARK_PILLAR = rk("deep_dark_pillar")
	val DEEP_DARK_STALACTITE = rk("deep_dark_stalactite")
	val DEEP_DARK_SCULK_VEIN = rk("deep_dark_sculk_vein")

	val DEEP_DARK_LAKE_LAVA_SURFACE = rk("deep_dark_lake_lava_surface")

	val DEEP_DARK_ORE_COAL_UPPER = rk("deep_dark_ore_coal_upper")
	val DEEP_DARK_ORE_COAL_LOWER = rk("deep_dark_ore_coal_lower")
	val DEEP_DARK_ORE_IRON_UPPER = rk("deep_dark_ore_iron_upper")
	val DEEP_DARK_ORE_IRON_UPPER_EXTRA = rk("deep_dark_ore_iron_upper_extra")
	val DEEP_DARK_ORE_IRON_MIDDLE = rk("deep_dark_ore_iron_middle")
	val DEEP_DARK_ORE_IRON_SMALL = rk("deep_dark_ore_iron_small")
	val DEEP_DARK_ORE_GOLD = rk("deep_dark_ore_gold")
	val DEEP_DARK_ORE_GOLD_LOWER = rk("deep_dark_ore_gold_lower")
	val DEEP_DARK_ORE_GOLD_EXTRA = rk("deep_dark_ore_gold_extra")
	val DEEP_DARK_ORE_REDSTONE = rk("deep_dark_ore_redstone")
	val DEEP_DARK_ORE_REDSTONE_LOWER = rk("deep_dark_ore_redstone_lower")
	val DEEP_DARK_ORE_DIAMOND = rk("deep_dark_ore_diamond")
	val DEEP_DARK_ORE_DIAMOND_MEDIUM = rk("deep_dark_ore_diamond_medium")
	val DEEP_DARK_ORE_DIAMOND_LARGE = rk("deep_dark_ore_diamond_large")
	val DEEP_DARK_ORE_DIAMOND_BURIED = rk("deep_dark_ore_diamond_buried")
	val DEEP_DARK_ORE_LAPIS = rk("deep_dark_ore_lapis")
	val DEEP_DARK_ORE_LAPIS_BURIED = rk("deep_dark_ore_lapis_buried")
	val DEEP_DARK_ORE_COPPER = rk("deep_dark_ore_copper")
	val DEEP_DARK_ORE_COPPER_LARGE = rk("deep_dark_ore_copper_large")
	val DEEP_DARK_ORE_EMERALD = rk("deep_dark_ore_emerald")
	val DEEP_DARK_ORE_EMERALD_EXTRA = rk("deep_dark_ore_emerald_extra")

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

		context.register(
			DEEP_DARK_SCULK_VEIN,
			PlacedFeature(
				configuredFeatures.getOrThrow(ModConfiguredFeatures.DEEP_DARK_SCULK_VEIN),
				listOf(
					BiomeFilter.biome(),
					CountPlacement.of(1),
					HeightRangePlacement.uniform(
						VerticalAnchor.absolute(DeepDarkConstants.FLOOR_TOP),
						VerticalAnchor.absolute(DeepDarkConstants.FLOOR_TOP),
					)
				)
			)
		)

		context.register(
			DEEP_DARK_LAKE_LAVA_SURFACE,
			PlacedFeature(
				configuredFeatures.getOrThrow(MiscOverworldFeatures.LAKE_LAVA),
				listOf(
					RarityFilter.onAverageOnceEvery(67),
					InSquarePlacement.spread(),
					PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
					BiomeFilter.biome()
				)
			)
		)

		context.register(DEEP_DARK_ORE_COAL_UPPER, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_COAL), commonOrePlacement(90, HeightRangePlacement.uniform(VerticalAnchor.absolute(136), VerticalAnchor.top()))))
		context.register(DEEP_DARK_ORE_COAL_LOWER, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_COAL_BURIED), commonOrePlacement(60, HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(192)))))
		context.register(DEEP_DARK_ORE_IRON_UPPER, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_IRON), commonOrePlacement(256, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384)))))
		context.register(DEEP_DARK_ORE_IRON_UPPER_EXTRA, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_IRON), commonOrePlacement(14, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384)))))
		context.register(DEEP_DARK_ORE_IRON_MIDDLE, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_IRON), commonOrePlacement(30, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56)))))
		context.register(DEEP_DARK_ORE_IRON_SMALL, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_IRON_SMALL), commonOrePlacement(30, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72)))))
		context.register(DEEP_DARK_ORE_GOLD_EXTRA, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_GOLD), commonOrePlacement(150, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256)))))
		context.register(DEEP_DARK_ORE_GOLD, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_GOLD_BURIED), commonOrePlacement(12, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32)))))
		context.register(
			DEEP_DARK_ORE_GOLD_LOWER,
			PlacedFeature(
				configuredFeatures.getOrThrow(OreFeatures.ORE_GOLD_BURIED),
				orePlacement(
					CountPlacement.of(UniformInt.of(0, 3)),
					HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48))
				)
			)
		)
		context.register(DEEP_DARK_ORE_REDSTONE, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_REDSTONE), commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15)))))
		context.register(DEEP_DARK_ORE_REDSTONE_LOWER, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_REDSTONE), commonOrePlacement(24, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-32), VerticalAnchor.aboveBottom(32)))))
		context.register(DEEP_DARK_ORE_DIAMOND, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_DIAMOND_SMALL), commonOrePlacement(21, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))))
		context.register(DEEP_DARK_ORE_DIAMOND_MEDIUM, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_DIAMOND_MEDIUM), commonOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-4)))))
		context.register(DEEP_DARK_ORE_DIAMOND_LARGE, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_DIAMOND_LARGE), rareOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))))
		context.register(DEEP_DARK_ORE_DIAMOND_BURIED, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_DIAMOND_BURIED), commonOrePlacement(12, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))))
		context.register(DEEP_DARK_ORE_LAPIS, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_LAPIS), commonOrePlacement(6, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(32)))))
		context.register(DEEP_DARK_ORE_LAPIS_BURIED, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_LAPIS_BURIED), commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)))))
		context.register(DEEP_DARK_ORE_EMERALD, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_EMERALD), commonOrePlacement(256, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(480)))))
		context.register(DEEP_DARK_ORE_EMERALD_EXTRA, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_EMERALD), commonOrePlacement(44, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(480)))))
		context.register(DEEP_DARK_ORE_COPPER, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_COPPPER_SMALL), commonOrePlacement(24, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112)))))
		context.register(DEEP_DARK_ORE_COPPER_LARGE, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_COPPER_LARGE), commonOrePlacement(24, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112)))))
	}

	private fun orePlacement(countPlacement: PlacementModifier, heightRange: PlacementModifier): List<PlacementModifier> {
		return listOf(countPlacement, InSquarePlacement.spread(), heightRange, BiomeFilter.biome())
	}

	private fun commonOrePlacement(count: Int, heightRange: PlacementModifier): List<PlacementModifier> {
		return orePlacement(CountPlacement.of(count), heightRange)
	}

	private fun rareOrePlacement(chance: Int, heightRange: PlacementModifier): List<PlacementModifier> {
		return orePlacement(RarityFilter.onAverageOnceEvery(chance), heightRange)
	}

	fun rk(name: String): ResourceKey<PlacedFeature> {
		return ResourceKey.create(Registries.PLACED_FEATURE, ExcessiveUtilities.modResource(name))
	}

}
