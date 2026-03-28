package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.ModJeiPlugin
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.ItemAndFluidFuelRecipe
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.level.Level

class ItemAndFluidFuelJeiCategory(
	generatorBlock: GeneratorBlock,
	recipeType: RecipeType<RecipeHolder<ItemAndFluidFuelRecipe>>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<RecipeHolder<ItemAndFluidFuelRecipe>>(
	recipeType,
	generatorBlock.name,
	guiHelper.createDrawableItemLike(generatorBlock),
	120,
	40
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<ItemAndFluidFuelRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value

		val itemInputSlot = builder.addInputSlot(9, 1)

		itemInputSlot
			.addItemStacks(recipe.itemIngredient.items.toList())
			.setStandardSlotBackground()

		val fluidInputSlot = builder.addInputSlot(9, 22)
			.setStandardSlotBackground()
			.setFluidRenderer(1, false, 16, 16)

		val fluidIngredient = recipe.fluidIngredient

		for (fluid in fluidIngredient.fluids) {
			fluidInputSlot
				.addFluidStack(fluid.fluid, fluid.amount.toLong())
		}
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<ItemAndFluidFuelRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value

		val fePerTick = recipe.fePerTick
		val duration = recipe.duration
		val totalFe = fePerTick * duration

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

	override fun getRegistryName(recipe: RecipeHolder<ItemAndFluidFuelRecipe>): ResourceLocation = recipe.id()

	companion object {
		fun registerCategories(registration: IRecipeCategoryRegistration) {
			for ((generatorType, recipeType) in ModJeiPlugin.ITEM_AND_FLUID_FUELS) {
				registration.addRecipeCategories(
					ItemAndFluidFuelJeiCategory(
						generatorType.deferredBlock.get(),
						recipeType,
						registration.jeiHelpers.guiHelper
					)
				)
			}
		}

		fun registerRecipes(level: Level, registration: IRecipeRegistration) {
			for ((generatorType, recipeType) in ModJeiPlugin.ITEM_AND_FLUID_FUELS) {
				val recipes = ItemAndFluidFuelRecipe.getAllRecipesOfType(generatorType, level.recipeManager)
				registration.addRecipes(recipeType, recipes)
			}
		}

		fun registerCatalysts(registration: IRecipeCatalystRegistration) {
			for ((generatorType, recipeType) in ModJeiPlugin.ITEM_AND_FLUID_FUELS) {
				registration.addRecipeCatalyst(generatorType.deferredBlock.get(), recipeType)
			}
		}

	}

}