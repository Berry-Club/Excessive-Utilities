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
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

class WorldInteractionFluidRecipe(
	val optionalRequiredOnBlock: Optional<BlockStateIngredient>,
	val requiredAdjacentBlocks: List<BlockStateIngredient>,
	val optionalRequiredBlockBehind: Optional<BlockStateIngredient>,
	val output: FluidStack
) : Recipe<WorldInteractionFluidRecipe.Input> {

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

	override fun canCraftInDimensions(width: Int, height: Int): Boolean = true
	override fun assemble(input: Input, registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY
	override fun getResultItem(registries: HolderLookup.Provider): ItemStack = ItemStack.EMPTY

	override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.WORLD_INTERACTION_ITEM.get()
	override fun getType(): RecipeType<*> = ModRecipeTypes.WORLD_INTERACTION_ITEM.get()

	companion object {
		fun getRecipe(
			level: Level,
			onBlock: BlockState,
			adjacentBlocks: List<BlockState>,
			blockBehind: BlockState
		): WorldInteractionFluidRecipe? {
			val input = Input(onBlock, adjacentBlocks, blockBehind)

			return getAllRecipes(level.recipeManager)
				.firstOrNull { recipeHolder ->
					recipeHolder.value.matches(input, level)
				}
				?.value
		}

		// Sort recipes so the most specific ones are checked first
		fun getAllRecipes(recipeManager: RecipeManager): List<RecipeHolder<WorldInteractionFluidRecipe>> {
			return recipeManager.getAllRecipesFor(ModRecipeTypes.WORLD_INTERACTION_FLUID.get())
				.sortedWith(compareByDescending<RecipeHolder<WorldInteractionFluidRecipe>> {
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

	class Serializer : RecipeSerializer<WorldInteractionFluidRecipe> {
		override fun codec(): MapCodec<WorldInteractionFluidRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, WorldInteractionFluidRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<WorldInteractionFluidRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						BlockStateIngredient.CODEC
							.optionalFieldOf("on")
							.forGetter(WorldInteractionFluidRecipe::optionalRequiredOnBlock),
						BlockStateIngredient.CODEC.listOf()
							.optionalFieldOf("adjacent", emptyList())
							.forGetter(WorldInteractionFluidRecipe::requiredAdjacentBlocks),
						BlockStateIngredient.CODEC
							.optionalFieldOf("behind")
							.forGetter(WorldInteractionFluidRecipe::optionalRequiredBlockBehind),
						FluidStack.CODEC
							.fieldOf("output")
							.forGetter(WorldInteractionFluidRecipe::output)
					).apply(instance, ::WorldInteractionFluidRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, WorldInteractionFluidRecipe> =
				StreamCodec.composite(
					ByteBufCodecs.optional(BlockStateIngredient.STREAM_CODEC), WorldInteractionFluidRecipe::optionalRequiredOnBlock,
					BlockStateIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), WorldInteractionFluidRecipe::requiredAdjacentBlocks,
					ByteBufCodecs.optional(BlockStateIngredient.STREAM_CODEC), WorldInteractionFluidRecipe::optionalRequiredBlockBehind,
					FluidStack.STREAM_CODEC, WorldInteractionFluidRecipe::output,
					::WorldInteractionFluidRecipe
				)
		}
	}

}