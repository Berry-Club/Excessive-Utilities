package dev.aaronhowser.mods.excessive_utilities.recipe.machine

import dev.aaronhowser.mods.excessive_utilities.recipe.base.MachineRecipe
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

class QedRecipe(
	val pattern: ShapedRecipePattern,
	val result: ItemStack,
	val crystalTicks: Int
) : MachineRecipe<CraftingInput> {

	override fun matches(input: CraftingInput, level: Level): Boolean = pattern.matches(input)
	override fun assemble(input: CraftingInput): ItemStack = result.copy()
	fun canCraftInDimensions(width: Int, height: Int): Boolean = true
	fun getResultItem(registries: HolderLookup.Provider): ItemStack = result.copy()
	override fun getSerializer(): RecipeSerializer<QedRecipe> = ModRecipeSerializers.QED.get()
	override fun getType(): RecipeType<QedRecipe> = ModRecipeTypes.QED.get()

	companion object {
		fun getAllRecipes(recipeManager: RecipeMap): List<RecipeHolder<QedRecipe>> {
			return recipeManager.byType(ModRecipeTypes.QED.get()).toList()
		}

		fun getRecipe(level: Level, input: CraftingInput): RecipeHolder<QedRecipe>? {
			return getAllRecipes(dev.aaronhowser.mods.excessive_utilities.recipe.base.ModRecipes.recipes(level))
				.firstOrNull { recipeHolder ->
					recipeHolder.value.matches(input, level)
				}
		}
	}

	class Serializer {

		companion object {
			val CODEC: MapCodec<QedRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						ShapedRecipePattern.MAP_CODEC
							.fieldOf("pattern")
							.forGetter(QedRecipe::pattern),
						ItemStack.CODEC
							.fieldOf("result")
							.forGetter(QedRecipe::result),
						Codec.INT
							.fieldOf("crystal_ticks")
							.forGetter(QedRecipe::crystalTicks)
					).apply(instance, ::QedRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, QedRecipe> =
				StreamCodec.composite(
					ShapedRecipePattern.STREAM_CODEC, QedRecipe::pattern,
					ItemStack.STREAM_CODEC, QedRecipe::result,
					ByteBufCodecs.VAR_INT, QedRecipe::crystalTicks,
					::QedRecipe
				)
		}
	}

}