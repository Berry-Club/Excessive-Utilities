package dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.machine

import dev.aaronhowser.mods.excessive_utilities.recipe.machine.CrusherRecipe
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
import kotlin.collections.iterator

class CrusherRecipeBuilder(
	val ingredient: Ingredient,
	val primaryOutput: ItemStack,
	val secondaryOutput: ItemStack = ItemStack.EMPTY,
	val secondaryChance: Float = 0f
) : RecipeBuilder {

	private val criteria: MutableMap<String, Criterion<*>> = mutableMapOf()

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder {
		criteria[name] = criterion
		return this
	}

	override fun group(p0: String?): RecipeBuilder = error("Unsupported")
	override fun getResult(): Item = primaryOutput.item

	override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
		val idString = StringBuilder()

		idString
			.append("crusher/")
			.append(id.path)

		val id =
			ResourceLocation.fromNamespaceAndPath(
				id.namespace,
				idString.toString()
			)

		val advancement = recipeOutput.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
			.rewards(AdvancementRewards.Builder.recipe(id))
			.requirements(AdvancementRequirements.Strategy.OR)

		for (criterion in criteria) {
			advancement.addCriterion(criterion.key, criterion.value)
		}

		val recipe = CrusherRecipe(ingredient, primaryOutput, secondaryOutput, secondaryChance)

		recipeOutput.accept(id, recipe, advancement.build(id.withPrefix("recipes/")))
	}
}