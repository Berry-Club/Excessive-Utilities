package dev.aaronhowser.mods.excessive_utilities.menu.components

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth

class ProgressArrow(
	x: Int,
	y: Int,
	val font: Font,
	val texture: ResourceLocation = TEXTURE,
	val percentDoneFunction: () -> Float,
	val shouldRenderProgress: () -> Boolean,
	val onClickFunction: (Double, Double, Int) -> Unit = { _, _, _ -> }
) : AbstractWidget(
	x, y,
	WIDTH,
	HEIGHT,
	Component.empty()
) {

	override fun renderWidget(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
		if (!shouldRenderProgress()) return

		guiGraphics.blitSprite(
			texture,
			WIDTH, HEIGHT,
			0, 0,
			this.x,
			this.y,
			Mth.ceil(this.width * percentDoneFunction()),
			this.height,
		)

		if (isHovered) renderTooltip(guiGraphics, mouseX, mouseY)
	}

	private fun renderTooltip(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
		if (percentDoneFunction() <= 0f) return

		val percentString = (percentDoneFunction() * 100).toInt().toString() + "%"

		guiGraphics.renderComponentTooltip(
			font,
			listOf(Component.literal(percentString)),
			mouseX,
			mouseY
		)
	}

	override fun onClick(mouseX: Double, mouseY: Double, button: Int) {
		super.onClick(mouseX, mouseY, button)

		onClickFunction(mouseX, mouseY, button)
	}

	override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
		return this.defaultButtonNarrationText(narrationElementOutput)
	}

	companion object {
		const val TEXTURE_SIZE = 32

		val TEXTURE = ExcessiveUtilities.modResource("arrow_right")
		const val WIDTH = 22
		const val HEIGHT = 16
	}

}