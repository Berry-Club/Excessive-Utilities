package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.worldgen.quantum_quarry.QuantumQuarryBiomeSource
import dev.aaronhowser.mods.excessive_utilities.worldgen.quantum_quarry.QuantumQuarryDimension
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.FixedBiomeSource
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.FlatLevelSource
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings
import java.util.*

object ModLevelStems {

	val THE_LAST_MILLENNIUM =
		rk("the_last_millennium")
	val THE_DEPTHS =
		rk(DepthsDimConstants.NAME)

	fun bootstrap(context: BootstrapContext<LevelStem>) {
		val dimensionTypeLookup =
			context.lookup(Registries.DIMENSION_TYPE)

		val noiseSettingsLookup =
			context.lookup(Registries.NOISE_SETTINGS)

		val biomeLookup = context.lookup(Registries.BIOME)

		for (dimension in QuantumQuarryDimension.entries) {
			val biomeSource = QuantumQuarryBiomeSource(
				dimension,
				biomeLookup.getOrThrow(dimension.biomeTag),
				biomeLookup.getOrThrow(dimension.fallbackBiome)
			)

			context.register(
				dimension.levelStemKey,
				LevelStem(
					dimensionTypeLookup.getOrThrow(dimension.dimensionType),
					NoiseBasedChunkGenerator(
						biomeSource,
						noiseSettingsLookup.getOrThrow(dimension.noiseSettings)
					)
				)
			)
		}

		val tlmSettings = FlatLevelGeneratorSettings(
			Optional.of(HolderSet.direct()),
			biomeLookup.getOrThrow(Biomes.PLAINS),
			emptyList()
		)

		context.register(
			THE_LAST_MILLENNIUM,
			LevelStem(
				dimensionTypeLookup.getOrThrow(ModDimensionTypes.THE_LAST_MILLENNIUM),
				FlatLevelSource(tlmSettings)
			)
		)

		context.register(
			THE_DEPTHS,
			LevelStem(
				dimensionTypeLookup.getOrThrow(ModDimensionTypes.THE_DEPTHS),
				NoiseBasedChunkGenerator(
					FixedBiomeSource(biomeLookup.getOrThrow(ModBiomes.THE_DEPTHS)),
					noiseSettingsLookup.getOrThrow(ModNoiseSettings.THE_DEPTHS)
				)
			)
		)
	}

	private fun rk(path: String): ResourceKey<LevelStem> {
		return ResourceKey.create(Registries.LEVEL_STEM, ExcessiveUtilities.modResource(path))
	}

}