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

class DamageGlassCutterRecipe(
	pattern: ShapedRecipePattern,
	val output: ItemStack,
) : ShapedRecipe("", CraftingBookCategory.MISC, pattern, output, false) {

	override fun getRemainingItems(input: CraftingInput): NonNullList<ItemStack> {
		val list = NonNullList.withSize(input.size(), ItemStack.EMPTY)

		for (i in list.indices) {
			val stack = input.getItem(i)

			if (stack.hasCraftingRemainingItem()) {
				list[i] = stack.craftingRemainingItem
			}

			if (stack.isItem(ModItems.GLASS_CUTTER)) {
				val nextDamage = stack.damageValue + 1
				if (nextDamage < stack.maxDamage) {
					val newStack = stack.copy()
					newStack.damageValue = nextDamage
					list[i] = newStack
					break
				}
			}
		}

		return list
	}

	override fun isSpecial(): Boolean = true
	override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.DAMAGE_GLASS_CUTTER.get()

	class Serializer : RecipeSerializer<DamageGlassCutterRecipe> {
		override fun codec(): MapCodec<DamageGlassCutterRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, DamageGlassCutterRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<DamageGlassCutterRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						ShapedRecipePattern.MAP_CODEC
							.fieldOf("pattern")
							.forGetter(DamageGlassCutterRecipe::pattern),
						ItemStack.STRICT_CODEC
							.fieldOf("result")
							.forGetter(DamageGlassCutterRecipe::output)
					).apply(instance, ::DamageGlassCutterRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, DamageGlassCutterRecipe> =
				StreamCodec.composite(
					ShapedRecipePattern.STREAM_CODEC, DamageGlassCutterRecipe::pattern,
					ItemStack.STREAM_CODEC, DamageGlassCutterRecipe::output,
					::DamageGlassCutterRecipe
				)
		}
	}

}