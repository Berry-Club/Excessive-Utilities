package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import net.minecraft.world.item.TooltipFlag

class SpeedUpgradeItem(properties: Properties) : Item(properties) {

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val stackSize = stack.count

		val gpCost = getGpCost(stackSize)
		val formattedGpCost = "%.2f".format(gpCost)

		tooltipComponents += ModMenuLang.GP_COST.toComponent(formattedGpCost)
	}

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

		fun getGpCost(stackSize: Int): Double {
			val baseCost = gpCostCalculator(stackSize)
			val multiplier = ServerConfig.CONFIG.speedUpgradeGpCostMultiplier.get()

			return baseCost * multiplier
		}

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