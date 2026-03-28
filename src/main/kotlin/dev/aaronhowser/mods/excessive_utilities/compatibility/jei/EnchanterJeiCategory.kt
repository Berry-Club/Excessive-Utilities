package dev.aaronhowser.mods.excessive_utilities.compatibility.jei

import dev.aaronhowser.mods.excessive_utilities.recipe.machine.EnchanterRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.drawable.IDrawableStatic
import mezz.jei.api.gui.ingredient.IRecipeSlotsView
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

class EnchanterJeiCategory(
	recipeType: RecipeType<EnchanterRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<EnchanterRecipe>(
	recipeType,
	ModBlocks.ENCHANTER.get().name,
	guiHelper.createDrawableItemLike(ModBlocks.ENCHANTER),
	110,
	40
) {

	private val slot: IDrawableStatic = guiHelper.slotDrawable

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: EnchanterRecipe, focuses: IFocusGroup) {
		val leftInputSlot = builder.addInputSlot(9, 9)
		val rightInputSlot = builder.addInputSlot(33, 9)
		val outputSlot = builder.addOutputSlot(80, 9)

		val inputs = recipe.leftIngredient.items.map { it.copyWithCount(recipe.leftCount) }
		leftInputSlot.addItemStacks(inputs)

		val rightInputs = recipe.rightIngredient.items.map { it.copyWithCount(recipe.rightCount) }
		rightInputSlot.addItemStacks(rightInputs)

		outputSlot.addItemStack(recipe.output)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: EnchanterRecipe, focuses: IFocusGroup) {
		builder.addRecipeArrow().setPosition(55, 9)

		builder.addText(
			Component.literal("${recipe.fePerTick} FE/t"),
			80, 12
		).setPosition(9, 32).setColor(0xFF808080.toInt())

		builder.addText(
			Component.literal("${recipe.ticks} ticks"),
			80, 12
		).setPosition(60, 32).setColor(0xFF808080.toInt())
	}

	override fun draw(recipe: EnchanterRecipe, recipeSlotsView: IRecipeSlotsView, guiGraphics: GuiGraphics, mouseX: Double, mouseY: Double) {
		slot.draw(guiGraphics, 8, 8)
		slot.draw(guiGraphics, 32, 8)
		slot.draw(guiGraphics, 79, 8)
	}

}