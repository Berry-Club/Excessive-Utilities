package dev.aaronhowser.mods.excessive_utilities.datagen.datapack

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.Registry
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.BlockTags
import net.minecraft.util.valueproviders.ConstantInt
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.MultiNoiseBiomeSource
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterLists
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import java.util.OptionalLong

object ModDimensionProvider {

	val QUANTUM_QUARRY_LEVEL_STEM: ResourceKey<LevelStem> =
		rk(Registries.LEVEL_STEM, "quantum_quarry")
	val QUANTUM_QUARRY_LEVEL: ResourceKey<Level> =
		rk(Registries.DIMENSION, "quantum_quarry")

	val MILLENNIUM_RL: ResourceLocation =
		ExcessiveUtilities.modResource("the_last_millennium")
	val MILLENNIUM_STEM_KEY: ResourceKey<LevelStem> =
		rk(Registries.LEVEL_STEM, "the_last_millennium")
	val MILLENNIUM_LEVEL_KEY: ResourceKey<Level> =
		rk(Registries.DIMENSION, "the_last_millennium")
	val MILLENNIUM_DIM_TYPE_KEY: ResourceKey<DimensionType> =
		rk(Registries.DIMENSION_TYPE, "the_last_millennium")

	fun bootstrapType(context: BootstrapContext<DimensionType>) {
		context.register(
			MILLENNIUM_DIM_TYPE_KEY,
			DimensionType(
				OptionalLong.of(18000L),
				true,
				false,
				false,
				false,
				1.0,
				true,
				true,
				0,
				256,
				256,
				BlockTags.INFINIBURN_OVERWORLD,
				MILLENNIUM_RL,
				1f,
				DimensionType.MonsterSettings(
					false,
					false,
					ConstantInt.of(0),
					0
				)
			)
		)
	}

	fun bootstrapLevelStem(context: BootstrapContext<LevelStem>) {
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

	private fun <T> rk(registryKey: ResourceKey<out Registry<T>>, path: String): ResourceKey<T> {
		return ResourceKey.create(registryKey, ExcessiveUtilities.modResource(path))
	}

}