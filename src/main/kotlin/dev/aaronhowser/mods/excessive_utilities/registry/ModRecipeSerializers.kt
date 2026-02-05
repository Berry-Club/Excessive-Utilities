package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.recipe.resonator.ResonatorRecipe
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeSerializer
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModRecipeSerializers {

	val RECIPE_SERIALIZERS_REGISTRY: DeferredRegister<RecipeSerializer<*>> =
		DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ExcessiveUtilities.MOD_ID)

	val RESONATOR =
		registerRecipeSerializer("resonator") { ResonatorRecipe.Serializer(::ResonatorRecipe) }

	private fun registerRecipeSerializer(
		name: String,
		factory: () -> RecipeSerializer<*>
	): DeferredHolder<RecipeSerializer<*>, RecipeSerializer<*>> {
		return RECIPE_SERIALIZERS_REGISTRY.register(name, factory)
	}

}