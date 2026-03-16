package dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class SingleFluidFuelRecipe(
	val fluidIngredient: SizedFluidIngredient,
	fePerTick: Int,
	duration: Int
) : GeneratorFuelRecipe<SingleFluidFuelRecipe.Input>(fePerTick, duration) {

	override fun matches(input: Input, level: Level): Boolean {
		val fluidStack = input.getFluid()
		return fluidIngredient.test(fluidStack)
	}

	override fun getSerializer(): RecipeSerializer<*> = ModRecipeSerializers.SINGLE_FLUID_FUEL.get()
	override fun getType(): RecipeType<*> = ModRecipeTypes.SINGLE_FLUID_FUEL.get()

	companion object {
		fun getRecipe(
			level: Level,
			fluidStack: FluidStack
		): SingleFluidFuelRecipe? {
			val input = Input(fluidStack)
			return getAllRecipes(level.recipeManager)
				.firstOrNull { it.value.matches(input, level) }
				?.value
		}

		fun getAllRecipes(
			recipeManager: RecipeManager
		): List<RecipeHolder<SingleFluidFuelRecipe>> {
			return recipeManager.getAllRecipesFor(ModRecipeTypes.SINGLE_FLUID_FUEL.get())
		}
	}

	class Input(
		private val fluidStack: FluidStack
	) : RecipeInput {
		override fun size(): Int = 0
		override fun getItem(index: Int): ItemStack = ItemStack.EMPTY
		fun getFluid(): FluidStack = fluidStack
	}

	class Serializer : RecipeSerializer<SingleFluidFuelRecipe> {
		override fun codec(): MapCodec<SingleFluidFuelRecipe> = CODEC
		override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, SingleFluidFuelRecipe> = STREAM_CODEC

		companion object {
			val CODEC: MapCodec<SingleFluidFuelRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						SizedFluidIngredient.FLAT_CODEC
							.fieldOf("ingredient")
							.forGetter(SingleFluidFuelRecipe::fluidIngredient),
						Codec.INT
							.fieldOf("fe_per_tick")
							.forGetter(SingleFluidFuelRecipe::fePerTick),
						Codec.INT
							.fieldOf("duration")
							.forGetter(SingleFluidFuelRecipe::duration)
					).apply(instance, ::SingleFluidFuelRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, SingleFluidFuelRecipe> =
				StreamCodec.composite(
					SizedFluidIngredient.STREAM_CODEC, SingleFluidFuelRecipe::fluidIngredient,
					ByteBufCodecs.VAR_INT, SingleFluidFuelRecipe::fePerTick,
					ByteBufCodecs.VAR_INT, SingleFluidFuelRecipe::duration,
					::SingleFluidFuelRecipe
				)
		}
	}

}