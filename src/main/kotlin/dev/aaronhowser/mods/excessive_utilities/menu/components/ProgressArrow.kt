package dev.aaronhowser.mods.excessive_utilities.menu.components

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth

class ProgressArrow(
	x: Int,
	y: Int,
	val font: Font,
	val percentDoneFunction: () -> Float,
	val shouldRenderProgress: () -> Boolean,
	val onClickFunction: (Double, Double, Int) -> Unit
) : AbstractWidget(
	x, y,
	RIGHT_WIDTH,
	RIGHT_HEIGHT,
	Component.empty()
) {

	override fun renderWidget(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
		if (!shouldRenderProgress()) return

		pGuiGraphics.blitSprite(
			TEXTURE,
			TEXTURE_SIZE, TEXTURE_SIZE,
			0, 0,
			this.x,
			this.y,
			this.width,
			Mth.floor(this.height * percentDoneFunction()),
		)

		if (isHovered) renderTooltip(pGuiGraphics, pMouseX, pMouseY)
	}

	private fun renderTooltip(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int) {
		if (percentDoneFunction() <= 0f) return

		val percentString = (percentDoneFunction() * 100).toInt().toString() + "%"

		pGuiGraphics.renderComponentTooltip(
			font,
			listOf(Component.literal(percentString)),
			pMouseX,
			pMouseY
		)
	}

	override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
		super.onClick(mouseX, mouseY, button)

		onClickFunction(mouseX, mouseY, button)
	}

	override fun updateWidgetNarration(pNarrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(pNarrationElementOutput)
	}

	companion object {
		const val TEXTURE_SIZE = 32

		val TEXTURE = ExcessiveUtilities.modResource("arrow_right")
		const val RIGHT_WIDTH = 24
		const val RIGHT_HEIGHT = 17
	}

}