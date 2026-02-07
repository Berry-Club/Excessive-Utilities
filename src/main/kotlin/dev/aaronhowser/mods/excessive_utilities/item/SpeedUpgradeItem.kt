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

		private var gpCostCalculator: (Int) -> Double = { stackSize ->
			if (stackSize <= 0) {
				0.0
			} else {
				stackSize * (122 + 4 * stackSize) / 126.0
			}
		}

		fun getGpCost(stackSize: Int): Double = gpCostCalculator(stackSize)

		/**
		 * Mostly meant to be called from KubeJS.
		 *
		 * Here's an example:
		 *
		 * ```js
		 * const $SpeedUpgradeItem = Java.loadClass('dev.aaronhowser.mods.excessive_utilities.item.SpeedUpgradeItem')
		 *
		 * $SpeedUpgradeItem.setGpCostCalculator(stackSize => stackSize * 10)
		 * ```
		 *
		 * This would set the GP cost to be 10 GP per Speed Upgrade in the stack
		 *
		 */
		@JvmStatic
		fun setGpCostCalculator(newFunction: (Int) -> Double) {
			gpCostCalculator = newFunction
		}
	}

}