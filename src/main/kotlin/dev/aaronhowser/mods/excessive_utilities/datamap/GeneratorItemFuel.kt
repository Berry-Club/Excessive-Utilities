package dev.aaronhowser.mods.excessive_utilities.datamap

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GeneratorType
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.datamaps.DataMapType

data class GeneratorItemFuel(
	val fePerTick: Int,
	val burnTime: Int
) {

	companion object {
		val CODEC: Codec<GeneratorItemFuel> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.INT
						.fieldOf("fe_per_tick")
						.forGetter(GeneratorItemFuel::fePerTick),
					Codec.INT
						.fieldOf("burn_time")
						.forGetter(GeneratorItemFuel::burnTime)
				).apply(instance, ::GeneratorItemFuel)
			}

		val ENDER: DataMapType<Item, GeneratorItemFuel> = createMap(GeneratorType.ENDER)
		val EXPLOSIVE: DataMapType<Item, GeneratorItemFuel> = createMap(GeneratorType.EXPLOSIVE)
		val PINK: DataMapType<Item, GeneratorItemFuel> = createMap(GeneratorType.PINK)
		val NETHER_STAR: DataMapType<Item, GeneratorItemFuel> = createMap(GeneratorType.NETHER_STAR)
		val FROSTY: DataMapType<Item, GeneratorItemFuel> = createMap(GeneratorType.FROSTY)
		val HALITOSIS: DataMapType<Item, GeneratorItemFuel> = createMap(GeneratorType.HALITOSIS)
		val DEATH: DataMapType<Item, GeneratorItemFuel> = createMap(GeneratorType.DEATH)

		private fun createMap(generatorType: GeneratorType): DataMapType<Item, GeneratorItemFuel> {
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