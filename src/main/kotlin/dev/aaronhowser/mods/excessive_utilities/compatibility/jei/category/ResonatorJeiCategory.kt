package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category

import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.ClientGridPower
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.ResonatorRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.world.item.crafting.RecipeHolder

class ResonatorJeiCategory(
	recipeType: RecipeType<RecipeHolder<ResonatorRecipe>>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<RecipeHolder<ResonatorRecipe>>(
	recipeType,
	ModBlocks.RESONATOR.get().name,
	guiHelper.createDrawableItemLike(ModBlocks.RESONATOR),
	94,
	34
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<ResonatorRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value()

		val inputSlot = builder.addInputSlot(9, 9)
		val outputSlot = builder.addOutputSlot(65, 9)

		inputSlot
			.addItemStacks(recipe.ingredient.items.toList())
			.setStandardSlotBackground()

		outputSlot
			.addItemStack(recipe.result)
			.setOutputSlotBackground()
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<ResonatorRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value()

		builder.addRecipeArrow().setPosition(32, 9)

		val formattedGpCost = ClientGridPower.format(recipe.gpCost)

		builder.addText(
			ModMenuLang.GP.toComponent(formattedGpCost),
			50, 12
		).setPosition(32, 0).setColor(0xFF808080.toInt())
	}

}