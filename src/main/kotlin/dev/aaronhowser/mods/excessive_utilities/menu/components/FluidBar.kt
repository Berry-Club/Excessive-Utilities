package dev.aaronhowser.mods.excessive_utilities.menu.components

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.AbstractWidget
import net.minecraft.client.gui.narration.NarrationElementOutput
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.inventory.InventoryMenu
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions
import net.neoforged.neoforge.fluids.FluidStack
import java.util.function.Supplier

class FluidBar(
	x: Int,
	y: Int,
	val capacityGetter: Supplier<Int>,
	val fluidGetter: Supplier<FluidStack>,
	val font: Font
) : AbstractWidget(x, y, WIDTH, HEIGHT, Component.empty()) {

	override fun renderWidget(
		guiGraphics: GuiGraphics,
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
		guiGraphics: GuiGraphics,
		fluid: FluidStack,
		x: Int,
		y: Int,
		width: Int,
		height: Int
	) {
		val extensions = IClientFluidTypeExtensions.of(fluid.fluid)
		val textureLocation = extensions.getStillTexture(fluid) ?: return
		val tintColor = extensions.getTintColor(fluid)

		val sprite = Minecraft.getInstance()
			.getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
			.apply(textureLocation)

		val r = (tintColor shr 16 and 0xFF) / 255.0f
		val g = (tintColor shr 8 and 0xFF) / 255.0f
		val b = (tintColor and 0xFF) / 255.0f
		val a = ((tintColor shr 24 and 0xFF) / 255.0f).let { if (it == 0.0f) 1.0f else it }

		RenderSystem.enableBlend()
		RenderSystem.setShaderColor(r, g, b, a)

		val tileSize = 16
		var drawnHeight = 0
		while (drawnHeight < height) {
			val currentTileHeight = (height - drawnHeight).coerceAtMost(tileSize)
			var drawnWidth = 0
			while (drawnWidth < width) {
				val currentTileWidth = (width - drawnWidth).coerceAtMost(tileSize)
				guiGraphics.blit(
					x + drawnWidth,
					y + height - drawnHeight - currentTileHeight,
					0,
					currentTileWidth,
					currentTileHeight,
					sprite
				)
				drawnWidth += currentTileWidth
			}
			drawnHeight += currentTileHeight
		}

		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f)
		RenderSystem.disableBlend()
	}

	private fun renderTooltip(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
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

		guiGraphics.renderComponentTooltip(font, lines, mouseX, mouseY)
	}

	override fun updateWidgetNarration(narrationElementOutput: NarrationElementOutput) {
		this.defaultButtonNarrationText(narrationElementOutput)
	}

	companion object {
		const val WIDTH = 18
		const val HEIGHT = 57
	}
}
