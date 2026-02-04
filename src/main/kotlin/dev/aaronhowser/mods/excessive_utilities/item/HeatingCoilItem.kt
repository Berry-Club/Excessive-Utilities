package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class HeatingCoilItem(properties: Properties) : Item(properties) {

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = { Properties().component(ModDataComponents.ENERGY, 1000) }

		@JvmStatic
		fun getFuelBurnTime(stack: ItemStack): Int {
			return if (
				stack.isItem(ModItems.HEATING_COIL)
				&& stack.getOrDefault(ModDataComponents.ENERGY, 0) > 0
			) 100 else 0
		}
	}

}