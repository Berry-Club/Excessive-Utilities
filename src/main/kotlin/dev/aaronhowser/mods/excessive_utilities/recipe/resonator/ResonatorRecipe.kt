package dev.aaronhowser.mods.excessive_utilities.recipe.resonator

import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeSerializers
import dev.aaronhowser.mods.excessive_utilities.registry.ModRecipeTypes
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.SingleItemRecipe
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level

class ResonatorRecipe(
	ingredient: Ingredient,
	output: ItemStack,
	val gpCost: Int
) : SingleItemRecipe(
	ModRecipeTypes.RESONATOR.get(),
	ModRecipeSerializers.RESONATOR.get(),
	"eu_resonator",
	ingredient,
	output
) {

	override fun matches(input: SingleRecipeInput, level: Level): Boolean {
		TODO("Not yet implemented")
	}

	interface Factory : SingleItemRecipe.Factory<ResonatorRecipe> {
		fun create(group: String, ingredient: Ingredient, output: ItemStack, gpCost: Int): ResonatorRecipe
	}

	class Serializer(factory: Factory) : SingleItemRecipe.Serializer<ResonatorRecipe>(factory) {

	}

}