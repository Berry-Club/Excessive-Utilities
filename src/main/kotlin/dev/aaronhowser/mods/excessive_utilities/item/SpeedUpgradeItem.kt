package dev.aaronhowser.mods.excessive_utilities.item

import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity

class SpeedUpgradeItem(properties: Properties) : Item(properties) {

	companion object {
		val BASIC_PROPERTIES: Properties =
			Properties()
				.stacksTo(4)
				.rarity(Rarity.UNCOMMON)

		val MAGICAL_PROPERTIES: Properties =
			Properties()
				.stacksTo(16)
				.rarity(Rarity.RARE)
				.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)

		val ULTIMATE_PROPERTIES: Properties =
			Properties()
				.stacksTo(64)
				.rarity(Rarity.EPIC)
				.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
	}

}