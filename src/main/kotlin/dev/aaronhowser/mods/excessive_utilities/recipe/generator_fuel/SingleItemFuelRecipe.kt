package dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeTypes
import io.netty.buffer.ByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.util.StringRepresentable
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level

class SingleItemFuelRecipe(
	val generatorType: GeneratorType,
	val ingredient: Ingredient,
	fePerTick: Int,
	duration: Int
) : GeneratorFuelRecipe<SingleRecipeInput>(fePerTick, duration) {

	override fun matches(input: SingleRecipeInput, level: Level): Boolean {
		return ingredient.test(input.item())
	}

	override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.SINGLE_ITEM_FUEL.get()
	override fun getType(): RecipeType<*> = ModRecipeTypes.SINGLE_ITEM_FUEL.get()

	companion object {
		fun getRecipe(
			level: Level,
			type: GeneratorType,
			stack: ItemStack,
		): SingleItemFuelRecipe? {
			val input = SingleRecipeInput(stack)

			return getAllRecipesOfType(type, level.recipeManager)
				.firstOrNull { it.value.matches(input, level) }
				?.value
		}

		fun getAllRecipesOfType(
			generatorType: GeneratorType,
			recipeManager: RecipeManager
		): List<RecipeHolder<SingleItemFuelRecipe>> {
			return getAllRecipes(recipeManager).filter { it.value().generatorType == generatorType }
		}

		fun getAllRecipes(
			recipeManager: RecipeManager
		): List<RecipeHolder<SingleItemFuelRecipe>> {
			return recipeManager.getAllRecipesFor(ModRecipeTypes.SINGLE_ITEM_FUEL.get())
		}
	}

	enum class GeneratorType(
		val id: String
	) : StringRepresentable {
		ENDER("ender"),
		EXPLOSIVE("explosive"),
		PINK("pink"),
		NETHER_STAR("nether_star"),
		FROSTY("frosty"),
		HALITOSIS("halitosis"),
		DEATH("death")
		;

		override fun getSerializedName(): String = id

		companion object {
			val CODEC: StringRepresentable.EnumCodec<GeneratorType> = StringRepresentable.fromEnum { entries.toTypedArray() }
			val STREAM_CODEC: StreamCodec<ByteBuf, GeneratorType> = ByteBufCodecs.fromCodec(CODEC)
		}
	}

	class Serializer : RecipeSerializer<SingleItemFuelRecipe> {
		override fun codec(): MapCodec<SingleItemFuelRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, SingleItemFuelRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<SingleItemFuelRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						GeneratorType.CODEC
							.fieldOf("generator_type")
							.forGetter(SingleItemFuelRecipe::generatorType),
						Ingredient.CODEC
							.fieldOf("ingredient")
							.forGetter(SingleItemFuelRecipe::ingredient),
						Codec.INT
							.fieldOf("fe_per_tick")
							.forGetter(SingleItemFuelRecipe::fePerTick),
						Codec.INT
							.fieldOf("duration")
							.forGetter(SingleItemFuelRecipe::duration)
					).apply(instance, ::SingleItemFuelRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SingleItemFuelRecipe> =
				StreamCodec.composite(
					GeneratorType.STREAM_CODEC, SingleItemFuelRecipe::generatorType,
					Ingredient.CONTENTS_STREAM_CODEC, SingleItemFuelRecipe::ingredient,
					ByteBufCodecs.VAR_INT, SingleItemFuelRecipe::fePerTick,
					ByteBufCodecs.VAR_INT, SingleItemFuelRecipe::duration,
					::SingleItemFuelRecipe
				)
		}
	}

}