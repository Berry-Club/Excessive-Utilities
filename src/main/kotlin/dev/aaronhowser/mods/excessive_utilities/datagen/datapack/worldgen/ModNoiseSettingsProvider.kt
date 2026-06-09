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
import net.minecraft.world.level.levelgen.SurfaceRules
import net.minecraft.world.level.levelgen.VerticalAnchor

object ModNoiseSettingsProvider {

	val DEEP_DARK = rk("deep_dark")

	fun bootstrap(context: BootstrapContext<NoiseGeneratorSettings>) {

		val densityLookup = context.lookup(Registries.DENSITY_FUNCTION)
		val deepDarkDensity = densityLookup.getOrThrow(ModDensityFunctionsProvider.DEEP_DARK)

		val bottomStone = DensityFunctions.add(
			DensityFunctions.constant(1.0),
			DensityFunctions.mul(
				DensityFunctions.yClampedGradient(0, 64, -1.0, 1.0),
				DensityFunctions.constant(1.0)
			)
		)

		val topStone = DensityFunctions.add(
			DensityFunctions.constant(1.0),
			DensityFunctions.mul(
				DensityFunctions.yClampedGradient(128, 256, -1.0, 1.0),
				DensityFunctions.constant(1.0)
			)
		)

		val union = DensityFunctions.max(bottomStone, topStone)

		context.register(
			DEEP_DARK,
			NoiseGeneratorSettings(
				NoiseSettings(0, 256, 1, 2),
				Blocks.STONE.defaultBlockState(),
				Blocks.AIR.defaultBlockState(),
				NoiseRouter(
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
					union,
					union,
					DensityFunctions.zero(),
					DensityFunctions.zero(),
					DensityFunctions.zero()
				),
				deepDarkRules(),
				listOf(),
				63,
				false,
				false,
				true,
				false
			)
		)
	}

	private fun deepDarkRules(): SurfaceRules.RuleSource {
		val bottomLayer = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(64), 0)
		val topLayer = SurfaceRules.yBlockCheck(VerticalAnchor.absolute(64 + 64), 0)

		val stone = SurfaceRules.state(Blocks.STONE.defaultBlockState())
		val air = SurfaceRules.state(Blocks.AIR.defaultBlockState())

		return SurfaceRules.sequence(
			SurfaceRules.ifTrue(
				bottomLayer,
				stone
			),
		)
	}

	private fun rk(path: String): ResourceKey<NoiseGeneratorSettings> {
		return ResourceKey.create(Registries.NOISE_SETTINGS, ExcessiveUtilities.modResource(path))
	}


}