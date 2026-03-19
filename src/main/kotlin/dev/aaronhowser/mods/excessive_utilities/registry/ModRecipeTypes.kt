package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.recipe.*
import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.ItemAndFluidFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.SingleFluidFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.SingleItemFuelRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModRecipeTypes {

	val RECIPE_TYPES_REGISTRY: DeferredRegister<RecipeType<*>> =
		DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, ExcessiveUtilities.MOD_ID)

	val RESONATOR: DeferredHolder<RecipeType<*>, RecipeType<ResonatorRecipe>> =
		registerRecipeType("resonator")
	val ENCHANTER: DeferredHolder<RecipeType<*>, RecipeType<EnchanterRecipe>> =
		registerRecipeType("enchanter")
	val QED: DeferredHolder<RecipeType<*>, RecipeType<QedRecipe>> =
		registerRecipeType("qed")
	val CRUSHER: DeferredHolder<RecipeType<*>, RecipeType<CrusherRecipe>> =
		registerRecipeType("crusher")

	val WORLD_INTERACTION_ITEM: DeferredHolder<RecipeType<*>, RecipeType<WorldInteractionItemRecipe>> =
		registerRecipeType("world_interaction_item")
	val WORLD_INTERACTION_FLUID: DeferredHolder<RecipeType<*>, RecipeType<WorldInteractionFluidRecipe>> =
		registerRecipeType("world_interaction_fluid")

	// Generator fuels
	val SINGLE_ITEM_FUEL: DeferredHolder<RecipeType<*>, RecipeType<SingleItemFuelRecipe>> =
		registerRecipeType("single_item_fuel")
	val SINGLE_FLUID_FUEL: DeferredHolder<RecipeType<*>, RecipeType<SingleFluidFuelRecipe>> =
		registerRecipeType("single_fluid_fuel")
	val ITEM_AND_FLUID_FUEL: DeferredHolder<RecipeType<*>, RecipeType<ItemAndFluidFuelRecipe>> =
		registerRecipeType("item_and_fluid_fuel")

	private fun <T : Recipe<*>> registerRecipeType(
		name: String
	): DeferredHolder<RecipeType<*>, RecipeType<T>> {
		return RECIPE_TYPES_REGISTRY.register(name, Supplier { object : RecipeType<T> {} })
	}

}