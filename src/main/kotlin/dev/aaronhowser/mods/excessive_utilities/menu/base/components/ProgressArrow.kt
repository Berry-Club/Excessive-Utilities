package dev.aaronhowser.mods.excessive_utilities.menu.base.components

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.input.MouseButtonEvent
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.util.Mth

class ProgressArrow(
	x: Int,
	y: Int,
	val font: Font,
	val texture: Identifier = TEXTURE,
	val percentDoneFunction: () -> Float,
	val shouldRenderProgress: () -> Boolean,
	val onClickFunction: (Double, Double, Int) -> Unit = { _, _, _ -> }
) : AbstractWidget(
	x, y,
	WIDTH,
	HEIGHT,
	Component.empty()
) {

	override fun extractWidgetRenderState(guiGraphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int, partialTick: Float) {
		if (!shouldRenderProgress()) return

		guiGraphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
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

	private fun renderTooltip(guiGraphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
		if (percentDoneFunction() <= 0f) return

		val percentString = (percentDoneFunction() * 100).toInt().toString() + "%"

		guiGraphics.setTooltipForNextFrame(
			font,
			listOf(Component.literal(percentString).visualOrderText),
			mouseX,
			mouseY
		)
	}

	override fun onClick(event: MouseButtonEvent, doubleClick: Boolean) {
		super.onClick(event, doubleClick)

		onClickFunction(event.x(), event.y(), event.button())
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
