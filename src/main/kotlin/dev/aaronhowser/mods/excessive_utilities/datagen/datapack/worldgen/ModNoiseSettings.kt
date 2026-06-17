package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.*

object ModNoiseSettings {

	val DEEP_DARK = rk("deep_dark")

	fun bootstrap(context: BootstrapContext<NoiseGeneratorSettings>) {
		context.register(
			DEEP_DARK,
			NoiseGeneratorSettings(
				NoiseSettings(DeepDarkConstants.MIN_Y, DeepDarkConstants.HEIGHT, 1, 1),
				Blocks.STONE.defaultBlockState(),
				Blocks.AIR.defaultBlockState(),
				buildNoiseRouter(),
				deepDarkRules(),
				listOf(),
				DeepDarkConstants.FLOOR_TOP,
				false,
				false,
				true,
				false
			)
		)
	}

	private fun buildNoiseRouter(): NoiseRouter {
		val solidFloor = DensityFunctions.yClampedGradient(
			DeepDarkConstants.MIN_Y,
			DeepDarkConstants.FLOOR_TOP,
			1.0,
			0.0
		)

		val noiseFloor = DensityFunctions.yClampedGradient(
			DeepDarkConstants.FLOOR_TOP,
			DeepDarkConstants.FLOOR_TOP + DeepDarkConstants.BLEND_THICKNESS,
			1.0,
			-1.0
		)

		val floor = DensityFunctions.max(solidFloor, noiseFloor)

		val solidCeiling = DensityFunctions.yClampedGradient(
			DeepDarkConstants.CEILING_BOTTOM,
			DeepDarkConstants.MAX_Y,
			0.0,
			1.0
		)

		val noiseCeiling = DensityFunctions.yClampedGradient(
			DeepDarkConstants.CEILING_BOTTOM - DeepDarkConstants.BLEND_THICKNESS,
			DeepDarkConstants.CEILING_BOTTOM,
			-1.0,
			1.0
		)

		val ceiling = DensityFunctions.max(solidCeiling, noiseCeiling)

		val shape = DensityFunctions.max(floor, ceiling)

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
		val cobblestone = SurfaceRules.state(Blocks.COBBLESTONE.defaultBlockState())

		val isWorldBottom = SurfaceRules.not(
			SurfaceRules.yBlockCheck(
				VerticalAnchor.aboveBottom(3),
				0
			)
		)

		val isWorldTop = SurfaceRules.yBlockCheck(
			VerticalAnchor.belowTop(3),
			0
		)

		val isInFloorBlendZone = SurfaceRules.yBlockCheck(
			VerticalAnchor.absolute(DeepDarkConstants.FLOOR_TOP),
			0
		)

		val isInCeilingBlendZone = SurfaceRules.not(
			SurfaceRules.yBlockCheck(
				VerticalAnchor.absolute(DeepDarkConstants.CEILING_BOTTOM),
				0
			)
		)

		return SurfaceRules.sequence(
			SurfaceRules.ifTrue(isWorldBottom, bedrock),
			SurfaceRules.ifTrue(isWorldTop, bedrock),
			SurfaceRules.ifTrue(
				SurfaceRules.ON_FLOOR,
				SurfaceRules.ifTrue(isInFloorBlendZone, cobblestone)
			),
			SurfaceRules.ifTrue(
				SurfaceRules.ON_CEILING,
				SurfaceRules.ifTrue(isInCeilingBlendZone, cobblestone)
			),
			stone
		)
	}

	private fun rk(path: String): ResourceKey<NoiseGeneratorSettings> {
		return ResourceKey.create(Registries.NOISE_SETTINGS, ExcessiveUtilities.modResource(path))
	}


}
