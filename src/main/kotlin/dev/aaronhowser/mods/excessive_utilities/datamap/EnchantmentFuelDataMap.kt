package dev.aaronhowser.mods.excessive_utilities.datamap

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.enchantment.Enchantment
import net.neoforged.neoforge.registries.datamaps.DataMapType

class EnchantmentFuelDataMap(
	val fePerTick: Int,
	val burnTimePerLevel: Int
) {

	companion object {
		val CODEC: Codec<EnchantmentFuelDataMap> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					Codec.INT
						.fieldOf("fe_per_tick")
						.forGetter(EnchantmentFuelDataMap::fePerTick),
					Codec.INT
						.fieldOf("burn_time_per_level")
						.forGetter(EnchantmentFuelDataMap::burnTimePerLevel)
				).apply(instance, ::EnchantmentFuelDataMap)
			}

		val DATA_MAP_TYPE: DataMapType<Enchantment, EnchantmentFuelDataMap> =
			DataMapType
				.builder(
					ExcessiveUtilities.modResource("enchantment_generator_fuel"),
					Registries.ENCHANTMENT,
					CODEC
				)
				.synced(CODEC, true)
				.build()

		fun getFuelData(stack: ItemStack, lookup: HolderLookup.RegistryLookup<Enchantment>): List<EnchantmentFuelDataMap> {
			val enchantments = stack.getAllEnchantments(lookup)
			return enchantments.entrySet().mapNotNull { it.key.getData(DATA_MAP_TYPE) }
		}
	}

}