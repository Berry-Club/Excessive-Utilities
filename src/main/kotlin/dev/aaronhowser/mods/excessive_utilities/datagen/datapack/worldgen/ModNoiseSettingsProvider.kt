package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import net.minecraft.world.level.levelgen.NoiseRouter
import net.minecraft.world.level.levelgen.NoiseSettings
import net.minecraft.world.level.levelgen.SurfaceRules

object ModNoiseSettingsProvider {

	val DEEP_DARK = rk("deep_dark")

	fun bootstrap(context: BootstrapContext<NoiseGeneratorSettings>) {
		val densityFunctions = context.lookup(Registries.DENSITY_FUNCTION)

		val layeredDensity = densityFunctions.getOrThrow(ModDensityFunctionsProvider.DEEP_DARK).value()

		val settings = NoiseGeneratorSettings(
			NoiseSettings.create(0, 256, 1, 2),
			Blocks.STONE.defaultBlockState(),
			Blocks.AIR.defaultBlockState(),
			NoiseRouter(
				layeredDensity,
				layeredDensity,
				layeredDensity,
				layeredDensity,
				layeredDensity,
				layeredDensity,
				layeredDensity,
				layeredDensity,
				layeredDensity,
				layeredDensity,
				layeredDensity,
				layeredDensity,
				layeredDensity,
				layeredDensity,
				layeredDensity,
			),
			SurfaceRules.state(Blocks.STONE.defaultBlockState()),
			listOf(),
			63,
			false,
			false,
			false,
			false
		)

		context.register(DEEP_DARK, settings)
	}

	private fun rk(path: String): ResourceKey<NoiseGeneratorSettings> {
		return ResourceKey.create(Registries.NOISE_SETTINGS, ExcessiveUtilities.modResource(path))
	}


}