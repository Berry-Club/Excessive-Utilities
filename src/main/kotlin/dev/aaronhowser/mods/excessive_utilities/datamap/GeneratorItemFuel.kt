package dev.aaronhowser.mods.excessive_utilities.datamap

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block.base.DataDrivenGeneratorType
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

		val ENDER: DataMapType<Item, GeneratorItemFuel> = createMap(DataDrivenGeneratorType.ENDER)
		val EXPLOSIVE: DataMapType<Item, GeneratorItemFuel> = createMap(DataDrivenGeneratorType.EXPLOSIVE)
		val PINK: DataMapType<Item, GeneratorItemFuel> = createMap(DataDrivenGeneratorType.PINK)
		val NETHER_STAR: DataMapType<Item, GeneratorItemFuel> = createMap(DataDrivenGeneratorType.NETHER_STAR)
		val FROSTY: DataMapType<Item, GeneratorItemFuel> = createMap(DataDrivenGeneratorType.FROSTY)
		val HALITOSIS: DataMapType<Item, GeneratorItemFuel> = createMap(DataDrivenGeneratorType.HALITOSIS)
		val DEATH: DataMapType<Item, GeneratorItemFuel> = createMap(DataDrivenGeneratorType.DEATH)

		private fun createMap(generatorType: DataDrivenGeneratorType): DataMapType<Item, GeneratorItemFuel> {
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