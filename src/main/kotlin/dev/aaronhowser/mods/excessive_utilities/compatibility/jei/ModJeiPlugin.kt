package dev.aaronhowser.mods.excessive_utilities.compatibility.jei

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.CrusherRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.EnchanterRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.QedRecipe
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.ResonatorRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.IModPlugin
import mezz.jei.api.JeiPlugin
import mezz.jei.api.constants.RecipeTypes
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.registration.IRecipeCatalystRegistration
import mezz.jei.api.registration.IRecipeCategoryRegistration
import mezz.jei.api.registration.IRecipeRegistration
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeHolder

@JeiPlugin
class ModJeiPlugin : IModPlugin {

	override fun getPluginUid(): ResourceLocation = ID

	override fun registerRecipeCatalysts(registration: IRecipeCatalystRegistration) {
		registration.addRecipeCatalyst(ModBlocks.FURNACE, RecipeTypes.SMELTING)
		registration.addRecipeCatalyst(ModBlocks.CRUSHER, CRUSHER)
		registration.addRecipeCatalyst(ModBlocks.ENCHANTER, ENCHANTER)
		registration.addRecipeCatalyst(ModBlocks.QED, QED)
		registration.addRecipeCatalyst(ModBlocks.RESONATOR, RESONATOR)
	}

	override fun registerCategories(registration: IRecipeCategoryRegistration) {
		val guiHelper = registration.jeiHelpers.guiHelper

		registration.addRecipeCategories(
			EnchanterJeiCategory(ENCHANTER, guiHelper)
		)
	}

	override fun registerRecipes(registration: IRecipeRegistration) {
		val level = AaronClientUtil.localLevel ?: return

		val enchanterRecipes = EnchanterRecipe
			.getAllRecipes(level.recipeManager)
			.map(RecipeHolder<EnchanterRecipe>::value)
		registration.addRecipes(ENCHANTER, enchanterRecipes)

		val crusherRecipes = CrusherRecipe
			.getAllRecipes(level.recipeManager)
			.map(RecipeHolder<CrusherRecipe>::value)
		registration.addRecipes(CRUSHER, crusherRecipes)

		val qedRecipes = QedRecipe
			.getAllRecipes(level.recipeManager)
			.map(RecipeHolder<QedRecipe>::value)
		registration.addRecipes(QED, qedRecipes)

		val resonatorRecipes = ResonatorRecipe
			.getAllRecipes(level.recipeManager)
			.map(RecipeHolder<ResonatorRecipe>::value)
		registration.addRecipes(RESONATOR, resonatorRecipes)
	}

	companion object {
		val ID = ExcessiveUtilities.modResource("jei_plugin")

		val CRUSHER: RecipeType<CrusherRecipe> = makeRecipeType("crusher", CrusherRecipe::class.java)
		val ENCHANTER: RecipeType<EnchanterRecipe> = makeRecipeType("enchanter", EnchanterRecipe::class.java)
		val QED: RecipeType<QedRecipe> = makeRecipeType("qed", QedRecipe::class.java)
		val RESONATOR: RecipeType<ResonatorRecipe> = makeRecipeType("resonator", ResonatorRecipe::class.java)

		private fun <T : Recipe<*>> makeRecipeType(id: String, clazz: Class<out T>): RecipeType<T> {
			return RecipeType.create<T>(ExcessiveUtilities.MOD_ID, id, clazz)
		}
	}

}