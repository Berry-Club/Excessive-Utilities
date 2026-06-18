package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.HolderSet
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.GenerationStep
import net.neoforged.neoforge.common.world.BiomeModifier
import net.neoforged.neoforge.common.world.BiomeModifiers
import net.neoforged.neoforge.registries.NeoForgeRegistries

object ModBiomeModifiers {

	val DEEP_DARK_PILLAR = rk("deep_dark_pillar")
	val DEEP_DARK_STALACTITE = rk("deep_dark_stalactite")
	val DEEP_DARK_SCULK_VEIN = rk("deep_dark_sculk_vein")

	fun bootstrap(context: BootstrapContext<BiomeModifier>) {
		val placedFeatures = context.lookup(Registries.PLACED_FEATURE)
		val biomes = context.lookup(Registries.BIOME)

		context.register(
			DEEP_DARK_PILLAR,
			BiomeModifiers.AddFeaturesBiomeModifier(
				HolderSet.direct(biomes.getOrThrow(ModBiomes.DEEP_DARK)),
				HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DEEP_DARK_PILLAR)),
				GenerationStep.Decoration.UNDERGROUND_STRUCTURES
			)
		)

		context.register(
			DEEP_DARK_STALACTITE,
			BiomeModifiers.AddFeaturesBiomeModifier(
				HolderSet.direct(biomes.getOrThrow(ModBiomes.DEEP_DARK)),
				HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DEEP_DARK_STALACTITE)),
				GenerationStep.Decoration.UNDERGROUND_DECORATION
			)
		)

		context.register(
			DEEP_DARK_SCULK_VEIN,
			BiomeModifiers.AddFeaturesBiomeModifier(
				HolderSet.direct(biomes.getOrThrow(ModBiomes.DEEP_DARK)),
				HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DEEP_DARK_SCULK_VEIN)),
				GenerationStep.Decoration.UNDERGROUND_ORES
			)
		)

	}

	private fun rk(name: String): ResourceKey<BiomeModifier> {
		return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ExcessiveUtilities.modResource(name))
	}

}
