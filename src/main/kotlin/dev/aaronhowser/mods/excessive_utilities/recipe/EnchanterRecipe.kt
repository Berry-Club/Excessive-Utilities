package dev.aaronhowser.mods.excessive_utilities.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class EnchanterRecipe(
	val leftIngredient: Ingredient,
	val rightIngredient: Ingredient,
	val feCost: Int,
	val ticks: Int,
	private val result: ItemStack
) : Recipe<EnchanterRecipe.Input> {

	override fun matches(input: Input, level: Level): Boolean {
		return leftIngredient.test(input.left) && rightIngredient.test(input.right)
	}

	override fun assemble(input: Input, registries: HolderLookup.Provider): ItemStack = result.copy()
	override fun canCraftInDimensions(width: Int, height: Int): Boolean = true
	override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.copy()

	override fun getSerializer(): RecipeSerializer<*> {
		TODO("Not yet implemented")
	}

	override fun getType(): RecipeType<*> {
		TODO("Not yet implemented")
	}

	class Input(
		val left: ItemStack,
		val right: ItemStack
	) : RecipeInput {
		override fun size(): Int = 2

		override fun getItem(index: Int): ItemStack = when (index) {
			0 -> left
			1 -> right
			else -> ItemStack.EMPTY
		}
	}

}