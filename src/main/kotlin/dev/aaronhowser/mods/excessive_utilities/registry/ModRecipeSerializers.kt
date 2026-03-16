package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.recipe.EnchanterRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.QedRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.ResonatorRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.WorldInteractionFluidRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.WorldInteractionItemRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.ItemAndFluidFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.SingleFluidFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.SingleItemFuelRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModRecipeSerializers {

	val RECIPE_SERIALIZERS_REGISTRY: DeferredRegister<RecipeSerializer<*>> =
		DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ExcessiveUtilities.MOD_ID)

	val RESONATOR: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("resonator", ResonatorRecipe::Serializer)
	val ENCHANTER: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("enchanter", EnchanterRecipe::Serializer)
	val QED: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("qed", QedRecipe::Serializer)

	val WORLD_INTERACTION_ITEM: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("world_interaction_item", WorldInteractionItemRecipe::Serializer)
	val WORLD_INTERACTION_FLUID: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("world_interaction_fluid", WorldInteractionFluidRecipe::Serializer)

	// Generator fuels
	val SINGLE_ITEM_FUEL: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("single_item_fuel", SingleItemFuelRecipe::Serializer)
	val SINGLE_FLUID_FUEL: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("single_fluid_fuel", SingleFluidFuelRecipe::Serializer)
	val ITEM_AND_FLUID_FUEL: DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> =
		registerRecipeSerializer("item_and_fluid_fuel", ItemAndFluidFuelRecipe::Serializer)

	private fun registerRecipeSerializer(
		name: String,
		factory: () -> RecipeSerializer<*>
	): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> {
		return RECIPE_SERIALIZERS_REGISTRY.register(name, factory)
	}

}