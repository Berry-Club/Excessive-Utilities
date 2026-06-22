package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BiomeDefaultFeatures
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeGenerationSettings
import net.minecraft.world.level.biome.BiomeSpecialEffects
import net.minecraft.world.level.biome.MobSpawnSettings
import net.minecraft.world.level.levelgen.GenerationStep

object ModBiomes {

	val THE_DEPTHS = rk("the_depths")

	fun bootstrap(context: BootstrapContext<Biome>) {

		val placedFeatures = context.lookup(Registries.PLACED_FEATURE)
		val configuredCarvers = context.lookup(Registries.CONFIGURED_CARVER)

		val biomeGenerationSettingsBuilder = BiomeGenerationSettings.Builder(
			placedFeatures,
			configuredCarvers
		)

		addDenseOverworldOres(biomeGenerationSettingsBuilder)
		addSurfaceLavaLakes(biomeGenerationSettingsBuilder)
		BiomeDefaultFeatures.addFossilDecoration(biomeGenerationSettingsBuilder)

		val biome = Biome.BiomeBuilder()
			.hasPrecipitation(false)
			.temperature(0.8f)
			.downfall(0f)
			.specialEffects(
				BiomeSpecialEffects.Builder()
					.fogColor(0x010101)
					.waterColor(4159204)
					.waterFogColor(329011)
					.skyColor(0)
					.build()
			)
			.mobSpawnSettings(
				MobSpawnSettings.Builder().build()
			)
			.generationSettings(
				biomeGenerationSettingsBuilder.build()
			)
			.build()

		context.register(THE_DEPTHS, biome)
	}

	private fun addDenseOverworldOres(builder: BiomeGenerationSettings.Builder) {
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_COAL_UPPER)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_COAL_LOWER)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_IRON_UPPER)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_IRON_UPPER_EXTRA)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_IRON_MIDDLE)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_IRON_SMALL)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_GOLD)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_GOLD_LOWER)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_GOLD_EXTRA)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_REDSTONE)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_REDSTONE_LOWER)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_DIAMOND)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_DIAMOND_MEDIUM)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_DIAMOND_LARGE)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_DIAMOND_BURIED)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_LAPIS)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_LAPIS_BURIED)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_COPPER)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_COPPER_LARGE)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_EMERALD)
		builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ModPlacedFeatures.DEPTHS_ORE_EMERALD_EXTRA)
	}

	private fun addSurfaceLavaLakes(builder: BiomeGenerationSettings.Builder) {
		builder.addFeature(GenerationStep.Decoration.LAKES, ModPlacedFeatures.DEPTHS_LAKE_LAVA_SURFACE)
	}

	private fun rk(path: String): ResourceKey<Biome> {
		return ResourceKey.create(Registries.BIOME, ExcessiveUtilities.modResource(path))
	}

}