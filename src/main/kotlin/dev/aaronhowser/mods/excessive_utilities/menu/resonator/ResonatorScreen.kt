package dev.aaronhowser.mods.excessive_utilities.menu.resonator

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.ClientGridPower
import dev.aaronhowser.mods.excessive_utilities.menu.components.ProgressArrow
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class ResonatorScreen(
	menu: ResonatorMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<ResonatorMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val inventoryLabelOffsetY: Int = 4

	private lateinit var progressArrow: ProgressArrow

	override fun baseInit() {
		progressArrow = ProgressArrow(
			x = leftPos + 73,
			y = topPos + 37,
			font = font,
			texture = PROGRESS_ARROW_TEXTURE,
			percentDoneFunction = { menu.getProgress().toFloat() / menu.getMaxProgress() },
			shouldRenderProgress = { menu.getProgress() > 0 }
		)

		addRenderableWidget(progressArrow)
	}

	override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
		super.renderLabels(guiGraphics, mouseX, mouseY)

		val gpUsage = ClientGridPower.format(menu.getGpUsage())

		guiGraphics.drawString(
			font,
			ModMenuLang.GP.toComponent(gpUsage),
			71,
			22,
			4210752,
			false
		)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/resonator.png"), 176, 172)
		val PROGRESS_ARROW_TEXTURE = ExcessiveUtilities.modResource("resonator_arrow_right")
	}

}