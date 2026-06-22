package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.features.MiscOverworldFeatures
import net.minecraft.data.worldgen.features.OreFeatures
import net.minecraft.resources.ResourceKey
import net.minecraft.util.valueproviders.UniformInt
import net.minecraft.world.level.levelgen.VerticalAnchor
import net.minecraft.world.level.levelgen.placement.*

object ModPlacedFeatures {

	val DEPTHS_PILLAR = rk("depths_pillar")
	val DEPTHS_STALACTITE = rk("depths_stalactite")
	val DEPTHS_SCULK_VEIN = rk("depths_sculk_vein")

	val DEPTHS_LAKE_LAVA_SURFACE = rk("depths_lake_lava_surface")

	val DEPTHS_ORE_COAL_UPPER = rk("depths_ore_coal_upper")
	val DEPTHS_ORE_COAL_LOWER = rk("depths_ore_coal_lower")
	val DEPTHS_ORE_IRON_UPPER = rk("depths_ore_iron_upper")
	val DEPTHS_ORE_IRON_UPPER_EXTRA = rk("depths_ore_iron_upper_extra")
	val DEPTHS_ORE_IRON_MIDDLE = rk("depths_ore_iron_middle")
	val DEPTHS_ORE_IRON_SMALL = rk("depths_ore_iron_small")
	val DEPTHS_ORE_GOLD = rk("depths_ore_gold")
	val DEPTHS_ORE_GOLD_LOWER = rk("depths_ore_gold_lower")
	val DEPTHS_ORE_GOLD_EXTRA = rk("depths_ore_gold_extra")
	val DEPTHS_ORE_REDSTONE = rk("depths_ore_redstone")
	val DEPTHS_ORE_REDSTONE_LOWER = rk("depths_ore_redstone_lower")
	val DEPTHS_ORE_DIAMOND = rk("depths_ore_diamond")
	val DEPTHS_ORE_DIAMOND_MEDIUM = rk("depths_ore_diamond_medium")
	val DEPTHS_ORE_DIAMOND_LARGE = rk("depths_ore_diamond_large")
	val DEPTHS_ORE_DIAMOND_BURIED = rk("depths_ore_diamond_buried")
	val DEPTHS_ORE_LAPIS = rk("depths_ore_lapis")
	val DEPTHS_ORE_LAPIS_BURIED = rk("depths_ore_lapis_buried")
	val DEPTHS_ORE_COPPER = rk("depths_ore_copper")
	val DEPTHS_ORE_COPPER_LARGE = rk("depths_ore_copper_large")
	val DEPTHS_ORE_EMERALD = rk("depths_ore_emerald")
	val DEPTHS_ORE_EMERALD_EXTRA = rk("depths_ore_emerald_extra")

	fun bootstrap(context: BootstrapContext<PlacedFeature>) {

		val configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE)

		context.register(
			DEPTHS_PILLAR,
			PlacedFeature(
				configuredFeatures.getOrThrow(ModConfiguredFeatures.DEPTHS_PILLAR),
				listOf(
					BiomeFilter.biome(),
					CountPlacement.of(1),
					HeightRangePlacement.uniform(
						VerticalAnchor.absolute(DepthsDimConstants.FLOOR_TOP + 1),
						VerticalAnchor.absolute(DepthsDimConstants.FLOOR_TOP + 1),
					)
				)
			)
		)

		context.register(
			DEPTHS_STALACTITE,
			PlacedFeature(
				configuredFeatures.getOrThrow(ModConfiguredFeatures.DEPTHS_STALACTITE),
				listOf(
					BiomeFilter.biome(),
					CountPlacement.of(1),
					HeightRangePlacement.uniform(
						VerticalAnchor.absolute(DepthsDimConstants.CEILING_BOTTOM),
						VerticalAnchor.absolute(DepthsDimConstants.CEILING_BOTTOM),
					)
				)
			)
		)

		context.register(
			DEPTHS_SCULK_VEIN,
			PlacedFeature(
				configuredFeatures.getOrThrow(ModConfiguredFeatures.DEPTHS_SCULK_VEIN),
				listOf(
					BiomeFilter.biome(),
					CountPlacement.of(1),
					HeightRangePlacement.uniform(
						VerticalAnchor.absolute(DepthsDimConstants.FLOOR_TOP),
						VerticalAnchor.absolute(DepthsDimConstants.FLOOR_TOP),
					)
				)
			)
		)

		context.register(
			DEPTHS_LAKE_LAVA_SURFACE,
			PlacedFeature(
				configuredFeatures.getOrThrow(MiscOverworldFeatures.LAKE_LAVA),
				listOf(
					RarityFilter.onAverageOnceEvery(67),
					InSquarePlacement.spread(),
					HeightRangePlacement.uniform(
						VerticalAnchor.absolute(DepthsDimConstants.FLOOR_TOP),
						VerticalAnchor.absolute(DepthsDimConstants.FLOOR_TOP + DepthsDimConstants.BLEND_THICKNESS),
					),
					BiomeFilter.biome()
				)
			)
		)

		context.register(DEPTHS_ORE_COAL_UPPER, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_COAL), commonOrePlacement(90, HeightRangePlacement.uniform(VerticalAnchor.absolute(136), VerticalAnchor.top()))))
		context.register(DEPTHS_ORE_COAL_LOWER, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_COAL_BURIED), commonOrePlacement(60, HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(192)))))
		context.register(DEPTHS_ORE_IRON_UPPER, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_IRON), commonOrePlacement(256, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384)))))
		context.register(DEPTHS_ORE_IRON_UPPER_EXTRA, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_IRON), commonOrePlacement(14, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384)))))
		context.register(DEPTHS_ORE_IRON_MIDDLE, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_IRON), commonOrePlacement(30, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56)))))
		context.register(DEPTHS_ORE_IRON_SMALL, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_IRON_SMALL), commonOrePlacement(30, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72)))))
		context.register(DEPTHS_ORE_GOLD_EXTRA, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_GOLD), commonOrePlacement(150, HeightRangePlacement.uniform(VerticalAnchor.absolute(32), VerticalAnchor.absolute(256)))))
		context.register(DEPTHS_ORE_GOLD, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_GOLD_BURIED), commonOrePlacement(12, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32)))))
		context.register(
			DEPTHS_ORE_GOLD_LOWER,
			PlacedFeature(
				configuredFeatures.getOrThrow(OreFeatures.ORE_GOLD_BURIED),
				orePlacement(
					CountPlacement.of(UniformInt.of(0, 3)),
					HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48))
				)
			)
		)
		context.register(DEPTHS_ORE_REDSTONE, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_REDSTONE), commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15)))))
		context.register(DEPTHS_ORE_REDSTONE_LOWER, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_REDSTONE), commonOrePlacement(24, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-32), VerticalAnchor.aboveBottom(32)))))
		context.register(DEPTHS_ORE_DIAMOND, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_DIAMOND_SMALL), commonOrePlacement(21, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))))
		context.register(DEPTHS_ORE_DIAMOND_MEDIUM, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_DIAMOND_MEDIUM), commonOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-4)))))
		context.register(DEPTHS_ORE_DIAMOND_LARGE, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_DIAMOND_LARGE), rareOrePlacement(3, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))))
		context.register(DEPTHS_ORE_DIAMOND_BURIED, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_DIAMOND_BURIED), commonOrePlacement(12, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))))
		context.register(DEPTHS_ORE_LAPIS, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_LAPIS), commonOrePlacement(6, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(32)))))
		context.register(DEPTHS_ORE_LAPIS_BURIED, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_LAPIS_BURIED), commonOrePlacement(12, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)))))
		context.register(DEPTHS_ORE_EMERALD, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_EMERALD), commonOrePlacement(256, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(480)))))
		context.register(DEPTHS_ORE_EMERALD_EXTRA, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_EMERALD), commonOrePlacement(44, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(480)))))
		context.register(DEPTHS_ORE_COPPER, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_COPPPER_SMALL), commonOrePlacement(24, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112)))))
		context.register(DEPTHS_ORE_COPPER_LARGE, PlacedFeature(configuredFeatures.getOrThrow(OreFeatures.ORE_COPPER_LARGE), commonOrePlacement(24, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112)))))
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
