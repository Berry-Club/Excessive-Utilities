package dev.aaronhowser.mods.excessive_utilities.menu.qed

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.ExcessiveUtilitiesScreen
import dev.aaronhowser.mods.excessive_utilities.menu.base.components.ProgressArrow
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.entity.player.Inventory

class QedScreen(
	menu: QedMenu,
	playerInventory: Inventory,
	title: Component
) : ExcessiveUtilitiesScreen<QedMenu>(menu, playerInventory, title, BACKGROUND) {
	override val titleLabelSprite: Identifier = TEAL_LABEL

	override fun baseInit() {
		super.baseInit()

		val progressArrow = ProgressArrow(
			x = leftPos + 90,
			y = topPos + 41,
			font = font,
			percentDoneFunction = { menu.getProgress().toFloat() / menu.getMaxProgress() },
			shouldRenderProgress = { menu.getProgress() > 0 }
		)

		addRenderableWidget(progressArrow)
	}

	override fun extractLabels(graphics: GuiGraphicsExtractor, mouseX: Int, mouseY: Int) {
		super.extractLabels(graphics, mouseX, mouseY)

		val nearbyCrystals = menu.getAmountNearbyCrystals()
		graphics.text(
			font,
			Component.literal(nearbyCrystals.toString() + "x"),
			95,
			30,
			LABEL_TEXT_COLOR,
			false
		)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/qed.png"), 176, 180)
	}

}
