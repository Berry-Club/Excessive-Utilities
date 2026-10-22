package dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.machine

import dev.aaronhowser.mods.excessive_utilities.recipe.machine.CrusherRecipe
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.core.registries.BuiltInRegistries

class CrusherRecipeBuilder(
	val ingredient: Ingredient,
	val primaryOutput: ItemStack,
	val secondaryOutput: ItemStack = ItemStack.EMPTY,
	val secondaryChance: Float = CrusherRecipe.DEFAULT_SECONDARY_CHANCE,
	val ticks: Int = CrusherRecipe.DEFAULT_TICKS,
	val fePerTick: Int = CrusherRecipe.DEFAULT_FE_PER_TICK
) : RecipeBuilder {

	private val criteria: MutableMap<String, Criterion<*>> = mutableMapOf()

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder {
		criteria[name] = criterion
		return this
	}

	override fun group(p0: String?): RecipeBuilder = error("Unsupported")
	override fun defaultId(): ResourceKey<Recipe<*>> = ResourceKey.create(Registries.RECIPE, BuiltInRegistries.ITEM.getKey(primaryOutput.item))

	override fun save(recipeOutput: RecipeOutput, recipeKey: ResourceKey<Recipe<*>>) {
		val originalId = recipeKey.identifier()
		val idString = StringBuilder()

		idString
			.append("crusher/")
			.append(originalId.path)

		val id =
			Identifier.fromNamespaceAndPath(
				originalId.namespace,
				idString.toString()
			)

		val advancement = recipeOutput.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(ResourceKey.create(Registries.RECIPE, id)))
			.rewards(AdvancementRewards.Builder.recipe(ResourceKey.create(Registries.RECIPE, id)))
			.requirements(AdvancementRequirements.Strategy.OR)

		for (criterion in criteria) {
			advancement.addCriterion(criterion.key, criterion.value)
		}

		val recipe = CrusherRecipe(ingredient, primaryOutput, secondaryOutput, secondaryChance, ticks, fePerTick)

		recipeOutput.accept(ResourceKey.create(Registries.RECIPE, id), recipe, advancement.build(id.withPrefix("recipes/")))
	}
}
