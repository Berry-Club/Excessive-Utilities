package dev.aaronhowser.mods.excessive_utilities.recipe.crafting

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import net.minecraft.core.NonNullList
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*

class KeepPaintbrushRecipe(
	val pattern: ShapedRecipePattern,
	val output: ItemStack,
) : SpecialShapedRecipe(pattern, output) {

	override fun getRemainingItems(input: CraftingInput): NonNullList<ItemStack> {
		val list = NonNullList.withSize(input.size(), ItemStack.EMPTY)

		for (i in list.indices) {
			val stack = input.getItem(i)

			stack.craftingRemainder?.let { list[i] = it.create() }

			if (stack.isItem(ModItems.PAINTBRUSH)) {
				list[i] = stack.copy()
			}
		}

		return list
	}

	override fun isSpecial(): Boolean = true
	override fun getSerializer(): RecipeSerializer<KeepPaintbrushRecipe> = ModRecipeSerializers.KEEP_PAINTBRUSH.get()

	class Serializer {

		companion object {
			val CODEC: MapCodec<KeepPaintbrushRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						ShapedRecipePattern.MAP_CODEC
							.fieldOf("pattern")
							.forGetter(KeepPaintbrushRecipe::pattern),
						ItemStack.CODEC
							.fieldOf("result")
							.forGetter(KeepPaintbrushRecipe::output)
					).apply(instance, ::KeepPaintbrushRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, KeepPaintbrushRecipe> =
				StreamCodec.composite(
					ShapedRecipePattern.STREAM_CODEC, KeepPaintbrushRecipe::pattern,
					ItemStack.STREAM_CODEC, KeepPaintbrushRecipe::output,
					::KeepPaintbrushRecipe
				)
		}
	}

}
