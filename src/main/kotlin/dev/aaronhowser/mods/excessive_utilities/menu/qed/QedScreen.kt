package dev.aaronhowser.mods.excessive_utilities.menu.qed

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.components.ProgressArrow
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class QedScreen(
	menu: QedMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<QedMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val inventoryLabelOffsetY: Int = 12

	private lateinit var progressArrow: ProgressArrow

	override fun baseInit() {
		progressArrow = ProgressArrow(
			x = leftPos + 90,
			y = topPos + 41,
			font = font,
			percentDoneFunction = { menu.getProgress().toFloat() / menu.getMaxProgress() },
			shouldRenderProgress = { menu.getProgress() > 0 }
		)

		addRenderableWidget(progressArrow)
	}

	override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
		super.renderLabels(guiGraphics, mouseX, mouseY)

		val nearbyCrystals = menu.getAmountNearbyCrystals()
		guiGraphics.drawString(
			font,
			Component.literal(nearbyCrystals.toString() + "x"),
			95,
			30,
			4210752,
			false
		)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/qed.png"), 176, 180)
	}

}