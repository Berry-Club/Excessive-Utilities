package dev.aaronhowser.mods.excessive_utilities.recipe.machine

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import dev.aaronhowser.mods.excessive_utilities.recipe.base.MachineRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeTypes
import net.minecraft.core.HolderLookup
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemStackTemplate
import net.minecraft.world.item.crafting.*
import net.minecraft.world.level.Level
import java.util.Optional

class CrusherRecipe(
	val ingredient: Ingredient,
	private val primaryOutput: ItemStackTemplate,
	private val secondaryOutput: Optional<ItemStackTemplate>,
	val secondaryChance: Float,
	val ticks: Int,
	val fePerTick: Int
) : MachineRecipe<SingleRecipeInput> {

	fun getPrimaryOutput(): ItemStack = primaryOutput.create()
	fun getSecondaryOutput(): ItemStack = secondaryOutput.map(ItemStackTemplate::create).orElse(ItemStack.EMPTY)

	override fun matches(input: SingleRecipeInput, level: Level): Boolean = ingredient.test(input.item())
	override fun assemble(input: SingleRecipeInput): ItemStack = primaryOutput.create()
	fun canCraftInDimensions(width: Int, height: Int): Boolean = true
	fun getResultItem(registries: HolderLookup.Provider): ItemStack = primaryOutput.create()

	override fun getSerializer(): RecipeSerializer<CrusherRecipe> = ModRecipeSerializers.CRUSHER.get()
	override fun getType(): RecipeType<CrusherRecipe> = ModRecipeTypes.CRUSHER.get()

	companion object {
		const val DEFAULT_SECONDARY_CHANCE = 0f
		const val DEFAULT_TICKS = 200
		const val DEFAULT_FE_PER_TICK = 20

		fun getRecipe(
			level: Level,
			input: ItemStack
		): RecipeHolder<CrusherRecipe>? {
			return getAllRecipes(dev.aaronhowser.mods.excessive_utilities.recipe.base.ModRecipes.recipes(level))
				.firstOrNull { recipeHolder ->
					recipeHolder.value.matches(SingleRecipeInput(input), level)
				}
		}

		fun getAllRecipes(recipeManager: RecipeMap): List<RecipeHolder<CrusherRecipe>> {
			return recipeManager.byType(ModRecipeTypes.CRUSHER.get()).toList()
		}
	}

	class Serializer {

		companion object {
			val CODEC: MapCodec<CrusherRecipe> =
				RecordCodecBuilder.mapCodec { instance ->
					instance.group(
						Ingredient.CODEC
							.fieldOf("ingredient")
							.forGetter(CrusherRecipe::ingredient),
						ItemStackTemplate.CODEC
							.fieldOf("primary_output")
							.forGetter(CrusherRecipe::primaryOutput),
						ItemStackTemplate.CODEC
							.optionalFieldOf("secondary_output")
							.forGetter(CrusherRecipe::secondaryOutput),
						Codec.FLOAT
							.optionalFieldOf("secondary_chance", DEFAULT_SECONDARY_CHANCE)
							.forGetter(CrusherRecipe::secondaryChance),
						Codec.INT
							.optionalFieldOf("ticks", DEFAULT_TICKS)
							.forGetter(CrusherRecipe::ticks),
						Codec.INT
							.optionalFieldOf("fe_per_tick", DEFAULT_FE_PER_TICK)
							.forGetter(CrusherRecipe::fePerTick)
					).apply(instance, ::CrusherRecipe)
				}

			val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, CrusherRecipe> =
				StreamCodec.composite(
					Ingredient.CONTENTS_STREAM_CODEC, CrusherRecipe::ingredient,
					ItemStackTemplate.STREAM_CODEC, CrusherRecipe::primaryOutput,
					ByteBufCodecs.optional(ItemStackTemplate.STREAM_CODEC), CrusherRecipe::secondaryOutput,
					ByteBufCodecs.FLOAT, CrusherRecipe::secondaryChance,
					ByteBufCodecs.INT, CrusherRecipe::ticks,
					ByteBufCodecs.INT, CrusherRecipe::fePerTick,
					::CrusherRecipe
				)
		}
	}

}