package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType

class HeatingCoilItem(properties: Properties) : Item(properties) {

	override fun getBurnTime(itemStack: ItemStack, recipeType: RecipeType<*>?): Int {
		return if (recipeType == RecipeType.SMELTING) {
			100
		} else {
			0
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = { Properties().component(ModDataComponents.ENERGY, 1000) }

		@JvmStatic
		fun burnInFuelSlot(stack: ItemStack) {
			val energy = stack.getOrDefault(ModDataComponents.ENERGY, 0)
			val newEnergy = maxOf(0, energy - 20)
			stack.set(ModDataComponents.ENERGY, newEnergy)
		}
	}

}