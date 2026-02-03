package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.AaronExtensions.isHolder
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.item.enchantment.ItemEnchantments

class TrowelItem(properties: Properties) : ShovelItem(Tiers.IRON, properties) {

	override fun getEnchantmentLevel(stack: ItemStack, enchantment: Holder<Enchantment>): Int {
		if (enchantment.isHolder(Enchantments.SILK_TOUCH)) {
			return maxOf(1, super.getEnchantmentLevel(stack, enchantment))
		}

		return super.getEnchantmentLevel(stack, enchantment)
	}

	override fun getAllEnchantments(stack: ItemStack, lookup: HolderLookup.RegistryLookup<Enchantment>): ItemEnchantments {
		val enchants = ItemEnchantments.Mutable(super.getAllEnchantments(stack, lookup))
		enchants.upgrade(lookup.getOrThrow(Enchantments.SILK_TOUCH), 1)
		return enchants.toImmutable()
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties =
			Properties()
				.attributes(createAttributes(Tiers.IRON, 1.5f, -3f))
	}

}