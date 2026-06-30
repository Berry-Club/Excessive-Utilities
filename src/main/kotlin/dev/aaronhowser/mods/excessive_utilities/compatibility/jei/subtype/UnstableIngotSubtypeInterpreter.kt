package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.subtype

import dev.aaronhowser.mods.excessive_utilities.item.UnstableIngotItem
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import net.minecraft.world.item.ItemStack

// Purposely unused
object UnstableIngotSubtypeInterpreter : ISubtypeInterpreter<ItemStack> {

	override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any {
		return stability(ingredient)
	}

	override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String {
		return stability(ingredient).toString()
	}

	private fun stability(stack: ItemStack): Int {
		return when {
			UnstableIngotItem.isCheesed(stack) -> -1
			UnstableIngotItem.isUnstableAndCountingDown(stack) -> 0
			UnstableIngotItem.isStable(stack) -> 1
			else -> -2
		}
	}
}