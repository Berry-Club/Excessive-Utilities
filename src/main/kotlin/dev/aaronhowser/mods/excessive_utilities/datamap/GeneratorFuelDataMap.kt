package dev.aaronhowser.mods.excessive_utilities.datamap

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.datamaps.DataMapType

data class GeneratorFuelDataMap(
	val generatorType: GeneratorType,
	val fePerTick: Int,
	val burnTime: Int
) {

	companion object {
		val CODEC: Codec<GeneratorFuelDataMap> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					GeneratorType.CODEC
						.fieldOf("type")
						.forGetter(GeneratorFuelDataMap::generatorType),
					Codec.INT
						.fieldOf("fe_per_tick")
						.forGetter(GeneratorFuelDataMap::fePerTick),
					Codec.INT
						.fieldOf("burn_time")
						.forGetter(GeneratorFuelDataMap::burnTime)
				).apply(instance, ::GeneratorFuelDataMap)
			}

		val DATA_MAP_TYPE: DataMapType<Item, GeneratorFuelDataMap> =
			DataMapType
				.builder(
					ExcessiveUtilities.modResource("generator_fuel"),
					Registries.ITEM,
					CODEC
				)
				.synced(CODEC, true)
				.build()

		fun getFuelData(stack: ItemStack): GeneratorFuelDataMap? {
			return stack.itemHolder.getData(DATA_MAP_TYPE)
		}
	}

}