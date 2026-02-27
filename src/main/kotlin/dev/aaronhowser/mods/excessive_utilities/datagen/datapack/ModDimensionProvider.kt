package dev.aaronhowser.mods.excessive_utilities.datagen.datapack

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.MultiNoiseBiomeSource
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings

object ModDimensionProvider {

	val QUANTUM_QUARRY_LEVEL_STEM: ResourceKey<LevelStem> =
		ResourceKey.create(Registries.LEVEL_STEM, ExcessiveUtilities.modResource("quantum_quarry"))
	val QUANTUM_QUARRY_LEVEL: ResourceKey<Level> =
		ResourceKey.create(Registries.DIMENSION, ExcessiveUtilities.modResource("quantum_quarry"))

	fun bootstrap(context: BootstrapContext<LevelStem>) {
		val dimensionTypeLookup =
			context.lookup(Registries.DIMENSION_TYPE)

		val noiseSettingsLookup =
			context.lookup(Registries.NOISE_SETTINGS)

		val multiNoiseLookup =
			context.lookup(Registries.MULTI_NOISE_BIOME_SOURCE_PARAMETER_LIST)

		val overworldDimensionType =
			dimensionTypeLookup.getOrThrow(BuiltinDimensionTypes.OVERWORLD)

		val overworldNoiseSettings =
			noiseSettingsLookup.getOrThrow(NoiseGeneratorSettings.OVERWORLD)

		val overworldMultiNoiseParameters =
			multiNoiseLookup.getOrThrow(MultiNoiseBiomeSourceParameterLists.OVERWORLD)

		val overworldBiomeSource =
			MultiNoiseBiomeSource.createFromPreset(overworldMultiNoiseParameters)

		val chunkGenerator =
			NoiseBasedChunkGenerator(
				overworldBiomeSource,
				overworldNoiseSettings
			)

		context.register(
			QUANTUM_QUARRY_LEVEL_STEM,
			LevelStem(
				overworldDimensionType,
				chunkGenerator
			)
		)
	}

}