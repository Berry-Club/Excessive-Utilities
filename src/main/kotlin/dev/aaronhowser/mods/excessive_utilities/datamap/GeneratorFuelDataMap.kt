package dev.aaronhowser.mods.excessive_utilities.datamap

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GeneratorType
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.datamaps.DataMapType

data class GeneratorFuelDataMap(
	val fePerTick: Int,
	val burnTime: Int
) {

	companion object {
		val CODEC: Codec<GeneratorFuelDataMap> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.INT
						.fieldOf("fe_per_tick")
						.forGetter(GeneratorFuelDataMap::fePerTick),
					Codec.INT
						.fieldOf("burn_time")
						.forGetter(GeneratorFuelDataMap::burnTime)
				).apply(instance, ::GeneratorFuelDataMap)
			}

		val ENDER: DataMapType<Item, GeneratorFuelDataMap> = createMap(GeneratorType.ENDER)
		val EXPLOSIVE: DataMapType<Item, GeneratorFuelDataMap> = createMap(GeneratorType.EXPLOSIVE)
		val PINK: DataMapType<Item, GeneratorFuelDataMap> = createMap(GeneratorType.PINK)
		val NETHER_STAR: DataMapType<Item, GeneratorFuelDataMap> = createMap(GeneratorType.NETHER_STAR)
		val FROSTY: DataMapType<Item, GeneratorFuelDataMap> = createMap(GeneratorType.FROSTY)
		val HALITOSIS: DataMapType<Item, GeneratorFuelDataMap> = createMap(GeneratorType.HALITOSIS)
		val DEATH: DataMapType<Item, GeneratorFuelDataMap> = createMap(GeneratorType.DEATH)

		private fun createMap(generatorType: GeneratorType): DataMapType<Item, GeneratorFuelDataMap> {
			return DataMapType
				.builder(
					ExcessiveUtilities.modResource("generator_fuel/${generatorType.serializedName}"),
					Registries.ITEM,
					CODEC
				)
				.synced(CODEC, true)
				.build()
		}
	}

}