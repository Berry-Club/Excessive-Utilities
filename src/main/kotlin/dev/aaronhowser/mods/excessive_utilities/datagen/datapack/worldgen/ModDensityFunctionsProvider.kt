package dev.aaronhowser.mods.excessive_utilities.datagen.datapack.worldgen

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.data.worldgen.BootstrapContext
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.levelgen.DensityFunction
import net.minecraft.world.level.levelgen.DensityFunctions

object ModDensityFunctionsProvider {

	val DEEP_DARK = rk("deep_dark")

	fun bootstrap(context: BootstrapContext<DensityFunction>) {

		val yGradient = DensityFunctions.yClampedGradient(
			0,
			256,
			0.0,
			256.0
		)

		val stone = DensityFunctions.constant(1.0)
		val air = DensityFunctions.constant(-1.0)

		val middleAir = DensityFunctions.rangeChoice(
			yGradient,
			-10000.0,
			62.0,
			stone,
			DensityFunctions.rangeChoice(
				yGradient,
				62.0,
				120.0,
				air,
				stone
			)
		)

		context.register(
			DEEP_DARK,
			middleAir
		)
	}

	private fun rk(path: String): ResourceKey<DensityFunction> {
		return ResourceKey.create(Registries.DENSITY_FUNCTION, ExcessiveUtilities.modResource(path))
	}

}