package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.block_entity.generator.DisenchantmentGeneratorBlockEntity
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
import net.minecraft.core.registries.Registries
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
	100,
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
		val formattedFe = "%,d".format(totalFe)

		builder.addText(
			ModMenuLang.FE.toComponent(formattedFe),
			200, 12
		).setPosition(32, 5).setColor(0xFF808080.toInt())
	}

	companion object {
		fun getAllRecipes(level: Level, registration: IRecipeRegistration): List<Recipe> {
			val lookup = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
			val allEnchantments = lookup.listElements()

			val recipes = mutableListOf<Recipe>()

			for (enchantment in allEnchantments) {
				for (enchantLevel in 1..enchantment.value().maxLevel) {
					val stack = ItemStack(Items.ENCHANTED_BOOK)
					stack.enchant(enchantment, enchantLevel)

					val power = DisenchantmentGeneratorBlockEntity.getPowerFromEnchantment(enchantment.value(), enchantLevel)

					if (power > 0) {
						recipes.add(Recipe(listOf(stack), power))
					}
				}
			}

			return recipes
		}
	}

	data class Recipe(val stacks: List<ItemStack>, val totalFe: Int)

}