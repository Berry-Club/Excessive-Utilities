package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.block_entity.generator.PotionGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
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
import net.minecraft.world.item.Items
import net.minecraft.world.item.alchemy.PotionContents
import net.minecraft.world.item.alchemy.Potions
import net.minecraft.world.level.Level

class PotionFuelJeiCategory(
	recipeType: RecipeType<Recipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<PotionFuelJeiCategory.Recipe>(
	recipeType,
	ModBlocks.POTION_GENERATOR.get().name,
	guiHelper.createDrawableItemLike(ModBlocks.POTION_GENERATOR),
	130,
	30
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: Recipe, focuses: IFocusGroup) {
		val itemInputSlot = builder.addInputSlot(9, 8)
			.setStandardSlotBackground()

		for (stack in recipe.stacks) {
			itemInputSlot.addItemStack(stack)
		}
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: Recipe, focuses: IFocusGroup) {
		val steps = recipe.steps

		if (steps > 0) {
			builder.addText(
				Component.literal("$steps Brewing Steps"),
				200, 12
			).setPosition(32, 5).setColor(0xFF808080.toInt())
		}

		val totalFe = recipe.totalFe
		val formattedFeTotal = "%,d".format(totalFe)

		val totalFeY = if (steps > 0) 17 else 12

		builder.addText(
			ModMenuLang.FE.toComponent(formattedFeTotal),
			200, 12
		).setPosition(32, totalFeY).setColor(0xFF808080.toInt())
	}

	companion object {
		fun getAllRecipes(level: Level, registration: IRecipeRegistration): List<Recipe> {
			val most = registration.jeiHelpers
				.ingredientManager
				.allItemStacks
				.asSequence()
				.mapNotNull { stack ->
					val steps = PotionGeneratorBlockEntity.getBrewingSteps(level, stack)

					if (steps <= 0) {
						null
					} else {
						steps to stack
					}
				}
				.groupBy(
					keySelector = { it.first },
					valueTransform = { it.second }
				)
				.map { (steps, stacks) ->
					val fe = PotionGeneratorBlockEntity.getPowerFromSteps(steps)
					Recipe(stacks, steps, fe)
				}
				.sortedByDescending(Recipe::totalFe)
				.toList()

			val waterPotion = PotionContents.createItemStack(Items.POTION, Potions.WATER)
			val waterRecipe = Recipe(listOf(waterPotion), 0, 10)

			return most + waterRecipe
		}
	}

	data class Recipe(val stacks: List<ItemStack>, val steps: Int, val totalFe: Int)
}