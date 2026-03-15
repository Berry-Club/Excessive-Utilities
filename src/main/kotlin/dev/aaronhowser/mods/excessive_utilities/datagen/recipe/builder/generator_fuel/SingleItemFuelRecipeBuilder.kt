package dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.recipe.generator_fuel.SingleItemFuelRecipe
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.Ingredient

class SingleItemFuelRecipeBuilder(
	val generatorType: SingleItemFuelRecipe.GeneratorType,
	val ingredient: Ingredient,
	val fePerTick: Int,
	val duration: Int
) : RecipeBuilder {

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = error("Unsupported")
	override fun group(groupName: String?): RecipeBuilder = error("Unsupported")
	override fun getResult(): Item = Items.AIR

	override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
		val path = StringBuilder()
			.append("generator_fuel/")
			.append(generatorType.id)
			.append("/")
			.append(id.path)
			.toString()

		val realId = ResourceLocation.fromNamespaceAndPath(id.namespace, path)

		val advancement = recipeOutput.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(realId))
			.rewards(AdvancementRewards.Builder.recipe(realId))
			.requirements(AdvancementRequirements.Strategy.OR)

		val recipe = SingleItemFuelRecipe(generatorType, ingredient, fePerTick, duration)

		recipeOutput.accept(
			realId,
			recipe,
			advancement.build(realId.withPrefix("recipes/generator_fuel/${generatorType.id}/"))
		)
	}

}