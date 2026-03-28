package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category

import dev.aaronhowser.mods.excessive_utilities.recipe.machine.QedRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder

class QedJeiCategory(
	recipeType: RecipeType<RecipeHolder<QedRecipe>>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<RecipeHolder<QedRecipe>>(
	recipeType,
	ModBlocks.QED.get().name,
	guiHelper.createDrawableItemLike(ModBlocks.QED),
	116,
	54
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<QedRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value()

		val pattern = recipe.pattern
		val ingredients = pattern.ingredients()

		val patternWidth = pattern.width()
		val patternHeight = pattern.height()

		val xOffset = (3 - patternWidth) / 2
		val yOffset = (3 - patternHeight) / 2

		for (gridX in 0 until 3) {
			val x = 1 + gridX * 18

			for (gridY in 0 until 3) {
				val y = 1 + gridY * 18

				val slot = builder.addInputSlot(x, y)

				val patternX = gridX - xOffset
				val patternY = gridY - yOffset

				val ingredient =
					if (patternX in 0 until patternWidth && patternY in 0 until patternHeight) {
						val index = patternY * patternWidth + patternX
						ingredients.getOrNull(index)
					} else null

				slot
					.addItemStacks(ingredient?.items?.toList() ?: listOf(ItemStack.EMPTY))
					.setStandardSlotBackground()
			}
		}

		val outputSlot = builder.addOutputSlot(90, 18)
		outputSlot
			.addItemStack(recipe.result)
			.setOutputSlotBackground()
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<QedRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value()

		builder.addRecipeArrow().setPosition(58, 18)

		builder.addText(
			Component.literal("${recipe.crystalTicks} ticks"),
			100, 12
		).setPosition(58, 4).setColor(0xFF808080.toInt())
	}

	override fun getRegistryName(recipe: RecipeHolder<QedRecipe>): ResourceLocation = recipe.id()

}