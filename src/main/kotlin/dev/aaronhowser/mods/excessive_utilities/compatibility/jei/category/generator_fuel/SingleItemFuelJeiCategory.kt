package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.ModJeiPlugin
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
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

class SingleItemFuelJeiCategory(
	generatorBlock: GeneratorBlock,
	recipeType: RecipeType<RecipeHolder<SingleItemFuelRecipe>>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<RecipeHolder<SingleItemFuelRecipe>>(
	recipeType,
	generatorBlock.name,
	guiHelper.createDrawableItemLike(generatorBlock),
	120,
	40
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<SingleItemFuelRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value

		val inputSlot = builder.addInputSlot(9, 9)

		inputSlot
			.addItemStacks(recipe.ingredient.items.toList())
			.setStandardSlotBackground()
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<SingleItemFuelRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value

		val fePerTick = recipe.fePerTick
		val duration = recipe.duration
		val totalFe = fePerTick * duration

		val formattedFePerTick = "%,d".format(fePerTick)
		val formattedDuration = "%,d".format(duration)
		val formattedFeTotal = "%,d".format(totalFe)

		builder.addText(
			ModMenuLang.FE_PER_TICK.toComponent(formattedFePerTick),
			200, 12
		).setPosition(32, 1).setColor(0xFF808080.toInt())

		builder.addText(
			ModMenuLang.TICKS.toComponent(formattedDuration),
			200, 12
		).setPosition(32, 16).setColor(0xFF808080.toInt())

		builder.addText(
			ModMenuLang.FE.toComponent(formattedFeTotal),
			200, 12
		).setPosition(32, 31).setColor(0xFF808080.toInt())

	}

	override fun getRegistryName(recipe: RecipeHolder<SingleItemFuelRecipe>): ResourceLocation = recipe.id()

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