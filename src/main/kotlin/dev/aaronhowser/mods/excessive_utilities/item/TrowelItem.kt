package dev.aaronhowser.mods.excessive_utilities.item

import net.minecraft.core.Holder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ShovelItem
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.enchantment.Enchantment
import net.minecraft.world.item.enchantment.Enchantments

class TrowelItem(properties: Properties) : ShovelItem(Tiers.IRON, properties) {

	override fun getEnchantmentLevel(stack: ItemStack, enchantment: Holder<Enchantment>): Int {
		if (enchantment == Enchantments.SILK_TOUCH) {
			return maxOf(1, super.getEnchantmentLevel(stack, enchantment))
		}

		return super.getEnchantmentLevel(stack, enchantment)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties =
			Properties()
				.attributes(createAttributes(Tiers.IRON, 1.5f, -3f))
	}

}