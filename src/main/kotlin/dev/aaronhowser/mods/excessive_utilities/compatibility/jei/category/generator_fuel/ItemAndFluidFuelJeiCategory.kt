package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.ModJeiPlugin
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.ItemAndFluidFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.getItemHolders
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.types.IRecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.resources.Identifier
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ItemAndFluidFuelJeiCategory(
	generatorBlock: GeneratorBlock,
	recipeType: IRecipeType<RecipeHolder<ItemAndFluidFuelRecipe>>,
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
			.addItemStacks(recipe.itemIngredient.getItemHolders().map(::ItemStack).toList())
			.setStandardSlotBackground()

		val fluidInputSlot = builder.addInputSlot(9, 22)
			.setStandardSlotBackground()
			.setFluidRenderer(1, false, 16, 16)

		val fluidIngredient = recipe.fluidIngredient

		for (fluid in fluidIngredient.ingredient().fluids()) {
			fluidInputSlot
				.add(fluid.value(), fluidIngredient.amount().toLong())
		}
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<ItemAndFluidFuelRecipe>, focuses: IFocusGroup) {
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

	override fun getIdentifier(recipe: RecipeHolder<ItemAndFluidFuelRecipe>): Identifier = recipe.id.identifier()

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
				val recipes = ItemAndFluidFuelRecipe.getAllRecipesOfType(generatorType, dev.aaronhowser.mods.excessive_utilities.recipe.base.ModRecipes.recipes(level))
				registration.addRecipes(recipeType, recipes)
			}
		}

		fun registerCatalysts(registration: IRecipeCatalystRegistration) {
			for ((generatorType, recipeType) in ModJeiPlugin.ITEM_AND_FLUID_FUELS) {
				registration.addCraftingStation(recipeType, generatorType.deferredBlock.get())
			}
		}

	}

}
