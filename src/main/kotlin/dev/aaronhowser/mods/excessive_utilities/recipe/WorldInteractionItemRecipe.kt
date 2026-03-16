package dev.aaronhowser.mods.excessive_utilities.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.recipe.base.BlockStateIngredient
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import java.util.*

class WorldInteractionItemRecipe(
	val optionalRequiredOnBlock: Optional<BlockStateIngredient>,
	val requiredAdjacentBlocks: List<BlockStateIngredient>,
	val optionalRequiredBlockBehind: Optional<BlockStateIngredient>,
	val output: ItemStack
) : Recipe<WorldInteractionItemRecipe.Input> {

	val requiredOnBlock: BlockStateIngredient? = optionalRequiredOnBlock.orElse(null)
	val requiredBlockBehind: BlockStateIngredient? = optionalRequiredBlockBehind.orElse(null)

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

	override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.WORLD_INTERACTION_ITEM.get()
	override fun getType(): RecipeType<*> = ModRecipeTypes.WORLD_INTERACTION_ITEM.get()

	companion object {
		fun getRecipe(
			level: Level,
			onBlock: BlockState,
			adjacentBlocks: List<BlockState>,
			blockBehind: BlockState
		): WorldInteractionItemRecipe? {
			val input = Input(onBlock, adjacentBlocks, blockBehind)

			return getAllRecipes(level.recipeManager)
				.firstOrNull { recipeHolder ->
					recipeHolder.value.matches(input, level)
				}
				?.value
		}

		// Sort recipes so the most specific ones are checked first
		fun getAllRecipes(recipeManager: RecipeManager): List<RecipeHolder<WorldInteractionItemRecipe>> {
			return recipeManager.getAllRecipesFor(ModRecipeTypes.WORLD_INTERACTION_ITEM.get())
				.sortedWith(compareByDescending<RecipeHolder<WorldInteractionItemRecipe>> {
					it.value.optionalRequiredOnBlock.isPresent
				}.thenByDescending {
					it.value.requiredAdjacentBlocks.size
				}.thenByDescending {
					it.value.optionalRequiredBlockBehind.isPresent
				}
				)
		}
	}

	class Input(
		val onBlock: BlockState,
		val adjacentBlocks: List<BlockState>,
		val blockBehind: BlockState
	) : RecipeInput {
		override fun getItem(index: Int): ItemStack = ItemStack.EMPTY
		override fun size(): Int = 0
	}

	class Serializer : RecipeSerializer<WorldInteractionItemRecipe> {
		override fun codec(): MapCodec<WorldInteractionItemRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, WorldInteractionItemRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<WorldInteractionItemRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						BlockStateIngredient.CODEC
							.optionalFieldOf("on")
							.forGetter(WorldInteractionItemRecipe::optionalRequiredOnBlock),
						BlockStateIngredient.CODEC.listOf()
							.optionalFieldOf("adjacent", emptyList())
							.forGetter(WorldInteractionItemRecipe::requiredAdjacentBlocks),
						BlockStateIngredient.CODEC
							.optionalFieldOf("behind")
							.forGetter(WorldInteractionItemRecipe::optionalRequiredBlockBehind),
						ItemStack.CODEC
							.fieldOf("output")
							.forGetter(WorldInteractionItemRecipe::output)
					).apply(instance, ::WorldInteractionItemRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, WorldInteractionItemRecipe> =
				StreamCodec.composite(
					ByteBufCodecs.optional(BlockStateIngredient.STREAM_CODEC), WorldInteractionItemRecipe::optionalRequiredOnBlock,
					BlockStateIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), WorldInteractionItemRecipe::requiredAdjacentBlocks,
					ByteBufCodecs.optional(BlockStateIngredient.STREAM_CODEC), WorldInteractionItemRecipe::optionalRequiredBlockBehind,
					ItemStack.STREAM_CODEC, WorldInteractionItemRecipe::output,
					::WorldInteractionItemRecipe
				)
		}
	}

}