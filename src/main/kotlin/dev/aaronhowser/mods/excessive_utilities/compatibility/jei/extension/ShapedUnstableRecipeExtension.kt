package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.extension

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.item.UnstableIngotItem
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.ShapedUnstableRecipe
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

class ShapedUnstableRecipeExtension : ICraftingCategoryExtension<ShapedUnstableRecipe> {

	override fun setRecipe(
		recipeHolder: RecipeHolder<ShapedUnstableRecipe>,
		builder: IRecipeLayoutBuilder,
		craftingGridHelper: ICraftingGridHelper,
		focuses: IFocusGroup
	) {
		val recipe = recipeHolder.value
		craftingGridHelper.createAndSetOutputs(builder, recipe.display().first().result())
		craftingGridHelper.createAndSetIngredientsFromDisplays(builder, getIngredients(recipeHolder), getWidth(recipeHolder), getHeight(recipeHolder))
	}

	override fun getIngredients(recipeHolder: RecipeHolder<ShapedUnstableRecipe>): List<SlotDisplay> =
		getDisplayIngredients(recipeHolder.value)
	override fun getWidth(recipeHolder: RecipeHolder<ShapedUnstableRecipe>): Int = recipeHolder.value.getWidth()
	override fun getHeight(recipeHolder: RecipeHolder<ShapedUnstableRecipe>): Int = recipeHolder.value.getHeight()

	override fun isHandled(recipeHolder: RecipeHolder<ShapedUnstableRecipe>): Boolean {
		return recipeHolder.value.isSpecial
	}

    private fun getDisplayIngredients(recipe: ShapedUnstableRecipe): List<SlotDisplay> {
        return recipe.getIngredients().map { optional ->
            if (optional.isEmpty) SlotDisplay.Empty.INSTANCE else {
                val ingredient = optional.orElseThrow()
                val stacks = ingredient.getItemHolders().map(::ItemStack).toList()
                if (stacks.none { it.isItem(ModItems.UNSTABLE_INGOT) }) ingredient.display() else {
                    val variants = stacks.flatMap { stack ->
                        if (!stack.isItem(ModItems.UNSTABLE_INGOT)) listOf(stack) else when (recipe.requiredStability) {
                            ShapedUnstableRecipe.Stability.STABLE -> listOf(stableIngot())
                    ShapedUnstableRecipe.Stability.UNSTABLE -> listOf(unstableIngot())
                    ShapedUnstableRecipe.Stability.EITHER -> listOf(stableIngot(), unstableIngot())
                        }
                    }
                    SlotDisplay.Composite(variants.map { SlotDisplay.ItemStackSlotDisplay(ItemStackTemplate.fromNonEmptyStack(it)) })
                }
            }
        }
    }

	private fun stableIngot(): ItemStack {
		return ModItems.UNSTABLE_INGOT.toStack()
	}

	private fun unstableIngot(): ItemStack {
		val stack = stableIngot()
		stack.set(ModDataComponents.COUNTDOWN, UnstableIngotItem.MAX_COUNTDOWN)
		stack.set(ModDataComponents.CRAFTED_IN_MENU, UnstableIngotItem.VANILLA_CRAFTING_MENU_ID)
		return stack
	}

}
