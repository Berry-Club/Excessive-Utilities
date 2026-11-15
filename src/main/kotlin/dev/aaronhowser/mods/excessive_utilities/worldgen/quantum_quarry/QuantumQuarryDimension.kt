package dev.aaronhowser.mods.excessive_utilities.worldgen.quantum_quarry

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.tags.BiomeTags
import net.minecraft.tags.TagKey
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.dimension.BuiltinDimensionTypes
import net.minecraft.world.level.dimension.DimensionType
import net.minecraft.world.level.dimension.LevelStem
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings

enum class QuantumQuarryDimension(
	private val serializedName: String,
	levelName: String,
	val biomeTag: TagKey<Biome>,
	val fallbackBiome: ResourceKey<Biome>,
	val dimensionType: ResourceKey<DimensionType>,
	val noiseSettings: ResourceKey<NoiseGeneratorSettings>
) : StringRepresentable {
	OVERWORLD(
		"overworld",
		"quantum_quarry",
		BiomeTags.IS_OVERWORLD,
		Biomes.PLAINS,
		BuiltinDimensionTypes.OVERWORLD,
		NoiseGeneratorSettings.OVERWORLD
	),
	NETHER(
		"nether",
		"quantum_quarry_nether",
		BiomeTags.IS_NETHER,
		Biomes.NETHER_WASTES,
		BuiltinDimensionTypes.NETHER,
		NoiseGeneratorSettings.NETHER
	),
	END(
		"end",
		"quantum_quarry_end",
		BiomeTags.IS_END,
		Biomes.THE_END,
		BuiltinDimensionTypes.END,
		NoiseGeneratorSettings.END
	);

	val levelKey: ResourceKey<Level> =
		ResourceKey.create(Registries.DIMENSION, ExcessiveUtilities.modResource(levelName))

	val levelStemKey: ResourceKey<LevelStem> =
		ResourceKey.create(Registries.LEVEL_STEM, ExcessiveUtilities.modResource(levelName))

	override fun getSerializedName(): String = serializedName

	companion object {
		val CODEC: Codec<QuantumQuarryDimension> = StringRepresentable.fromEnum(::values)

		@JvmStatic
		fun isTransientDimension(dimension: ResourceKey<Level>): Boolean {
			return entries.any { it.levelKey == dimension }
		}

		fun fromSerializedName(name: String): QuantumQuarryDimension? {
			return entries.firstOrNull { it.serializedName == name }
		}
	}

}