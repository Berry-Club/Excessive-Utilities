package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.ModJeiPlugin
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.SingleFluidFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.SingleItemFuelRecipe
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
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.iterator

class SingleFluidFuelJeiCategory(
	generatorBlock: GeneratorBlock,
	recipeType: RecipeType<RecipeHolder<SingleFluidFuelRecipe>>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<RecipeHolder<SingleFluidFuelRecipe>>(
	recipeType,
	generatorBlock.name,
	guiHelper.createDrawableItemLike(generatorBlock),
	120,
	40
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<SingleFluidFuelRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value

		val inputSlot = builder.addInputSlot(9, 9)
			.setStandardSlotBackground()

		val fluidIngredient = recipe.fluidIngredient

		for (fluid in fluidIngredient.fluids) {
			inputSlot
				.addFluidStack(fluid.fluid, fluid.amount.toLong())
		}
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<SingleFluidFuelRecipe>, focuses: IFocusGroup) {
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

	override fun getRegistryName(recipe: RecipeHolder<SingleFluidFuelRecipe>): ResourceLocation = recipe.id()

	companion object {
		fun registerCategory(registration: IRecipeCategoryRegistration) {
			for ((generatorType, recipeType) in ModJeiPlugin.SINGLE_ITEM_FUELS) {
				registration.addRecipeCategories(
					SingleItemFuelJeiCategory(
						generatorType.deferredBlock.get(),
						recipeType,
						registration.jeiHelpers.guiHelper
					)
				)
			}
		}

		fun registerRecipes(level: Level, registration: IRecipeRegistration) {
			for ((generatorType, recipeType) in ModJeiPlugin.SINGLE_ITEM_FUELS) {
				val recipes = SingleItemFuelRecipe.getAllRecipesOfType(generatorType, level.recipeManager)
				registration.addRecipes(recipeType, recipes)
			}
		}

		fun registerCatalysts(registration: IRecipeCatalystRegistration) {
			for ((generatorType, recipeType) in ModJeiPlugin.SINGLE_ITEM_FUELS) {
				registration.addRecipeCatalyst(generatorType.deferredBlock.get(), recipeType)
			}
		}
	}

}