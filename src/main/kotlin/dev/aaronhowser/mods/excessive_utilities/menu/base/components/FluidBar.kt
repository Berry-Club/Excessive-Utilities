package dev.aaronhowser.mods.excessive_utilities.menu.base.components

import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.client.Minecraft
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.AtlasIds
import net.minecraft.resources.Identifier
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Supplier

class FluidBar(
	x: Int,
	y: Int,
	val capacityGetter: Supplier<Int>,
	val fluidGetter: Supplier<FluidStack>,
	val font: Font
) : AbstractWidget(x, y, WIDTH, HEIGHT, Component.empty()) {

	override fun extractWidgetRenderState(
		guiGraphics: GuiGraphicsExtractor,
		mouseX: Int,
		mouseY: Int,
		partialTick: Float
	) {
		val fluid = fluidGetter.get()
		val capacity = capacityGetter.get().coerceAtLeast(1)

		if (!fluid.isEmpty) {
			val percent = (fluid.amount.toFloat() / capacity.toFloat()).coerceIn(0f, 1f)
			val filledHeight = Mth.ceil(HEIGHT * percent).coerceAtMost(HEIGHT)
			drawFluid(guiGraphics, fluid, x, y + HEIGHT - filledHeight, WIDTH, filledHeight)
		}

		if (isHovered) renderTooltip(guiGraphics, mouseX, mouseY)
	}

	private fun drawFluid(
		guiGraphics: GuiGraphicsExtractor,
		fluid: FluidStack,
		x: Int,
		y: Int,
		width: Int,
		height: Int
	) {
        val model = Minecraft.getInstance().modelManager.fluidStateModelSet.get(fluid.fluid.defaultFluidState())
        val sprite = model.stillMaterial().sprite()
        val color = model.fluidTintSource()?.colorAsStack(fluid) ?: -1

		guiGraphics.enableScissor(x, y, x + width, y + height)
		val tileSize = 16
		var drawnY = 0
		while (drawnY < height) {
			var drawnX = 0
			while (drawnX < width) {
				val tileWidth = (width - drawnX).coerceAtMost(tileSize)
				val tileHeight = (height - drawnY).coerceAtMost(tileSize)
				guiGraphics.blitSprite(
					RenderPipelines.GUI_TEXTURED,
					sprite,
					x + drawnX,
					y + height - drawnY - tileSize,
					tileSize,
					tileSize,
					color
				)
				drawnX += tileWidth
			}
			drawnY += (height - drawnY).coerceAtMost(tileSize)
		}
		guiGraphics.disableScissor()
	}

	private fun renderTooltip(guiGraphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
		val fluid = fluidGetter.get()
		val capacity = capacityGetter.get()
		val amountString = String.format("%,d", fluid.amount)
		val capacityString = String.format("%,d", capacity)

		val lines = mutableListOf<Component>()
		if (fluid.isEmpty) {
			lines.add(Component.literal("Empty"))
		} else {
			lines.add(fluid.hoverName)
		}
		lines.add(Component.literal("$amountString / $capacityString mB"))

		guiGraphics.setTooltipForNextFrame(font, lines.map { it.visualOrderText }, mouseX, mouseY)
	}

	override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
		this.defaultButtonNarrationText(narrationElementOutput)
	}

	companion object {
		const val WIDTH = 14
		const val HEIGHT = 29
	}
}
