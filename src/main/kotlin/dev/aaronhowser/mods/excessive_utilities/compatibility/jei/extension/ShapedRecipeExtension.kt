package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.extension

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.ICraftingGridHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.ShapedRecipe
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.CraftingRecipe

class ShapedRecipeExtension<T : CraftingRecipe> : ICraftingCategoryExtension<T> {

	override fun setRecipe(
		recipeHolder: RecipeHolder<T>,
		builder: IRecipeLayoutBuilder,
		craftingGridHelper: ICraftingGridHelper,
		focuses: IFocusGroup
	) {
		val registry = AaronClientUtil.localLevel?.registryAccess() ?: return

		val recipe = recipeHolder.value
		craftingGridHelper.createAndSetOutputs(builder, recipe.display().first().result())
		craftingGridHelper.createAndSetIngredientsFromDisplays(builder, getIngredients(recipeHolder), getWidth(recipeHolder), getHeight(recipeHolder))
	}

	override fun getIngredients(recipeHolder: RecipeHolder<T>): List<SlotDisplay> =
		recipeHolder.value.placementInfo().ingredients().map { Ingredient.optionalIngredientToDisplay(java.util.Optional.of(it)) }

	override fun getWidth(recipeHolder: RecipeHolder<T>): Int =
		(recipeHolder.value as? ShapedRecipe)?.width ?: 0

	override fun getHeight(recipeHolder: RecipeHolder<T>): Int =
		(recipeHolder.value as? ShapedRecipe)?.height ?: 0

	override fun isHandled(recipeHolder: RecipeHolder<T>): Boolean {
		return recipeHolder.value.isSpecial
	}

}
