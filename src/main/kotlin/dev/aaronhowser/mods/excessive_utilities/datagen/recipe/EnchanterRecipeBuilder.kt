package dev.aaronhowser.mods.excessive_utilities.datagen.recipe

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.recipe.EnchanterRecipe
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient

class EnchanterRecipeBuilder(
	val leftIngredient: Ingredient,
	val leftCount: Int,
	val rightIngredient: Ingredient,
	val rightCount: Int,
	val feCost: Int,
	val ticks: Int,
	val result: ItemStack,
) : RecipeBuilder {

	private val criteria: MutableMap<String, Criterion<*>> = mutableMapOf()

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder {
		criteria[name] = criterion
		return this
	}

	override fun group(p0: String?): RecipeBuilder {
		error("Unsupported")
	}

	override fun getResult(): Item = result.item

	override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
		val idString = StringBuilder()

		idString
			.append("enchanter/")
			.append(id.path)

		val id = ExcessiveUtilities.modResource(idString.toString())

		val advancement = recipeOutput.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
			.rewards(AdvancementRewards.Builder.recipe(id))
			.requirements(AdvancementRequirements.Strategy.OR)

		for (criterion in criteria) {
			advancement.addCriterion(criterion.key, criterion.value)
		}

		val recipe = EnchanterRecipe(leftIngredient, leftCount, rightIngredient, rightCount, feCost, ticks, result)

		recipeOutput.accept(id, recipe, advancement.build(id.withPrefix("recipes/")))
	}
}