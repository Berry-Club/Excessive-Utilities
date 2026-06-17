package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.HolderGetter
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.levelgen.*
import net.minecraft.world.level.levelgen.synth.NormalNoise

object ModNoiseSettings {

	val DEEP_DARK = rk("deep_dark")

	fun bootstrap(context: BootstrapContext<NoiseGeneratorSettings>) {
		context.register(
			DEEP_DARK,
			NoiseGeneratorSettings(
				NoiseSettings(DeepDarkConstants.MIN_Y, DeepDarkConstants.HEIGHT, 1, 1),
				Blocks.DEEPSLATE.defaultBlockState(),
				Blocks.AIR.defaultBlockState(),
				buildNoiseRouter(context.lookup(Registries.NOISE)),
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

	private fun buildNoiseRouter(noises: HolderGetter<NormalNoise.NoiseParameters>): NoiseRouter {
		val surfaceNoise = DensityFunctions.noise(
			noises.getOrThrow(Noises.SURFACE),
			0.085,
			0.0
		)

		val detailNoise = DensityFunctions.noise(
			noises.getOrThrow(Noises.SURFACE_SECONDARY),
			0.22,
			0.0
		)

		val roughSurface = DensityFunctions.add(
			DensityFunctions.mul(surfaceNoise, DensityFunctions.constant(0.85)),
			DensityFunctions.mul(detailNoise, DensityFunctions.constant(0.35))
		)

		val solidFloor = DensityFunctions.yClampedGradient(
			DeepDarkConstants.MIN_Y,
			DeepDarkConstants.FLOOR_TOP,
			1.0,
			0.0
		)

		val noiseFloor = DensityFunctions.yClampedGradient(
			DeepDarkConstants.FLOOR_TOP,
			DeepDarkConstants.FLOOR_TOP + DeepDarkConstants.BLEND_THICKNESS + 4,
			1.0,
			-1.0
		)

		val floor = DensityFunctions.max(
			solidFloor,
			DensityFunctions.add(noiseFloor, roughSurface)
		)

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

		val ceiling = DensityFunctions.max(
			solidCeiling,
			DensityFunctions.add(noiseCeiling, roughSurface)
		)

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
		val deepslate = SurfaceRules.state(Blocks.DEEPSLATE.defaultBlockState())
		val cobbledDeepslate = SurfaceRules.state(Blocks.COBBLED_DEEPSLATE.defaultBlockState())

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
			SurfaceRules.ifTrue(
				SurfaceRules.ON_FLOOR,
				SurfaceRules.ifTrue(isInFloorBlendZone, cobbledDeepslate)
			),
			SurfaceRules.ifTrue(
				SurfaceRules.ON_CEILING,
				SurfaceRules.ifTrue(isInCeilingBlendZone, cobbledDeepslate)
			),
			deepslate
		)
	}

	private fun rk(path: String): ResourceKey<NoiseGeneratorSettings> {
		return ResourceKey.create(Registries.NOISE_SETTINGS, ExcessiveUtilities.modResource(path))
	}


}
