package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.*

object ModNoiseSettingsProvider {

	const val FLOOR_TOP = 64
	const val CEILING_BOTTOM = 128
	const val BLEND_THICKNESS = 8

	const val MIN_Y = 0
	const val MAX_Y = 256

	val DEEP_DARK = rk("deep_dark")

	fun bootstrap(context: BootstrapContext<NoiseGeneratorSettings>) {
		context.register(
			DEEP_DARK,
			NoiseGeneratorSettings(
				NoiseSettings(MIN_Y, MAX_Y, 1, 1),
				Blocks.STONE.defaultBlockState(),
				Blocks.AIR.defaultBlockState(),
				buildNoiseRouter(),
				deepDarkRules(),
				listOf(),
				FLOOR_TOP,
				false,
				false,
				true,
				false
			)
		)
	}

	private fun buildNoiseRouter(): NoiseRouter {
		val floorDensity = DensityFunctions.yClampedGradient(
			MIN_Y,
			FLOOR_TOP + BLEND_THICKNESS,
			1.0,
			-1.0
		)

		val ceilingDensity = DensityFunctions.yClampedGradient(
			CEILING_BOTTOM - BLEND_THICKNESS,
			MAX_Y,
			-1.0,
			1.0
		)

		val shape = DensityFunctions.max(floorDensity, ceilingDensity)

		return NoiseRouter(
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
			shape,
			shape,
			DensityFunctions.zero(),
			DensityFunctions.zero(),
			DensityFunctions.zero()
		)
	}

	private fun deepDarkRules(): SurfaceRules.RuleSource {
		val bedrock = SurfaceRules.state(Blocks.BEDROCK.defaultBlockState())
		val stone = SurfaceRules.state(Blocks.STONE.defaultBlockState())
		val cobble = SurfaceRules.state(Blocks.COBBLESTONE.defaultBlockState())

		val isWorldBottom = SurfaceRules.not(
			SurfaceRules.yBlockCheck(
				VerticalAnchor.aboveBottom(3),
				1
			)
		)
		val isWorldTop = SurfaceRules.not(
			SurfaceRules.yBlockCheck(
				VerticalAnchor.belowTop(3),
				1
			)
		)

		return SurfaceRules.sequence(
			SurfaceRules.ifTrue(isWorldBottom, bedrock),
			SurfaceRules.ifTrue(isWorldTop, bedrock),
		)
	}

	private fun rk(path: String): ResourceKey<NoiseGeneratorSettings> {
		return ResourceKey.create(Registries.NOISE_SETTINGS, ExcessiveUtilities.modResource(path))
	}


}