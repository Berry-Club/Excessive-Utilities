package dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.crafting

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.recipe.crafting.ShapedUnstableRecipe
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
import net.minecraft.world.item.crafting.ShapedRecipePattern
import net.minecraft.world.level.ItemLike
import kotlin.collections.iterator

class ShapedUnstableRecipeBuilder(
	val output: ItemStack
) : RecipeBuilder {

	constructor(output: ItemLike) : this(output.getDefaultInstance())

	private val rows = mutableListOf<String>()
	private val key = mutableMapOf<Char, Ingredient>()
	private val criteria = mutableMapOf<String, Criterion<*>>()

	fun define(symbol: Char, ingredient: Ingredient): ShapedUnstableRecipeBuilder {
		key[symbol] = ingredient
		return this
	}

	fun pattern(vararg rows: String): ShapedUnstableRecipeBuilder {
		this.rows.addAll(rows)

		val width = rows.first().length
		if (rows.any { it.length != width }) {
			throw IllegalArgumentException("All rows must have the same length")
		}

		return this
	}

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder {
		criteria[name] = criterion
		return this
	}

	override fun group(groupName: String?): RecipeBuilder {
		error("UnstableIngotRecipe does not support recipe groups")
	}

	override fun getResult(): Item = output.item

	override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
		val idString = StringBuilder()

		idString
			.append("unstable_ingot/")
			.append(id.path)

		val id = ExcessiveUtilities.Companion.modResource(idString.toString())

		val advancement = recipeOutput.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
			.rewards(AdvancementRewards.Builder.recipe(id))
			.requirements(AdvancementRequirements.Strategy.OR)

		for (criterion in criteria) {
			advancement.addCriterion(criterion.key, criterion.value)
		}

		val pattern = ShapedRecipePattern.of(key, rows)
		val recipe = ShapedUnstableRecipe(pattern, output)
		recipeOutput.accept(id, recipe, advancement.build(id.withPrefix("recipes/unstable_ingot/")))
	}

}