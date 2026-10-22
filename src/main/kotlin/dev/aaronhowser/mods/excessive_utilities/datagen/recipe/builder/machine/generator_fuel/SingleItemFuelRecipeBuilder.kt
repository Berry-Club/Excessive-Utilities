package dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.machine.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.SingleItemFuelRecipe
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
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe

class SingleItemFuelRecipeBuilder(
	val generatorType: SingleItemFuelRecipe.GeneratorType,
	val ingredient: Ingredient,
	val fePerTick: Int,
	val duration: Int
) : RecipeBuilder {

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = error("Unsupported")
	override fun group(groupName: String?): RecipeBuilder = error("Unsupported")
	override fun defaultId(): ResourceKey<Recipe<*>> = error("Generator fuel recipes require an explicit id")

	override fun save(recipeOutput: RecipeOutput, recipeKey: ResourceKey<Recipe<*>>) {
		val id = recipeKey.identifier()
		val path = StringBuilder()
			.append("generator_fuel/")
			.append(generatorType.id)
			.append("/")
			.append(id.path)
			.toString()

		val realId = Identifier.fromNamespaceAndPath(id.namespace, path)

		val advancement = recipeOutput.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(ResourceKey.create(Registries.RECIPE, realId)))
			.rewards(AdvancementRewards.Builder.recipe(ResourceKey.create(Registries.RECIPE, realId)))
			.requirements(AdvancementRequirements.Strategy.OR)

		val recipe = SingleItemFuelRecipe(generatorType, ingredient, fePerTick, duration)

		recipeOutput.accept(
			ResourceKey.create(Registries.RECIPE, realId),
			recipe,
			advancement.build(realId.withPrefix("recipes/generator_fuel/${generatorType.id}/"))
		)
	}

}
