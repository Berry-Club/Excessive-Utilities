package dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.recipe.base.MachineRecipe
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput

abstract class GeneratorFuelRecipe<T : RecipeInput>(
	val fePerTick: Int,
	val duration: Int
) : MachineRecipe<T> {

	fun canCraftInDimensions(width: Int, height: Int): Boolean = true
	fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
	override fun assemble(input: T): ItemStack = ItemStack.EMPTY

}