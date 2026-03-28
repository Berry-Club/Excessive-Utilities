package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.subtype

import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter
import mezz.jei.api.ingredients.subtypes.UidContext
import net.minecraft.world.item.ItemStack

class OpiniumCoreSubtypeInterpreter : ISubtypeInterpreter<ItemStack> {

	override fun getSubtypeData(ingredient: ItemStack, context: UidContext): Any? {
		return ingredient.get(ModDataComponents.OPINIUM_CORE_CONTENTS)
	}

	@Deprecated("Deprecated in Java")
	override fun getLegacyStringSubtypeInfo(ingredient: ItemStack, context: UidContext): String = ""
}