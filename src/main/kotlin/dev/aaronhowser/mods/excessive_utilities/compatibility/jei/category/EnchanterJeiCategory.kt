package dev.aaronhowser.mods.excessive_utilities.compatibility.jei.category

import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.EnchanterRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder
import mezz.jei.api.helpers.IGuiHelper
import mezz.jei.api.recipe.IFocusGroup
import mezz.jei.api.recipe.RecipeType
import mezz.jei.api.recipe.category.AbstractRecipeCategory
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.RecipeHolder

class EnchanterJeiCategory(
	recipeType: RecipeType<RecipeHolder<EnchanterRecipe>>,
	val guiHelper: IGuiHelper
) : AbstractRecipeCategory<RecipeHolder<EnchanterRecipe>>(
	recipeType,
	ModBlocks.ENCHANTER.get().name,
	guiHelper.createDrawableItemLike(ModBlocks.ENCHANTER),
	120,
	40
) {

	override fun setRecipe(builder: IRecipeLayoutBuilder, recipe: RecipeHolder<EnchanterRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value()

		val leftInputSlot = builder.addInputSlot(9, 19)
		val rightInputSlot = builder.addInputSlot(33, 19)
		val outputSlot = builder.addOutputSlot(90, 19)

		val inputs = recipe.leftIngredient.items.map { it.copyWithCount(recipe.leftIngredient.count()) }
		leftInputSlot
			.addItemStacks(inputs)
			.setStandardSlotBackground()

		val rightInputs = recipe.rightIngredient.items.map { it.copyWithCount(recipe.rightIngredient.count()) }
		rightInputSlot
			.addItemStacks(rightInputs)
			.setStandardSlotBackground()

		outputSlot
			.addItemStack(recipe.output)
			.setOutputSlotBackground()
	}

	override fun createRecipeExtras(builder: IRecipeExtrasBuilder, recipe: RecipeHolder<EnchanterRecipe>, focuses: IFocusGroup) {
		val recipe = recipe.value()

		builder.addRecipeArrow().setPosition(55, 19)

		val formattedFePerTick = "%,d".format(recipe.fePerTick)
		val formattedTicks = "%,d".format(recipe.ticks)
		val formattedEnchantPower = recipe.enchantingPower.toString()

		builder.addText(
			ModMenuLang.FE_PER_TICK.toComponent(formattedFePerTick),
			80, 12
		).setPosition(9, 42).setColor(0xFF808080.toInt())

		builder.addText(
			ModMenuLang.TICKS.toComponent(formattedTicks),
			80, 12
		).setPosition(65, 42).setColor(0xFF808080.toInt())

		val enchantPowerText = Component.literal("${formattedEnchantPower}x ")
		val enchantWidth = Minecraft.getInstance().font.width(enchantPowerText)

		builder.addText(
			enchantPowerText,
			enchantWidth + 2, 12
		).setPosition(5, 4).setColor(0xFF808080.toInt())

		builder.addDrawable(
			guiHelper.createDrawableItemLike(Items.BOOKSHELF),
			5 + enchantWidth, 0
		)
	}

	override fun getRegistryName(recipe: RecipeHolder<EnchanterRecipe>): ResourceLocation = recipe.id

}