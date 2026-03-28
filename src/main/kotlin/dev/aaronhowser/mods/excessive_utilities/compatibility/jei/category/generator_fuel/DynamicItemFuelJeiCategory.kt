package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.generator.CulinaryGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.compatibility.jei.ModJeiPlugin
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import mezz.jei.api.runtime.IIngredientManager
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import mezz.jei.api.recipe.RecipeType as JeiRecipeType

// For fuels that aren't based on actual recipes, but based off some property of the itemstack
class DynamicItemFuelJeiCategory(
	generatorBlock: GeneratorBlock,
	recipeType: JeiRecipeType<Recipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<DynamicItemFuelJeiCategory.Recipe>(
	recipeType,
	generatorBlock.name,
	guiHelper.createDrawableItemLike(generatorBlock),
	130,
	40
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: Recipe, focuses: IFocusGroup) {
		val itemInputSlot = builder.addInputSlot(9, 9)
			.setStandardSlotBackground()

		for (stack in recipe.stacks) {
			itemInputSlot.addItemStack(stack)
		}
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: Recipe, focuses: IFocusGroup) {
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
			val guiHelper = registration.jeiHelpers.guiHelper

			registration.addRecipeCategories(
				DynamicItemFuelJeiCategory(
					ModBlocks.CULINARY_GENERATOR.get(),
					ModJeiPlugin.CULINARY_FUELS,
					guiHelper
				),
				DynamicItemFuelJeiCategory(
					ModBlocks.FURNACE_GENERATOR.get(),
					ModJeiPlugin.FURNACE_FUELS,
					guiHelper
				),
				DynamicItemFuelJeiCategory(
					ModBlocks.SURVIVALIST_GENERATOR.get(),
					ModJeiPlugin.SURVIVALIST_FUELS,
					guiHelper
				)
			)
		}

		// Because this uses configs, it'll be outdated if you don't reload after changing the config
		// I could make the config only apply after a reload but that's stupid
		fun registerRecipes(level: Level, registration: IRecipeRegistration) {
			val ingredientManager = registration.jeiHelpers.ingredientManager

			val culinaryRecipes = getCulinaryRecipes(ingredientManager)
			registration.addRecipes(ModJeiPlugin.CULINARY_FUELS, culinaryRecipes)

			val furnaceFePerTick = ServerConfig.CONFIG.furnaceGeneratorFePerTick.get()
			val furnaceBurnTimeMultiplier = ServerConfig.CONFIG.furnaceGeneratorBurnTimeMultiplier.get()

			val survivalistFePerTick = ServerConfig.CONFIG.survivalistGeneratorFePerTick.get()
			val survivalistBurnTimeMultiplier = ServerConfig.CONFIG.survivalistGeneratorBurnTimeMultiplier.get()

			val fuels = getStackFuels(ingredientManager)
			val furnaceRecipes = mutableListOf<Recipe>()
			val survivalistRecipes = mutableListOf<Recipe>()

			for ((burnTicks, stacks) in fuels) {
				furnaceRecipes += Recipe(
					stacks,
					furnaceFePerTick,
					Mth.ceil(burnTicks * furnaceBurnTimeMultiplier)
				)

				survivalistRecipes += Recipe(
					stacks,
					survivalistFePerTick,
					Mth.ceil(burnTicks * survivalistBurnTimeMultiplier)
				)
			}

			registration.addRecipes(ModJeiPlugin.FURNACE_FUELS, furnaceRecipes)
			registration.addRecipes(ModJeiPlugin.SURVIVALIST_FUELS, survivalistRecipes)
		}

		private fun getCulinaryRecipes(ingredientManager: IIngredientManager): List<Recipe> {
			return ingredientManager.allItemStacks
				.asSequence()

				.mapNotNull { itemStack ->
					val values = CulinaryGeneratorBlockEntity.getFeValues(itemStack)

					if (values.first > 0 && values.second > 0) {
						values to itemStack
					} else {
						null
					}
				}
				.groupBy(
					keySelector = { it.first },
					valueTransform = { it.second }
				)
				.map { (values, stacks) ->
					Recipe(
						stacks,
						values.first,
						values.second
					)
				}
				.sortedByDescending(Recipe::feTotal)
				.toList()
		}

		fun getStackFuels(ingredientManager: IIngredientManager): List<Pair<Int, List<ItemStack>>> {
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
				.toList()
				.sortedByDescending { it.first }
		}
	}

	data class Recipe(val stacks: List<ItemStack>, val fePerTick: Int, val duration: Int) {
		val feTotal = fePerTick * duration
	}

}