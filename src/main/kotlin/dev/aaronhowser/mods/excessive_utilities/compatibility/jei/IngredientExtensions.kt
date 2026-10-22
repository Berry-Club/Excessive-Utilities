package dev.aaronhowser.mods.excessive_utilities.compatibility.jei

import net.minecraft.core.Holder
import net.minecraft.world.item.Item
import net.minecraft.world.item.crafting.Ingredient
import java.util.stream.Stream

internal fun Ingredient.getItemHolders(): Stream<Holder<Item>> =
	if (isCustom()) {
		(getCustomIngredient() ?: error("Custom ingredient has no custom ingredient implementation")).items()
	} else {
		getValues().stream()
	}
