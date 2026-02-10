package dev.aaronhowser.mods.excessive_utilities.datamap

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.material.Fluid
import net.neoforged.neoforge.registries.datamaps.DataMapType

data class MagmaticGeneratorFuel(
	val fePerTick: Int,
	val ticksPerMb: Int
) {

	companion object {
		val CODEC: Codec<MagmaticGeneratorFuel> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.INT
						.fieldOf("fe_per_tick")
						.forGetter(MagmaticGeneratorFuel::fePerTick),
					Codec.INT
						.fieldOf("ticks_per_mb")
						.forGetter(MagmaticGeneratorFuel::ticksPerMb)
				).apply(instance, ::MagmaticGeneratorFuel)
			}

		val MAP: DataMapType<Fluid, MagmaticGeneratorFuel> =
			DataMapType
				.builder(
					ExcessiveUtilities.modResource("generator_fuel/magmatic"),
					Registries.FLUID,
					CODEC
				)
				.synced(CODEC, true)
				.build()
	}

}