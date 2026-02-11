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

class QEDRecipe(
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
		fun getAllRecipes(recipeManager: RecipeManager): List<RecipeHolder<QEDRecipe>> {
			return recipeManager.getAllRecipesFor(ModRecipeTypes.QED.get())
		}
	}

	class Serializer : RecipeSerializer<QEDRecipe> {
		override fun codec(): MapCodec<QEDRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, QEDRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<QEDRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						ShapedRecipePattern.MAP_CODEC
							.fieldOf("pattern")
							.forGetter(QEDRecipe::pattern),
						ItemStack.CODEC
							.fieldOf("result")
							.forGetter(QEDRecipe::result)
					).apply(instance, ::QEDRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, QEDRecipe> =
				StreamCodec.composite(
					ShapedRecipePattern.STREAM_CODEC, QEDRecipe::pattern,
					ItemStack.STREAM_CODEC, QEDRecipe::result,
					::QEDRecipe
				)
		}
	}

}