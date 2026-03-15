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
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class ItemAndFluidFuelRecipe(
	val generatorType: GeneratorType,
	val itemIngredient: Ingredient,
	val fluidIngredient: SizedFluidIngredient,
	fePerTick: Int,
	duration: Int
) : GeneratorFuelRecipe<ItemAndFluidFuelRecipe.Input>(fePerTick, duration) {

	override fun matches(input: Input, level: Level): Boolean {
		val itemStack = input.getItem(0)
		if (!itemIngredient.isEmpty && !itemIngredient.test(itemStack)) return false

		if (fluidIngredient.fluids.isEmpty()) return true
		val fluidStack = input.getFluid()
		return fluidIngredient.test(fluidStack)
	}

	override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.ITEM_AND_FLUID_FUEL.get()
	override fun getType(): RecipeType<*> = ModRecipeTypes.ITEM_AND_FLUID_FUEL.get()

	companion object {
		fun isValidFluid(
			fluidStack: FluidStack,
			generatorType: GeneratorType,
			recipeManager: RecipeManager,
		): Boolean {
			return getAllRecipesOfType(generatorType, recipeManager)
				.any { it.value.fluidIngredient.test(fluidStack.copyWithAmount(99999)) }
		}

		fun isValidItem(
			itemStack: ItemStack,
			generatorType: GeneratorType,
			recipeManager: RecipeManager,
		): Boolean {
			return getAllRecipesOfType(generatorType, recipeManager)
				.any { it.value.itemIngredient.test(itemStack) }
		}

		fun getRecipe(
			level: Level,
			type: GeneratorType,
			itemStack: ItemStack,
			fluidStack: FluidStack
		): ItemAndFluidFuelRecipe? {
			val input = Input(itemStack, fluidStack)

			return getAllRecipesOfType(type, level.recipeManager)
				.firstOrNull { it.value.matches(input, level) }
				?.value
		}

		fun getAllRecipesOfType(
			generatorType: GeneratorType,
			recipeManager: RecipeManager
		): List<RecipeHolder<ItemAndFluidFuelRecipe>> {
			return getAllRecipes(recipeManager).filter { it.value().generatorType == generatorType }
		}


		fun getAllRecipes(
			recipeManager: RecipeManager
		): List<RecipeHolder<ItemAndFluidFuelRecipe>> {
			return recipeManager.getAllRecipesFor(ModRecipeTypes.ITEM_AND_FLUID_FUEL.get())
		}
	}

	enum class GeneratorType(
		val id: String
	) : StringRepresentable {
		SLIMY("slimy"),
		HEATED_REDSTONE("heated_redstone"),
		;

		override fun getSerializedName(): String = id

		companion object {
			val CODEC: StringRepresentable.EnumCodec<GeneratorType> = StringRepresentable.fromEnum { entries.toTypedArray() }
			val STREAM_CODEC: StreamCodec<ByteBuf, GeneratorType> = ByteBufCodecs.fromCodec(CODEC)
		}
	}

	class Input(
		private val itemStack: ItemStack,
		private val fluidStack: FluidStack
	) : RecipeInput {
		override fun getItem(index: Int): ItemStack = itemStack
		fun getFluid(): FluidStack = fluidStack
		override fun size(): Int = 1
	}

	class Serializer : RecipeSerializer<ItemAndFluidFuelRecipe> {
		override fun codec(): MapCodec<ItemAndFluidFuelRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, ItemAndFluidFuelRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<ItemAndFluidFuelRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						GeneratorType.CODEC
							.fieldOf("generator_type")
							.forGetter(ItemAndFluidFuelRecipe::generatorType),
						Ingredient.CODEC
							.fieldOf("item_ingredient")
							.forGetter(ItemAndFluidFuelRecipe::itemIngredient),
						SizedFluidIngredient.FLAT_CODEC
							.fieldOf("fluid_ingredient")
							.forGetter(ItemAndFluidFuelRecipe::fluidIngredient),
						Codec.INT
							.fieldOf("fe_per_tick")
							.forGetter(ItemAndFluidFuelRecipe::fePerTick),
						Codec.INT
							.fieldOf("duration")
							.forGetter(ItemAndFluidFuelRecipe::duration)
					).apply(instance, ::ItemAndFluidFuelRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ItemAndFluidFuelRecipe> =
				StreamCodec.composite(
					GeneratorType.STREAM_CODEC, ItemAndFluidFuelRecipe::generatorType,
					Ingredient.CONTENTS_STREAM_CODEC, ItemAndFluidFuelRecipe::itemIngredient,
					SizedFluidIngredient.STREAM_CODEC, ItemAndFluidFuelRecipe::fluidIngredient,
					ByteBufCodecs.VAR_INT, ItemAndFluidFuelRecipe::fePerTick,
					ByteBufCodecs.VAR_INT, ItemAndFluidFuelRecipe::duration,
					::ItemAndFluidFuelRecipe
				)
		}
	}

}