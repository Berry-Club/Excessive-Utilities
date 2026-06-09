package dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder.machine

import dev.aaronhowser.mods.excessive_utilities.recipe.machine.EnchanterRecipe
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.Ingredient
import net.neoforged.neoforge.common.crafting.SizedIngredient

class EnchanterRecipeBuilder(
	private val result: ItemStack
) : RecipeBuilder {

	private var leftIngredient: Ingredient? = null
	private var leftCount: Int? = null
	private var rightIngredient: Ingredient? = null
	private var rightCount: Int? = null
	private var fePerTick: Int? = null
	private var ticks: Int? = null
	private var enchantingPower: Float = 15f

	private val criteria: MutableMap<String, Criterion<*>> = mutableMapOf()

	fun left(
		ingredient: Ingredient,
		count: Int = 1
	): EnchanterRecipeBuilder {
		leftIngredient = ingredient
		leftCount = count

		return this
	}

	fun right(
		ingredient: Ingredient,
		count: Int = 1
	): EnchanterRecipeBuilder {
		rightIngredient = ingredient
		rightCount = count

		return this
	}

	fun fePerTick(fePerTick: Int): EnchanterRecipeBuilder {
		this.fePerTick = fePerTick
		return this
	}

	fun ticks(ticks: Int): EnchanterRecipeBuilder {
		this.ticks = ticks
		return this
	}

	fun enchantingPower(enchantingPower: Float): EnchanterRecipeBuilder {
		this.enchantingPower = enchantingPower
		return this
	}

	fun costAndDuration(
		feCost: Int,
		durationTicks: Int
	): EnchanterRecipeBuilder {
		this.fePerTick = Mth.ceil(feCost / durationTicks.toDouble())
		this.ticks = durationTicks
		return this
	}

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

		val recipe = EnchanterRecipe(
			SizedIngredient(leftIngredient!!, leftCount!!),
			SizedIngredient(rightIngredient!!, rightCount!!),
			fePerTick!!,
			ticks!!,
			enchantingPower,
			result
		)

		recipeOutput.accept(id, recipe, advancement.build(id.withPrefix("recipes/")))
	}
}