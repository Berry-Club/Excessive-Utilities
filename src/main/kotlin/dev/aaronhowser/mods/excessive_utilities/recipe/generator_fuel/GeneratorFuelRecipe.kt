package dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

abstract class GeneratorFuelRecipe<T : RecipeInput>(
	val fePerTick: Int,
	val duration: Int
) : Recipe<T> {

	override fun canCraftInDimensions(width: Int, height: Int): Boolean = true
	override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
	override fun assemble(input: T, registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

}