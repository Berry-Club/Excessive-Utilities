package dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel

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

class MagmaticFuelRecipe(
	val fluidIngredient: SizedFluidIngredient,
	fePerTick: Int,
	duration: Int
) : GeneratorFuelRecipe<MagmaticFuelRecipe.Input>(fePerTick, duration) {

	override fun matches(input: Input, level: Level): Boolean {
		val fluidStack = input.getFluid()
		return fluidIngredient.test(fluidStack)
	}

	override fun getSerializer(): RecipeSerializer<MagmaticFuelRecipe> = ModRecipeSerializers.MAGMATIC_FUEL.get()
	override fun getType(): RecipeType<MagmaticFuelRecipe> = ModRecipeTypes.SINGLE_FLUID_FUEL.get()

	companion object {
		fun getRecipe(
			level: Level,
			fluidStack: FluidStack
		): MagmaticFuelRecipe? {
			val input = Input(fluidStack)
			return getAllRecipes(dev.aaronhowser.mods.excessive_utilities.recipe.base.ModRecipes.recipes(level))
				.firstOrNull { it.value.matches(input, level) }
				?.value
		}

		fun getAllRecipes(
			recipeManager: RecipeMap
		): List<RecipeHolder<MagmaticFuelRecipe>> {
			return recipeManager.byType(ModRecipeTypes.SINGLE_FLUID_FUEL.get()).toList()
		}
	}

	class Input(
		private val fluidStack: FluidStack
	) : RecipeInput {
		override fun size(): Int = 0
		override fun getItem(index: Int): ItemStack = ItemStack.EMPTY
		fun getFluid(): FluidStack = fluidStack
	}

	class Serializer {

		companion object {
			val CODEC: MapCodec<MagmaticFuelRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						SizedFluidIngredient.CODEC
							.fieldOf("ingredient")
							.forGetter(MagmaticFuelRecipe::fluidIngredient),
						Codec.INT
							.fieldOf("fe_per_tick")
							.forGetter(MagmaticFuelRecipe::fePerTick),
						Codec.INT
							.fieldOf("duration")
							.forGetter(MagmaticFuelRecipe::duration)
					).apply(instance, ::MagmaticFuelRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, MagmaticFuelRecipe> =
				StreamCodec.composite(
					SizedFluidIngredient.STREAM_CODEC, MagmaticFuelRecipe::fluidIngredient,
					ByteBufCodecs.VAR_INT, MagmaticFuelRecipe::fePerTick,
					ByteBufCodecs.VAR_INT, MagmaticFuelRecipe::duration,
					::MagmaticFuelRecipe
				)
		}
	}

}