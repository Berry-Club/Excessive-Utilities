package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.ModJeiPlugin
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import mezz.jei.api.recipe.RecipeType as JeiRecipeType

class BasicFurnaceGeneratorFuelJeiCategory(
	block: GeneratorBlock,
	recipeType: JeiRecipeType<Recipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<BasicFurnaceGeneratorFuelJeiCategory.Recipe>(
	recipeType,
	block.name,
	guiHelper.createDrawableItemLike(block),
	120,
	40
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: Recipe, focuses: IFocusGroup) {
		val itemInputSlot = builder.addInputSlot(9, 9)
			.setStandardSlotBackground()

		for (stack in recipe.stacks) {
			itemInputSlot.addItemStack(stack)
		}
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: CulinaryFuelJeiCategory.Recipe, focuses: IFocusGroup) {
		val fePerTick = recipe.fePerTick
		val duration = recipe.duration
		val totalFe = recipe.feTotal

		val perTickString = "%,d".format(fePerTick)
		val durationString = "%,d".format(duration)
		val totalFeString = "%,d".format(totalFe)

		builder.addText(
			Component.literal("$perTickString FE/tick"),
			200, 12
		).setPosition(32, 1).setColor(0xFF808080.toInt())

		builder.addText(
			Component.literal("$durationString ticks"),
			200, 12
		).setPosition(32, 16).setColor(0xFF808080.toInt())

		builder.addText(
			Component.literal("$totalFeString FE total"),
			200, 12
		).setPosition(32, 31).setColor(0xFF808080.toInt())
	}

	companion object {
		fun registerCategories(registration: IRecipeCategoryRegistration) {
			registration.addRecipeCategories(
				BasicFurnaceGeneratorFuelJeiCategory(
					ModBlocks.FURNACE_GENERATOR.get(),
					ModJeiPlugin.FURNACE_GENERATOR_FUELS,
					registration.jeiHelpers.guiHelper
				)
			)
		}

		fun getStackFuels(ingredientManager: IIngredientManager): Map<Int, List<ItemStack>> {
			return ingredientManager.allItemStacks
				.asSequence()
				.mapNotNull { stack ->
					val burnTime = stack.getBurnTime(RecipeType.SMELTING)
					if (burnTime > 0) {
						burnTime to stack
					} else {
						null
					}
				}
				.groupBy(
					keySelector = { it.first },
					valueTransform = { it.second }
				)
				.toMap()
		}
	}

	data class Recipe(val stacks: List<ItemStack>, val fePerTick: Int, val duration: Int) {
		val feTotal = fePerTick * duration
	}

}