package dev.aaronhowser.mods.excessive_utilities.recipe.crafting

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.item.component.MagicalSnowGlobeProgressComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class SnowGlobeRecipe(
	pattern: ShapedRecipePattern,
	val output: ItemStack,
) : ShapedRecipe("", CraftingBookCategory.MISC, pattern, output, false) {

	override fun matches(input: CraftingInput, level: Level): Boolean {
		for (inputStack in input.items()) {
			if (!inputStack.isItem(ModItems.MAGICAL_SNOW_GLOBE)) continue

			val component = inputStack.getOrDefault(
				ModDataComponents.MAGICAL_SNOW_GLOBE_PROGRESS,
				MagicalSnowGlobeProgressComponent.DEFAULT
			)

			if (!component.isComplete) return false
		}

		return super.matches(input, level)
	}

	override fun isSpecial(): Boolean = true
	override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.SNOW_GLOBE.get()

	class Serializer : RecipeSerializer<SnowGlobeRecipe> {
		override fun codec(): MapCodec<SnowGlobeRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, SnowGlobeRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<SnowGlobeRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						ShapedRecipePattern.MAP_CODEC
							.fieldOf("pattern")
							.forGetter(SnowGlobeRecipe::pattern),
						ItemStack.STRICT_CODEC
							.fieldOf("result")
							.forGetter(SnowGlobeRecipe::output)
					).apply(instance, ::SnowGlobeRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SnowGlobeRecipe> =
				StreamCodec.composite(
					ShapedRecipePattern.STREAM_CODEC, SnowGlobeRecipe::pattern,
					ItemStack.STREAM_CODEC, SnowGlobeRecipe::output,
					::SnowGlobeRecipe
				)
		}
	}

}