package dev.aaronhowser.mods.excessive_utilities.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class QedRecipe(
	val pattern: ShapedRecipePattern,
	val result: ItemStack
) : Recipe<CraftingInput> {

	override fun matches(input: CraftingInput, level: Level): Boolean = pattern.matches(input)
	override fun assemble(input: CraftingInput, registries: HolderLookup.Provider): ItemStack = result.copy()
	override fun canCraftInDimensions(width: Int, height: Int): Boolean = true
	override fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.copy()
	override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.QED.get()
	override fun getType(): RecipeType<*> = ModRecipeTypes.QED.get()

	companion object {
		fun getAllRecipes(recipeManager: RecipeManager): List<RecipeHolder<QedRecipe>> {
			return recipeManager.getAllRecipesFor(ModRecipeTypes.QED.get())
		}

		fun getRecipe(level: Level, input: CraftingInput): QedRecipe? {
			return getAllRecipes(level.recipeManager)
				.firstOrNull { recipeHolder ->
					recipeHolder.value.matches(input, level)
				}
				?.value
		}
	}

	class Serializer : RecipeSerializer<QedRecipe> {
		override fun codec(): MapCodec<QedRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, QedRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<QedRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						ShapedRecipePattern.MAP_CODEC
							.fieldOf("pattern")
							.forGetter(QedRecipe::pattern),
						ItemStack.CODEC
							.fieldOf("result")
							.forGetter(QedRecipe::result)
					).apply(instance, ::QedRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, QedRecipe> =
				StreamCodec.composite(
					ShapedRecipePattern.STREAM_CODEC, QedRecipe::pattern,
					ItemStack.STREAM_CODEC, QedRecipe::result,
					::QedRecipe
				)
		}
	}

}