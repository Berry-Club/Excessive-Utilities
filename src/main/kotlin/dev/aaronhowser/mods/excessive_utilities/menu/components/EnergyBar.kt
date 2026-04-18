package dev.aaronhowser.mods.excessive_utilities.menu.components

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import java.util.function.IntSupplier

class EnergyBar(
	x: Int,
	y: Int,
	val maxGetter: IntSupplier,
	val currentGetter: IntSupplier,
	val font: Font,
	val isSmall: Boolean = false
) : AbstractWidget(
	x, y,
	if (isSmall) WIDTH_SMALL else WIDTH_BIG,
	if (isSmall) HEIGHT_SMALL else HEIGHT_BIG,
	Component.empty()
) {

	override fun renderWidget(
		guiGraphics: GuiGraphics,
		mouseX: Int,
		mouseY: Int,
		partialTick: Float
	) {
		val percentFull = currentGetter.asInt.toFloat() / maxGetter.asInt.toFloat()

		val energyTotalHeight = this.height
		val energyCurrentHeight = Mth.ceil(energyTotalHeight.toDouble() * percentFull)

		if (isSmall) {
			guiGraphics.blitSprite(
				TEXTURE,
				WIDTH_SMALL,
				HEIGHT_SMALL,
				0,
				energyTotalHeight - energyCurrentHeight,
				x,
				y + energyTotalHeight - energyCurrentHeight,
				WIDTH_SMALL,
				energyCurrentHeight
			)
		} else {
			guiGraphics.blitSprite(
				TEXTURE,
				WIDTH_BIG,
				HEIGHT_BIG,
				0,
				energyTotalHeight - energyCurrentHeight,
				x,
				y + energyTotalHeight - energyCurrentHeight,
				WIDTH_BIG,
				energyCurrentHeight
			)
		}

		if (isHovered) renderTooltip(guiGraphics, mouseX, mouseY)
	}

	private fun renderTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
		val currentAmountString = String.format("%,d", currentGetter.asInt)
		val maxAmountString = String.format("%,d", maxGetter.asInt)

		val component = ModMenuLang.FE_WITH_CAPACITY.toComponent(currentAmountString, maxAmountString)

		pGuiGraphics.renderComponentTooltip(
			font,
			listOf(component),
			pMouseX,
			pMouseY
		)
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}

	companion object {
		val TEXTURE = ExcessiveUtilities.modResource("energy")

		const val WIDTH_BIG = 18
		const val HEIGHT_BIG = 57
		const val WIDTH_SMALL = 18
		const val HEIGHT_SMALL = 33
	}

}