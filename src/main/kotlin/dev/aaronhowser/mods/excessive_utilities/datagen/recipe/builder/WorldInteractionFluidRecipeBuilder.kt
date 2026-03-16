package dev.aaronhowser.mods.excessive_utilities.datagen.recipe.builder

import dev.aaronhowser.mods.excessive_utilities.recipe.WorldInteractionFluidRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.WorldInteractionItemRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.base.BlockStateIngredient
import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.data.recipes.RecipeBuilder
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.neoforged.neoforge.fluids.FluidStack
import java.util.*

class WorldInteractionFluidRecipeBuilder(
	val requiredOnBlock: BlockStateIngredient?,
	val requiredAdjacentBlocks: List<BlockStateIngredient>,
	val requiredBlockBehind: BlockStateIngredient?,
	val output: FluidStack
) : RecipeBuilder {

	override fun unlockedBy(name: String, criterion: Criterion<*>): RecipeBuilder = error("Unsupported")
	override fun group(groupName: String?): RecipeBuilder = error("Unsupported")
	override fun getResult(): Item = Items.AIR

	override fun save(recipeOutput: RecipeOutput, id: ResourceLocation) {
		val path = StringBuilder()
			.append("world_interaction_upgrade/fluid/")
			.append(id.path)
			.toString()

		val realId = ResourceLocation.fromNamespaceAndPath(id.namespace, path)

		val advancement = recipeOutput.advancement()
			.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(realId))
			.rewards(AdvancementRewards.Builder.recipe(realId))
			.requirements(AdvancementRequirements.Strategy.OR)

		val recipe = WorldInteractionFluidRecipe(
			Optional.ofNullable(requiredOnBlock),
			requiredAdjacentBlocks,
			Optional.ofNullable(requiredBlockBehind),
			output
		)

		recipeOutput.accept(
			realId,
			recipe,
			advancement.build(realId.withPrefix("recipes/world_interaction_upgrade/fluid/"))
		)
	}

}