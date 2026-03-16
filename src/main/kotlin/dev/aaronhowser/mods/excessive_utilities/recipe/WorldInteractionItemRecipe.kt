package dev.aaronhowser.mods.excessive_utilities.recipe

import dev.aaronhowser.mods.aaron.recipe.block_state_ingredient.BlockStateIngredient
import net.minecraft.core.HolderLookup
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeInput
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class WorldInteractionItemRecipe(
	val input: Input,
	val requiredOnBlock: BlockStateIngredient?,
	val requiredAdjacentBlocks: List<BlockStateIngredient>,
	val requiredBlockBehind: BlockStateIngredient?,
	val output: ItemStack
) : Recipe<WorldInteractionItemRecipe.Input> {

	override fun matches(input: Input, level: Level): Boolean {
		if (requiredOnBlock != null && !requiredOnBlock.test(input.onBlock)) {
			return false
		}

		for (requiredAdjacent in requiredAdjacentBlocks) {
			var foundMatch = false
			for (adjacent in input.adjacentBlocks) {
				if (requiredAdjacent.test(adjacent)) {
					foundMatch = true
					break
				}
			}

			if (!foundMatch) {
				return false
			}
		}

		return !(requiredBlockBehind != null && !requiredBlockBehind.test(input.blockBehind))
	}

	override fun assemble(input: Input, registries: HolderLookup.Provider): ItemStack {
		return output.copy()
	}

	override fun canCraftInDimensions(width: Int, height: Int): Boolean {
		return true
	}

	override fun getResultItem(registries: HolderLookup.Provider): ItemStack {
		return output
	}

	override fun getSerializer(): RecipeSerializer<*> {
		TODO("Not yet implemented")
	}

	override fun getType(): RecipeType<*> {
		TODO("Not yet implemented")
	}

	class Input(
		val onBlock: BlockState,
		val adjacentBlocks: List<BlockState>,
		val blockBehind: BlockState
	) : RecipeInput {
		override fun getItem(index: Int): ItemStack = ItemStack.EMPTY
		override fun size(): Int = 0
	}


}