package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.ModJeiPlugin
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel.DynamicItemFuelJeiCategory.Companion.getStackFuels
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class OverclockedFuelJeiCategory(
	recipeType: RecipeType<Recipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<OverclockedFuelJeiCategory.Recipe>(
	recipeType,
	ModBlocks.OVERCLOCKED_GENERATOR.get().name,
	guiHelper.createDrawableItemLike(ModBlocks.OVERCLOCKED_GENERATOR),
	130,
	20
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: Recipe, focuses: IFocusGroup) {
		val itemInputSlot = builder.addInputSlot(9, 1)
			.setStandardSlotBackground()

		for (stack in recipe.stacks) {
			itemInputSlot.addItemStack(stack)
		}
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: Recipe, focuses: IFocusGroup) {
		val totalFe = recipe.totalFe
		val totalFeString = "%,d".format(totalFe)

		builder.addText(
			Component.literal("$totalFeString FE total"),
			200, 12
		).setPosition(32, 5).setColor(0xFF808080.toInt())
	}

	companion object {
		fun registerRecipes(level: Level, registration: IRecipeRegistration) {
			val ingredientManager = registration.jeiHelpers.ingredientManager

			val fuels = getStackFuels(ingredientManager)
			val recipes = mutableListOf<Recipe>()

			val fePerBurnTick = ServerConfig.CONFIG.overclockedGeneratorFePerBurnTick.get()

			for ((burnTicks, stacks) in fuels) {
				val recipe = Recipe(stacks, burnTicks * fePerBurnTick)
				recipes += recipe
			}

			registration.addRecipes(ModJeiPlugin.OVERCLOCKED_FUELS, recipes)
		}
	}

	data class Recipe(val stacks: List<ItemStack>, val totalFe: Int)

}