package dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.machine

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.QedRecipe
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.ShapedRecipePattern
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.core.registries.BuiltInRegistries

class QedRecipeBuilder(
	val result: ItemStack,
	val count: Int = 1,
	val crystalTicks: Int = 20 * 5 * 3
) : RecipeBuilder {

	private val criteria: MutableMap<String, Criterion<*>> = mutableMapOf()
	private val keys: MutableMap<Char, Ingredient> = mutableMapOf()
	private val rows: MutableList<String> = mutableListOf()

	fun define(key: Char, ingredient: Ingredient): QedRecipeBuilder {
		keys[key] = ingredient
		return this
	}

	fun pattern(vararg rows: String): QedRecipeBuilder {
		this.rows.addAll(rows)
		return this
	}

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder {
		criteria[name] = criterion
		return this
	}

	override fun group(p0: String?): RecipeBuilder {
		error("Unsupported")
	}

	override fun defaultId(): ResourceKey<Recipe<*>> = ResourceKey.create(Registries.RECIPE, BuiltInRegistries.ITEM.getKey(result.item))

	override fun save(recipeOutput: RecipeOutput, recipeKey: ResourceKey<Recipe<*>>) {
		val originalId = recipeKey.identifier()
		val idString = StringBuilder()

		idString
			.append("qed/")
			.append(originalId.path)

		val id = ExcessiveUtilities.modResource(idString.toString())

		val advancement = recipeOutput.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(ResourceKey.create(Registries.RECIPE, id)))
			.rewards(AdvancementRewards.Builder.recipe(ResourceKey.create(Registries.RECIPE, id)))
			.requirements(AdvancementRequirements.Strategy.OR)

		for (criterion in criteria) {
			advancement.addCriterion(criterion.key, criterion.value)
		}

		val pattern = ShapedRecipePattern.of(keys, rows)

		val recipe = QedRecipe(pattern, result, crystalTicks)
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), recipe, advancement.build(id.withPrefix("recipes/")))
	}
}
