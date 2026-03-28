package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category

import dev.aaronhowser.mods.excessive_utilities.recipe.machine.ResonatorRecipe
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
import java.text.DecimalFormat

class ResonatorJeiCategory(
	recipeType: RecipeType<ResonatorRecipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<ResonatorRecipe>(
	recipeType,
	ModBlocks.RESONATOR.get().name,
	guiHelper.createDrawableItemLike(ModBlocks.RESONATOR),
	82,
	34
) {

	private val slot: IDrawableStatic = guiHelper.slotDrawable

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: ResonatorRecipe, focuses: IFocusGroup) {
		val inputSlot = builder.addInputSlot(9, 9)
		val outputSlot = builder.addOutputSlot(61, 9)

		inputSlot.addItemStacks(recipe.ingredient.items.toList())
		outputSlot.addItemStack(recipe.result)
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: ResonatorRecipe, focuses: IFocusGroup) {
		builder.addRecipeArrow().setPosition(32, 9)

		val gpCost = DecimalFormat("#,##0.##").format(recipe.gpCost)

		builder.addText(
			Component.literal("$gpCost GP"),
			50, 12
		).setPosition(32, 0).setColor(0xFF808080.toInt())
	}

	override fun draw(recipe: ResonatorRecipe, recipeSlotsView: IRecipeSlotsView, guiGraphics: GuiGraphics, mouseX: Double, mouseY: Double) {
		slot.draw(guiGraphics, 8, 8)
		slot.draw(guiGraphics, 60, 8)
	}

}