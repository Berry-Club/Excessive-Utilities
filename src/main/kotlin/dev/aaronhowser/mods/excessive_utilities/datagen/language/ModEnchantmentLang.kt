package dev.aaronhowser.mods.excessive_utilities.datagen.language

import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModEnchantmentProvider
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.enchantment.Enchantment

object ModEnchantmentLang {

	fun add(provider: ModLanguageProvider) {
		fun addEnchantment(enchantment: ResourceKey<Enchantment>, value: String) {
			val location = enchantment.location()
			val key = StringBuilder()
				.append("enchantment.")
				.append(location.namespace)
				.append(".")
				.append(location.path)
				.toString()

			provider.add(key, value)
		}

		addEnchantment(ModEnchantmentProvider.BLADERANG, "Bladerang")
		addEnchantment(ModEnchantmentProvider.BOOMEREAPERANG, "Boomereaperang")
		addEnchantment(ModEnchantmentProvider.BURNERANG, "Burnerang")
		addEnchantment(ModEnchantmentProvider.KABOOMERANG, "Kaboomerang")
		addEnchantment(ModEnchantmentProvider.ZOOMERANG, "Zoomerang")
	}

}