package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.extension

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withComponent
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withoutComponent
import dev.aaronhowser.mods.excessive_utilities.item.DivisionSigilItem
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.ShapedDivisionRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.ICraftingGridHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder

class ShapedDivisionRecipeExtension : ICraftingCategoryExtension<ShapedDivisionRecipe> {

	override fun setRecipe(
		recipeHolder: RecipeHolder<ShapedDivisionRecipe>,
		builder: IRecipeLayoutBuilder,
		craftingGridHelper: ICraftingGridHelper,
		focuses: IFocusGroup
	) {
		val registry = AaronClientUtil.localLevel?.registryAccess() ?: return

		val recipe = recipeHolder.value
		val result = recipe.getResultItem(registry)

		craftingGridHelper.createAndSetOutputs(builder, listOf(result))
		craftingGridHelper.createAndSetIngredients(builder, getDisplayIngredients(recipe), recipe.width, recipe.height)
	}

	override fun isHandled(recipeHolder: RecipeHolder<ShapedDivisionRecipe>): Boolean {
		return recipeHolder.value.isSpecial
	}

	private fun getDisplayIngredients(recipe: ShapedDivisionRecipe): List<Ingredient> {
		return recipe.ingredients.map { ingredient ->
			val stacks = ingredient.items
			if (stacks.none { it.isItem(ModItems.DIVISION_SIGIL) }) return@map ingredient

			Ingredient.of(
				stacks.flatMap { stack ->
					if (!stack.isItem(ModItems.DIVISION_SIGIL)) return@flatMap listOf(stack)

					when (recipe.requiredInversion) {
						ShapedDivisionRecipe.Inversion.INVERTED -> listOf(inverted())
						ShapedDivisionRecipe.Inversion.UNINVERTED -> listOf(uninverted())
						ShapedDivisionRecipe.Inversion.EITHER -> listOf(uninverted(), inverted())
					}
				}.stream()
			)
		}
	}

	private fun uninverted(): ItemStack {
		return ModItems.DIVISION_SIGIL
			.withComponent(ModDataComponents.REMAINING_USES.get(), DivisionSigilItem.USES_AFTER_ACTIVATION)
	}

	private fun inverted(): ItemStack {
		return ModItems.DIVISION_SIGIL
			.withoutComponent(ModDataComponents.REMAINING_USES.get())
	}

}
