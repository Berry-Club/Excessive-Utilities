package dev.aaronhowser.mods.excessive_utilities.recipe.crafting

import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.serialization.AaronExtraStreamCodecs
import dev.aaronhowser.mods.excessive_utilities.item.UnstableIngotItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import io.netty.buffer.ByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class ShapedUnstableRecipe(
	pattern: ShapedRecipePattern,
	val output: ItemStack,
	val requiredStability: Stability,
) : ShapedRecipe("", CraftingBookCategory.MISC, pattern, output, false) {

	override fun matches(input: CraftingInput, level: Level): Boolean {
		for (inputStack in input.items()) {
			if (!inputStack.isItem(ModItems.UNSTABLE_INGOT)) continue

			if (UnstableIngotItem.isCheesed(inputStack)) return false

			if (!requiredStability.accepts(inputStack)) {
				return false
			}
		}

		return super.matches(input, level)
	}

	override fun isSpecial(): Boolean = true
	override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.SHAPED_UNSTABLE.get()

	class Serializer : RecipeSerializer<ShapedUnstableRecipe> {
		override fun codec(): MapCodec<ShapedUnstableRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, ShapedUnstableRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<ShapedUnstableRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						ShapedRecipePattern.MAP_CODEC
							.fieldOf("pattern")
							.forGetter(ShapedUnstableRecipe::pattern),
						ItemStack.STRICT_CODEC
							.fieldOf("result")
							.forGetter(ShapedUnstableRecipe::output),
						Stability.CODEC
							.optionalFieldOf("stability", Stability.EITHER)
							.forGetter(ShapedUnstableRecipe::requiredStability)
					).apply(instance, ::ShapedUnstableRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ShapedUnstableRecipe> =
				StreamCodec.composite(
					ShapedRecipePattern.STREAM_CODEC, ShapedUnstableRecipe::pattern,
					ItemStack.STREAM_CODEC, ShapedUnstableRecipe::output,
					Stability.STREAM_CODEC, ShapedUnstableRecipe::requiredStability,
					::ShapedUnstableRecipe
				)
		}
	}

	enum class Stability(private val id: String) : StringRepresentable {
		STABLE("stable"),
		UNSTABLE("unstable"),
		EITHER("either");

		fun accepts(stack: ItemStack): Boolean {
			return when (this) {
				STABLE -> isStable(stack)
				UNSTABLE -> isUnstableAndCountingDown(stack)
				EITHER -> true
			}
		}

		override fun getSerializedName(): String {
			return id
		}

		companion object {
			val CODEC: StringRepresentable.EnumCodec<Stability> = StringRepresentable.fromEnum { entries.toTypedArray() }
			val STREAM_CODEC: StreamCodec<ByteBuf, Stability> = AaronExtraStreamCodecs.enumStreamCodec(Stability::class.java)

			private fun isStable(stack: ItemStack): Boolean {
				return !stack.has(ModDataComponents.COUNTDOWN)
			}

			private fun isUnstableAndCountingDown(stack: ItemStack): Boolean {
				val countdown = stack.get(ModDataComponents.COUNTDOWN) ?: return false
				return countdown > 0 && stack.has(ModDataComponents.CRAFTED_IN_MENU)
			}
		}
	}

}
