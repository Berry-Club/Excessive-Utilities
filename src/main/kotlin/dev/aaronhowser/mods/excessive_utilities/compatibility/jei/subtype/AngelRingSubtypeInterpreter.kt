package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.subtype

import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import net.minecraft.world.item.ItemStack

object AngelRingSubtypeInterpreter : ISubtypeInterpreter<ItemStack> {

	override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? {
		return ingredient.get(ModDataComponents.ANGEL_RING_TYPE)
	}

	@Deprecated("Deprecated in Java")
	override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String {
		val remainingUses = ingredient.get(ModDataComponents.ANGEL_RING_TYPE) ?: return ""
		return remainingUses.toString()
	}
}