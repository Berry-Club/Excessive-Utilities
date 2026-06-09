package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeGenerationSettings
import net.minecraft.world.level.biome.BiomeSpecialEffects
import net.minecraft.world.level.biome.MobSpawnSettings

object ModBiomeProvider {

	val DEEP_DARK = rk("deep_dark")

	fun bootstrap(context: BootstrapContext<Biome>) {
		val biome = Biome.BiomeBuilder()
			.hasPrecipitation(false)
			.temperature(0.8f)
			.downfall(0f)
			.specialEffects(
				BiomeSpecialEffects.Builder()
					.fogColor(0)
					.waterColor(4159204)
					.waterFogColor(329011)
					.skyColor(0)
					.build()
			)
			.mobSpawnSettings(
				MobSpawnSettings.Builder().build()
			)
			.generationSettings(
				BiomeGenerationSettings.Builder(
					context.lookup(Registries.PLACED_FEATURE),
					context.lookup(Registries.CONFIGURED_CARVER)
				).build()
			)
			.build()

		context.register(DEEP_DARK, biome)
	}

	private fun rk(path: String): ResourceKey<Biome> {
		return ResourceKey.create(Registries.BIOME, ExcessiveUtilities.modResource(path))
	}

}