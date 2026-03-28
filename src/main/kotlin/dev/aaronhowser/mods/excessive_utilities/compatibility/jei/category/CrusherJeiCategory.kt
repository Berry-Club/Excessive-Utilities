package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category

import dev.aaronhowser.mods.excessive_utilities.recipe.machine.CrusherRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

class CrusherJeiCategory(
	recipeType: RecipeType<RecipeHolder<CrusherRecipe>>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<RecipeHolder<CrusherRecipe>>(
	recipeType,
	ModBlocks.CRUSHER.get().name,
	guiHelper.createDrawableItemLike(ModBlocks.CRUSHER),
	120,
	40
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<CrusherRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value()

		val inputSlot = builder.addInputSlot(9, 9)
		val primaryOutputSlot = builder.addOutputSlot(63, 9)

		inputSlot
			.addItemStacks(recipe.ingredient.items.toList())
			.setStandardSlotBackground()

		primaryOutputSlot
			.addItemStack(recipe.getPrimaryOutput())
			.setOutputSlotBackground()

		if (recipe.secondaryChance > 0) {
			val secondaryOutputSlot = builder.addOutputSlot(95, 9)

			secondaryOutputSlot
				.addItemStack(recipe.getSecondaryOutput())
				.setOutputSlotBackground()
		}

	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<CrusherRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value()

		builder.addRecipeArrow().setPosition(31, 9)

		if (recipe.secondaryChance > 0) {
			builder.addText(
				Component.literal("${recipe.secondaryChance}%"),
				80, 12
			).setPosition(95, 32).setColor(0xFF808080.toInt())
		}

	}

	override fun getRegistryName(recipe: RecipeHolder<CrusherRecipe>): ResourceLocation = recipe.id

}