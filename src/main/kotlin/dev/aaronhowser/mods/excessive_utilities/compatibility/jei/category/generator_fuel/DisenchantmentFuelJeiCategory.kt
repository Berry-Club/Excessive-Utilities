package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.generator.DisenchantmentGeneratorBlockEntity
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
import net.minecraft.world.level.Level

class DisenchantmentFuelJeiCategory(
	recipeType: RecipeType<Recipe>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<DisenchantmentFuelJeiCategory.Recipe>(
	recipeType,
	ModBlocks.DISENCHANTMENT_GENERATOR.get().name,
	guiHelper.createDrawableItemLike(ModBlocks.DISENCHANTMENT_GENERATOR),
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
			Component.literal("$totalFeString FE"),
			200, 12
		).setPosition(32, 5).setColor(0xFF808080.toInt())
	}

	companion object {
		fun getAllRecipes(level: Level, registration: IRecipeRegistration): List<Recipe> {
			return registration.jeiHelpers
				.ingredientManager
				.allItemStacks
				.asSequence()
				.mapNotNull { stack ->
					val power = DisenchantmentGeneratorBlockEntity.getPowerFromStack(level, stack)
					if (stack.isItem(Items.ENCHANTED_BOOK)) {
						println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa")
					}

					if (power <= 0) {
						null
					} else {
						power to stack
					}
				}
				.groupBy(
					keySelector = { it.first },
					valueTransform = { it.second }
				)
				.map { (fe, stacks) -> Recipe(stacks, fe) }
				.sortedByDescending(Recipe::totalFe)
				.toList()
		}
	}

	data class Recipe(val stacks: List<ItemStack>, val totalFe: Int)

}