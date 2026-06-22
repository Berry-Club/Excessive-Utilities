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

	val DEPTHS_PILLAR = rk("depths_pillar")
	val DEPTHS_STALACTITE = rk("depths_stalactite")
	val DEPTHS_SCULK_VEIN = rk("depths_sculk_vein")

	fun bootstrap(context: BootstrapContext<BiomeModifier>) {
		val placedFeatures = context.lookup(Registries.PLACED_FEATURE)
		val biomes = context.lookup(Registries.BIOME)

		context.register(
			DEPTHS_PILLAR,
			BiomeModifiers.AddFeaturesBiomeModifier(
				HolderSet.direct(biomes.getOrThrow(ModBiomes.THE_DEPTHS)),
				HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DEPTHS_PILLAR)),
				GenerationStep.Decoration.UNDERGROUND_STRUCTURES
			)
		)

		context.register(
			DEPTHS_STALACTITE,
			BiomeModifiers.AddFeaturesBiomeModifier(
				HolderSet.direct(biomes.getOrThrow(ModBiomes.THE_DEPTHS)),
				HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DEPTHS_STALACTITE)),
				GenerationStep.Decoration.UNDERGROUND_DECORATION
			)
		)

		context.register(
			DEPTHS_SCULK_VEIN,
			BiomeModifiers.AddFeaturesBiomeModifier(
				HolderSet.direct(biomes.getOrThrow(ModBiomes.THE_DEPTHS)),
				HolderSet.direct(placedFeatures.getOrThrow(ModPlacedFeatures.DEPTHS_SCULK_VEIN)),
				GenerationStep.Decoration.UNDERGROUND_ORES
			)
		)

	}

	private fun rk(name: String): ResourceKey<BiomeModifier> {
		return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ExcessiveUtilities.modResource(name))
	}

}
