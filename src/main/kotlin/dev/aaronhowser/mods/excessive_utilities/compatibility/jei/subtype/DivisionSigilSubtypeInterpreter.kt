package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.subtype

import dev.aaronhowser.mods.excessive_utilities.item.DivisionSigilItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import net.minecraft.world.item.ItemStack

object DivisionSigilSubtypeInterpreter : ISubtypeInterpreter<ItemStack> {

	override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? {
		return if (DivisionSigilItem.isInverted(ingredient)) {
			true
		} else {
			ingredient.get(ModDataComponents.REMAINING_USES)
		}
	}

	@Deprecated("Deprecated in Java")
	override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String {
		return if (DivisionSigilItem.isInverted(ingredient)) {
			"inverted"
		} else {
			ingredient.getOrDefault(ModDataComponents.REMAINING_USES, 0).toString()
		}
	}
}