package dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.crafting

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
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
import net.minecraft.world.item.crafting.ShapedRecipe
import net.minecraft.world.item.crafting.ShapedRecipePattern
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.level.ItemLike
import net.minecraft.core.registries.BuiltInRegistries

class SpecialShapedRecipeBuilder(
	val output: ItemStack
) : RecipeBuilder {

	constructor(
		output: ItemLike
	) : this(output.getDefaultInstance())

	private var type: String? = null
	private var factory: ((ShapedRecipePattern, ItemStack) -> dev.aaronhowser.mods.excessive_utilities.recipe.crafting.SpecialShapedRecipe)? = null

	private val rows = mutableListOf<String>()
	private val key = mutableMapOf<Char, Ingredient>()
	private val criteria = mutableMapOf<String, Criterion<*>>()

	fun type(
		type: String,
		factory: (ShapedRecipePattern, ItemStack) -> dev.aaronhowser.mods.excessive_utilities.recipe.crafting.SpecialShapedRecipe
	): SpecialShapedRecipeBuilder {
		this.type = type
		this.factory = factory
		return this
	}

	fun define(symbol: Char, ingredient: Ingredient): SpecialShapedRecipeBuilder {
		key[symbol] = ingredient
		return this
	}

	fun pattern(vararg rows: String): SpecialShapedRecipeBuilder {
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

	override fun group(groupName: String?): RecipeBuilder = error("Not supported")

	override fun defaultId(): ResourceKey<Recipe<*>> = ResourceKey.create(Registries.RECIPE, BuiltInRegistries.ITEM.getKey(output.item))

	override fun save(recipeOutput: RecipeOutput, recipeKey: ResourceKey<Recipe<*>>) {
		val originalId = recipeKey.identifier()
		val idString = StringBuilder()

		val type = type!!
		val factory = factory!!

		idString
			.append(type)
			.append("/")
			.append(originalId.path)

		val id = ExcessiveUtilities.modResource(idString.toString())

		val advancement = recipeOutput.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(ResourceKey.create(Registries.RECIPE, id)))
			.rewards(AdvancementRewards.Builder.recipe(ResourceKey.create(Registries.RECIPE, id)))
			.requirements(AdvancementRequirements.Strategy.OR)

		for (criterion in criteria) {
			advancement.addCriterion(criterion.key, criterion.value)
		}

		val pattern = ShapedRecipePattern.of(key, rows)
		val recipe = factory(pattern, output)
		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), recipe, advancement.build(id.withPrefix("recipes/")))
	}

}
