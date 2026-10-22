package dev.aaronhowser.mods.excessive_utilities.recipe.base

import net.minecraft.world.item.crafting.PlacementInfo
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeBookCategories
import net.minecraft.world.item.crafting.RecipeBookCategory
import net.minecraft.world.item.crafting.RecipeInput

/** Machine recipes are displayed by the machine integration rather than the crafting book. */
interface MachineRecipe<T : RecipeInput> : Recipe<T> {
    override fun showNotification(): Boolean = false
    override fun group(): String = ""
    override fun placementInfo(): PlacementInfo = PlacementInfo.NOT_PLACEABLE
    override fun recipeBookCategory(): RecipeBookCategory = RecipeBookCategories.CRAFTING_MISC
}
