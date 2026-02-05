package dev.aaronhowser.mods.excessive_utilities.recipe.resonator

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class ResonatorRecipe(
	ingredient: Ingredient,
	result: ItemStack,
	val gpCost: Int
) : SingleItemRecipe(
	ModRecipeTypes.RESONATOR.get(),
	ModRecipeSerializers.RESONATOR.get(),
	"eu_resonator",
	ingredient,
	result
) {

	override fun matches(input: SingleRecipeInput, level: Level): Boolean {
		return ingredient.test(input.item())
	}

	class Serializer : RecipeSerializer<ResonatorRecipe> {
		override fun codec(): MapCodec<ResonatorRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, ResonatorRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<ResonatorRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						Ingredient.CODEC
							.fieldOf("ingredient")
							.forGetter(ResonatorRecipe::ingredient),
						ItemStack.CODEC
							.fieldOf("output")
							.forGetter(ResonatorRecipe::result),
						Codec.INT
							.fieldOf("gp_cost")
							.forGetter(ResonatorRecipe::gpCost)
					).apply(instance, ::ResonatorRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ResonatorRecipe> =
				StreamCodec.composite(
					Ingredient.CONTENTS_STREAM_CODEC, ResonatorRecipe::ingredient,
					ItemStack.STREAM_CODEC, ResonatorRecipe::result,
					ByteBufCodecs.VAR_INT, ResonatorRecipe::gpCost,
					::ResonatorRecipe
				)
		}
	}

	companion object {
		fun getRecipe(level: Level, input: ItemStack): ResonatorRecipe? {
			return getAllRecipes(level.recipeManager)
				.firstOrNull { recipeHolder ->
					recipeHolder.value.matches(SingleRecipeInput(input), level)
				}
				?.value
		}

		fun getAllRecipes(recipeManager: RecipeManager): List<RecipeHolder<ResonatorRecipe>> {
			return recipeManager.getAllRecipesFor(ModRecipeTypes.RESONATOR.get())
		}
	}

}