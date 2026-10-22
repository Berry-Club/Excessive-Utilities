package dev.aaronhowser.mods.excessive_utilities.recipe.machine

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
import net.minecraft.core.HolderLookup

class ResonatorRecipe(
	val ingredient: Ingredient,
	val result: ItemStack,
	val gpCost: Double
) : dev.aaronhowser.mods.excessive_utilities.recipe.base.MachineRecipe<SingleRecipeInput> {
    override fun assemble(input: SingleRecipeInput): ItemStack = result.copy()
    fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.copy()
    override fun getSerializer(): RecipeSerializer<ResonatorRecipe> = ModRecipeSerializers.RESONATOR.get()
    override fun getType(): RecipeType<ResonatorRecipe> = ModRecipeTypes.RESONATOR.get()


	override fun matches(input: SingleRecipeInput, level: Level): Boolean {
		return ingredient.test(input.item())
	}

	class Serializer {

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
						Codec.DOUBLE
							.fieldOf("gp_cost")
							.forGetter(ResonatorRecipe::gpCost)
					).apply(instance, ::ResonatorRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ResonatorRecipe> =
				StreamCodec.composite(
					Ingredient.CONTENTS_STREAM_CODEC, ResonatorRecipe::ingredient,
					ItemStack.STREAM_CODEC, ResonatorRecipe::result,
					ByteBufCodecs.DOUBLE, ResonatorRecipe::gpCost,
					::ResonatorRecipe
				)
		}
	}

	companion object {
		fun getRecipe(level: Level, input: ItemStack): ResonatorRecipe? {
			return getAllRecipes(dev.aaronhowser.mods.excessive_utilities.recipe.base.ModRecipes.recipes(level))
				.firstOrNull { recipeHolder ->
					recipeHolder.value.matches(SingleRecipeInput(input), level)
				}
				?.value
		}

		fun getAllRecipes(recipeManager: RecipeMap): List<RecipeHolder<ResonatorRecipe>> {
			return recipeManager.byType(ModRecipeTypes.RESONATOR.get()).toList()
		}
	}

}