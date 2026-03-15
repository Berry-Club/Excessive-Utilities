package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.recipe.EnchanterRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.QedRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.ResonatorRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.MagmaticFuelRecipe
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

	// Generator fuels
	val SINGLE_ITEM_FUEL: DeferredHolder<RecipeType<*>, RecipeType<SingleItemFuelRecipe>> =
		registerRecipeType("single_item_fuel")
	val MAGMATIC_FUEL: DeferredHolder<RecipeType<*>, RecipeType<MagmaticFuelRecipe>> =
		registerRecipeType("magmatic_fuel")

	private fun <T : Recipe<*>> registerRecipeType(
		name: String
	): DeferredHolder<RecipeType<*>, RecipeType<T>> {
		return RECIPE_TYPES_REGISTRY.register(name, Supplier { object : RecipeType<T> {} })
	}

}