package dev.aaronhowser.mods.excessive_utilities.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
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

	override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.ENCHANTER.get()
	override fun getType(): RecipeType<*> = ModRecipeTypes.ENCHANTER.get()

	companion object {
		fun getRecipe(
			level: Level,
			leftInput: ItemStack,
			rightInput: ItemStack
		): RecipeHolder<EnchanterRecipe>? {
			return getAllRecipes(level.recipeManager)
				.firstOrNull { recipeHolder ->
					recipeHolder.value.matches(Input(leftInput, rightInput), level)
				}
		}

		fun getAllRecipes(recipeManager: RecipeManager): List<RecipeHolder<EnchanterRecipe>> {
			return recipeManager.getAllRecipesFor(ModRecipeTypes.ENCHANTER.get())
		}
	}

	class Serializer : RecipeSerializer<EnchanterRecipe> {
		override fun codec(): MapCodec<EnchanterRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, EnchanterRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<EnchanterRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						Ingredient.CODEC_NONEMPTY
							.fieldOf("left")
							.forGetter(EnchanterRecipe::leftIngredient),
						Ingredient.CODEC_NONEMPTY
							.fieldOf("right")
							.forGetter(EnchanterRecipe::rightIngredient),
						Codec.INT
							.fieldOf("fe_cost")
							.forGetter(EnchanterRecipe::feCost),
						Codec.INT
							.fieldOf("ticks")
							.forGetter(EnchanterRecipe::ticks),
						ItemStack.CODEC
							.fieldOf("output")
							.forGetter(EnchanterRecipe::result)
					).apply(instance, ::EnchanterRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, EnchanterRecipe> =
				StreamCodec.composite(
					Ingredient.CONTENTS_STREAM_CODEC, EnchanterRecipe::leftIngredient,
					Ingredient.CONTENTS_STREAM_CODEC, EnchanterRecipe::rightIngredient,
					ByteBufCodecs.VAR_INT, EnchanterRecipe::feCost,
					ByteBufCodecs.VAR_INT, EnchanterRecipe::ticks,
					ItemStack.STREAM_CODEC, EnchanterRecipe::result,
					::EnchanterRecipe
				)
		}
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