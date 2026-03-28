package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category.generator_fuel

import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.generator_fuel.MagmaticFuelRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeHolder

class MagmaticFuelJeiCategory(
	recipeType: RecipeType<RecipeHolder<MagmaticFuelRecipe>>,
	guiHelper: IGuiHelper
) : AbstractRecipeCategory<RecipeHolder<MagmaticFuelRecipe>>(
	recipeType,
	ModBlocks.MAGMATIC_GENERATOR.get().name,
	guiHelper.createDrawableItemLike(ModBlocks.MAGMATIC_GENERATOR),
	120,
	40
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<MagmaticFuelRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value

		val inputSlot = builder.addInputSlot(9, 9)
			.setStandardSlotBackground()
			.setFluidRenderer(1, false, 16, 16)

		val fluidIngredient = recipe.fluidIngredient

		for (fluid in fluidIngredient.fluids) {
			inputSlot
				.addFluidStack(fluid.fluid, fluid.amount.toLong())
		}
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<MagmaticFuelRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value

		val fePerTick = recipe.fePerTick
		val duration = recipe.duration
		val totalFe = fePerTick * duration

		val formattedFePerTick = "%,d".format(fePerTick)
		val formattedDuration = "%,d".format(duration)
		val formattedFeTotal = "%,d".format(totalFe)

		builder.addText(
			ModMenuLang.FE_PER_TICK.toComponent(formattedFePerTick),
			200, 12
		).setPosition(32, 1).setColor(0xFF808080.toInt())

		builder.addText(
			ModMenuLang.TICKS.toComponent(formattedDuration),
			200, 12
		).setPosition(32, 16).setColor(0xFF808080.toInt())

		builder.addText(
			ModMenuLang.FE.toComponent(formattedFeTotal),
			200, 12
		).setPosition(32, 31).setColor(0xFF808080.toInt())

	}

	override fun getRegistryName(recipe: RecipeHolder<MagmaticFuelRecipe>): ResourceLocation = recipe.id()

}