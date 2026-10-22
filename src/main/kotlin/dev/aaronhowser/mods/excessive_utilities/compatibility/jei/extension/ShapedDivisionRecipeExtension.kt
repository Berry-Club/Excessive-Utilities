package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.extension

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withComponent
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.withoutComponent
import dev.aaronhowser.mods.excessive_utilities.item.DivisionSigilItem
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.ShapedDivisionRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.getItemHolders
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.ingredient.ICraftingGridHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.category.extensions.vanilla.crafting.ICraftingCategoryExtension
import net.minecraft.world.item.crafting.display.SlotDisplay
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.ItemStackTemplate

class ShapedDivisionRecipeExtension : ICraftingCategoryExtension<ShapedDivisionRecipe> {

	override fun setRecipe(
		recipeHolder: RecipeHolder<ShapedDivisionRecipe>,
		builder: IRecipeLayoutBuilder,
		craftingGridHelper: ICraftingGridHelper,
		focuses: IFocusGroup
	) {
		val recipe = recipeHolder.value
		craftingGridHelper.createAndSetOutputs(builder, recipe.display().first().result())
		craftingGridHelper.createAndSetIngredientsFromDisplays(builder, getIngredients(recipeHolder), getWidth(recipeHolder), getHeight(recipeHolder))
	}

	override fun getIngredients(recipeHolder: RecipeHolder<ShapedDivisionRecipe>): List<SlotDisplay> =
		getDisplayIngredients(recipeHolder.value)

	override fun getWidth(recipeHolder: RecipeHolder<ShapedDivisionRecipe>): Int = recipeHolder.value.getWidth()
	override fun getHeight(recipeHolder: RecipeHolder<ShapedDivisionRecipe>): Int = recipeHolder.value.getHeight()

	override fun isHandled(recipeHolder: RecipeHolder<ShapedDivisionRecipe>): Boolean {
		return recipeHolder.value.isSpecial
	}

    private fun getDisplayIngredients(recipe: ShapedDivisionRecipe): List<SlotDisplay> {
        return recipe.getIngredients().map { optional ->
            if (optional.isEmpty) SlotDisplay.Empty.INSTANCE else {
                val ingredient = optional.orElseThrow()
                val stacks = ingredient.getItemHolders().map(::ItemStack).toList()
                if (stacks.none { it.isItem(ModItems.DIVISION_SIGIL) }) ingredient.display() else {
                    val variants = stacks.flatMap { stack ->
                        if (!stack.isItem(ModItems.DIVISION_SIGIL)) listOf(stack) else when (recipe.requiredInversion) {
                            ShapedDivisionRecipe.Inversion.INVERTED -> listOf(inverted())
                    ShapedDivisionRecipe.Inversion.UNINVERTED -> listOf(uninverted())
                    ShapedDivisionRecipe.Inversion.EITHER -> listOf(uninverted(), inverted())
                        }
                    }
                    SlotDisplay.Composite(variants.map { SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(it)) })
                }
            }
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
