package dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.machine.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.MagmaticFuelRecipe
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
import net.minecraft.world.item.crafting.Recipe
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient

class MagmaticFuelRecipeBuilder(
	val fluidIngredient: SizedFluidIngredient,
	val fePerTick: Int,
	val duration: Int
) : RecipeBuilder {

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = error("Unsupported")
	override fun group(groupName: String?): RecipeBuilder = error("Unsupported")
	override fun defaultId(): ResourceKey<Recipe<*>> = error("Magmatic fuel recipes require an explicit id")

	override fun save(recipeOutput: RecipeOutput, recipeKey: ResourceKey<Recipe<*>>) {
		val id = recipeKey.identifier()
		val path = StringBuilder()
			.append("generator_fuel/magmatic/")
			.append(id.path)
			.toString()

		val realId = Identifier.fromNamespaceAndPath(id.namespace, path)

		val advancement = recipeOutput.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(ResourceKey.create(Registries.RECIPE, realId)))
			.rewards(AdvancementRewards.Builder.recipe(ResourceKey.create(Registries.RECIPE, realId)))
			.requirements(AdvancementRequirements.Strategy.OR)

		val recipe = MagmaticFuelRecipe(fluidIngredient, fePerTick, duration)

		recipeOutput.accept(
			ResourceKey.create(Registries.RECIPE, realId),
			recipe,
			advancement.build(realId.withPrefix("recipes/generator_fuel/magmatic/"))
		)
	}

}
