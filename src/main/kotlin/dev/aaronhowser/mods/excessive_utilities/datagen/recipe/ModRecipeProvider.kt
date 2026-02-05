package dev.aaronhowser.mods.excessive_utilities.datagen.recipe

import dev.aaronhowser.mods.aaron.datagen.AaronRecipeProvider
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.world.item.Items
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>
) : AaronRecipeProvider(output, lookupProvider) {

	override fun buildRecipes(recipeOutput: RecipeOutput, holderLookup: HolderLookup.Provider) {

		resonatingRecipes(recipeOutput)

	}

	private fun resonatingRecipes(recipeOutput: RecipeOutput) {
		val recipes = listOf(
			ResonatorRecipeBuilder(
				ing(Items.STONE).getIngredient(),
				Items.COBBLESTONE.defaultInstance,
				10
			)
		)

		for (recipe in recipes) {
			recipe.save(recipeOutput)
		}
	}

}