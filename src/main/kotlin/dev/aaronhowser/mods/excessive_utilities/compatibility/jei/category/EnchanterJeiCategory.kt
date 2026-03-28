package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category

import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMessageLang
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.EnchanterRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

class EnchanterJeiCategory(
	recipeType: RecipeType<RecipeHolder<EnchanterRecipe>>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<RecipeHolder<EnchanterRecipe>>(
	recipeType,
	ModBlocks.ENCHANTER.get().name,
	guiHelper.createDrawableItemLike(ModBlocks.ENCHANTER),
	120,
	40
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<EnchanterRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value()

		val leftInputSlot = builder.addInputSlot(9, 9)
		val rightInputSlot = builder.addInputSlot(33, 9)
		val outputSlot = builder.addOutputSlot(90, 9)

		val inputs = recipe.leftIngredient.items.map { it.copyWithCount(recipe.leftCount) }
		leftInputSlot
			.addItemStacks(inputs)
			.setStandardSlotBackground()

		val rightInputs = recipe.rightIngredient.items.map { it.copyWithCount(recipe.rightCount) }
		rightInputSlot
			.addItemStacks(rightInputs)
			.setStandardSlotBackground()

		outputSlot
			.addItemStack(recipe.output)
			.setOutputSlotBackground()
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<EnchanterRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value()

		builder.addRecipeArrow().setPosition(55, 9)

		val formattedFePerTick = "%,d".format(recipe.fePerTick)
		val formattedTicks = "%,d".format(recipe.ticks)

		builder.addText(
			ModMenuLang.FE_PER_TICK.toComponent(formattedFePerTick),
			80, 12
		).setPosition(9, 32).setColor(0xFF808080.toInt())

		builder.addText(
			ModMenuLang.TICKS.toComponent(formattedTicks),
			80, 12
		).setPosition(65, 32).setColor(0xFF808080.toInt())
	}

	override fun getRegistryName(recipe: RecipeHolder<EnchanterRecipe>): ResourceLocation = recipe.id

}