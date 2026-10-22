package dev.aaronhowser.mods.excessive_utilities.recipe.machine

import dev.aaronhowser.mods.excessive_utilities.recipe.base.MachineRecipe
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
) : MachineRecipe<WorldInteractionItemRecipe.Input> {

	val requiredOnBlock: BlockStateIngredient? = optionalRequiredOnBlock.orElse(null)
	val requiredBlockBehind: BlockStateIngredient? = optionalRequiredBlockBehind.orElse(null)

	override fun matches(input: Input, level: Level): Boolean {
		if (requiredOnBlock != null && !requiredOnBlock.test(input.onBlock)) {
			return false
		}

		val adjacentBlocks = input.adjacentBlocks.toMutableList()

		for (requiredAdjacent in requiredAdjacentBlocks) {
			var foundMatch = false

			val iterator = adjacentBlocks.iterator()

			while (iterator.hasNext()) {
				val next = iterator.next()
				if (requiredAdjacent.test(next)) {
					foundMatch = true
					iterator.remove()
					break
				}
			}

			if (!foundMatch) {
				return false
			}
		}

		return !(requiredBlockBehind != null && !requiredBlockBehind.test(input.blockBehind))
	}

	fun canCraftInDimensions(width: Int, height: Int): Boolean = true
	override fun assemble(input: Input): ItemStack = output.copy()
	fun getResultItem(registries: HolderLookup.Provider): ItemStack = output

	override fun getSerializer(): RecipeSerializer<WorldInteractionItemRecipe> = ModRecipeSerializers.WORLD_INTERACTION_ITEM.get()
	override fun getType(): RecipeType<WorldInteractionItemRecipe> = ModRecipeTypes.WORLD_INTERACTION_ITEM.get()

	companion object {
		fun getRecipe(
			level: Level,
			onBlock: BlockState,
			adjacentBlocks: List<BlockState>,
			blockBehind: BlockState
		): WorldInteractionItemRecipe? {
			val input = Input(onBlock, adjacentBlocks, blockBehind)

			return getAllRecipes(dev.aaronhowser.mods.excessive_utilities.recipe.base.ModRecipes.recipes(level))
				.firstOrNull { recipeHolder ->
					recipeHolder.value.matches(input, level)
				}
				?.value
		}

		// Sort recipes so the most specific ones are checked first
		fun getAllRecipes(recipeManager: RecipeMap): List<RecipeHolder<WorldInteractionItemRecipe>> {
			return recipeManager.byType(ModRecipeTypes.WORLD_INTERACTION_ITEM.get()).toList()
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

	class Serializer {

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