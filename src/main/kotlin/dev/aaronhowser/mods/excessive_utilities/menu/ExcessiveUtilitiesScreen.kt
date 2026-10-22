package dev.aaronhowser.mods.excessive_utilities.menu

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu

abstract class ExcessiveUtilitiesScreen<M : AbstractContainerMenu>(
	menu: M,
	playerInventory: Inventory,
	title: Component,
	background: ScreenBackground
) : BaseScreen<M>(menu, playerInventory, title, background) {

	override val titleLabelOffsetX: Int = 4
	override val titleLabelOffsetY: Int = 4

	override val inventoryLabelOffsetY: Int = -5
	override val inventoryLabelOffsetX: Int = -4

	protected open val titleLabelSprite: Identifier = PLAIN_LABEL

	override fun extractLabels(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
		if (showTitleLabel) {
			drawTexturedLabel(
				graphics,
				title,
				titleLabelOffsetX,
				titleLabelOffsetY,
				titleLabelSprite
			)
		}

		if (showInventoryLabel) {
			drawTexturedLabel(
				graphics,
				playerInventoryTitle,
				inventoryLabelX + inventoryLabelOffsetX,
				inventoryLabelY + inventoryLabelOffsetY,
				INVENTORY_LABEL
			)
		}
	}

	private fun drawTexturedLabel(
		graphics: GuiGraphicsExtractor,
		message: Component,
		x: Int,
		y: Int,
		backgroundSprite: Identifier
	) {
		val horizontalPadding = 3
		val verticalPadding = 2
		val width = font.width(message) + horizontalPadding * 2
		val height = font.lineHeight + verticalPadding * 2

		graphics.blitSprite(RenderPipelines.GUI_TEXTURED, backgroundSprite, x, y, width, height)
		graphics.text(
			font,
			message,
			x + horizontalPadding,
			y + verticalPadding,
			LABEL_TEXT_COLOR,
			false
		)
	}

	companion object {
		// Opaque ARGB 0xFF404040; GUI text with zero alpha is skipped.
		const val LABEL_TEXT_COLOR: Int = -0xBFBFC0

		val BLUE_LABEL: Identifier =
			ExcessiveUtilities.modResource("label/blue")
		val BRIGHT_TEAL_LABEL: Identifier =
			ExcessiveUtilities.modResource("label/bright_teal")
		val DARK_RED_LABEL: Identifier =
			ExcessiveUtilities.modResource("label/dark_red")
		val PLAIN_LABEL: Identifier =
			ExcessiveUtilities.modResource("label/plain")
		val RED_LABEL: Identifier =
			ExcessiveUtilities.modResource("label/red")
		val TEAL_LABEL: Identifier =
			ExcessiveUtilities.modResource("label/teal")
		val YELLOW_LABEL: Identifier =
			ExcessiveUtilities.modResource("label/yellow")
		val INVENTORY_LABEL: Identifier =
			ExcessiveUtilities.modResource("label/inventory")
	}

}
