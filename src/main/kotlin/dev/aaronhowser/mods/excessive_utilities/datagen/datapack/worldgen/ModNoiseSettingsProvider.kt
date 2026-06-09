package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.data.worldgen.SurfaceRuleData
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.DensityFunctions
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings
import net.minecraft.world.level.levelgen.NoiseRouter
import net.minecraft.world.level.levelgen.NoiseSettings

object ModNoiseSettingsProvider {

	val DEEP_DARK = rk("deep_dark")

	fun bootstrap(context: BootstrapContext<NoiseGeneratorSettings>) {

		val densityLookup = context.lookup(Registries.DENSITY_FUNCTION)
		val deepDarkDensity = densityLookup.getOrThrow(ModDensityFunctionsProvider.DEEP_DARK).value()

		context.register(
			DEEP_DARK,
			NoiseGeneratorSettings(
				NoiseSettings(0, 256, 1, 2),
				Blocks.STONE.defaultBlockState(),
				Blocks.WATER.defaultBlockState(),
				NoiseRouter(
					deepDarkDensity,
					DensityFunctions.zero(),
					DensityFunctions.zero(),
					DensityFunctions.zero(),
					DensityFunctions.zero(),
					DensityFunctions.zero(),
					DensityFunctions.zero(),
					DensityFunctions.zero(),
					DensityFunctions.zero(),
					DensityFunctions.zero(),
					DensityFunctions.zero(),
					DensityFunctions.zero(),
					DensityFunctions.zero(),
					DensityFunctions.zero(),
					DensityFunctions.zero()
				),
				SurfaceRuleData.overworld(),
				listOf(),
				63,
				false,
				false,
				true,
				false
			)
		)
	}

	private fun rk(path: String): ResourceKey<NoiseGeneratorSettings> {
		return ResourceKey.create(Registries.NOISE_SETTINGS, ExcessiveUtilities.modResource(path))
	}


}