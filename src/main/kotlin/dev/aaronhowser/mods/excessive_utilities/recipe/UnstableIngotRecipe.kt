package dev.aaronhowser.mods.excessive_utilities.recipe

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.item.UnstableIngotItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class UnstableIngotRecipe(
	pattern: ShapedRecipePattern,
	val output: ItemStack,
) : ShapedRecipe("", CraftingBookCategory.MISC, pattern, output, false) {

	override fun matches(input: CraftingInput, level: Level): Boolean {
		for (inputStack in input.items()) {
			if (!inputStack.isItem(ModItems.UNSTABLE_INGOT)) continue

			if (UnstableIngotItem.isCheesed(inputStack)) {
				return false
			}
		}

		return super.matches(input, level)
	}

	override fun isSpecial(): Boolean = true
	override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.UNSTABLE_INGOT.get()

	class Serializer : RecipeSerializer<UnstableIngotRecipe> {
		override fun codec(): MapCodec<UnstableIngotRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, UnstableIngotRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<UnstableIngotRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						ShapedRecipePattern.MAP_CODEC
							.fieldOf("pattern")
							.forGetter(UnstableIngotRecipe::pattern),
						ItemStack.STRICT_CODEC
							.fieldOf("result")
							.forGetter(UnstableIngotRecipe::output)
					).apply(instance, ::UnstableIngotRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, UnstableIngotRecipe> =
				StreamCodec.composite(
					ShapedRecipePattern.STREAM_CODEC, UnstableIngotRecipe::pattern,
					ItemStack.STREAM_CODEC, UnstableIngotRecipe::output,
					::UnstableIngotRecipe
				)
		}
	}

}