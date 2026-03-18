package dev.aaronhowser.mods.excessive_utilities.menu.ender_porcupine_menu

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.aaron.packet.c2s.ClientClickedMenuButton
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.ImageButton
import net.minecraft.client.gui.components.WidgetSprites
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class EnderPorcupineScreen(
	menu: EnderPorcupineMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<EnderPorcupineMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND

	override val showInventoryLabel: Boolean = false

	private lateinit var minXIncreaseButton: Button
	private lateinit var minYIncreaseButton: Button
	private lateinit var minZIncreaseButton: Button

	private lateinit var minXDecreaseButton: Button
	private lateinit var minYDecreaseButton: Button
	private lateinit var minZDecreaseButton: Button

	private lateinit var maxXIncreaseButton: Button
	private lateinit var maxYIncreaseButton: Button
	private lateinit var maxZIncreaseButton: Button

	private lateinit var maxXDecreaseButton: Button
	private lateinit var maxYDecreaseButton: Button
	private lateinit var maxZDecreaseButton: Button

	override fun baseInit() {
		super.baseInit()

		val increaseMinButtonY = topPos + 52
		val decreaseMinButtonY = topPos + 84

		val xButtonX = leftPos + 8
		val yButtonX = leftPos + 24
		val zButtonX = leftPos + 40

		minXIncreaseButton = ImageButton(
			xButtonX,
			increaseMinButtonY,
			16,
			16,
			UP
		) {
			val packet = ClientClickedMenuButton(EnderPorcupineMenu.INCREASE_MIN_X_BUTTON_ID)
			packet.messageServer()
		}

		minXDecreaseButton = ImageButton(
			xButtonX,
			decreaseMinButtonY,
			16,
			16,
			DOWN
		) {
			val packet = ClientClickedMenuButton(EnderPorcupineMenu.DECREASE_MIN_X_BUTTON_ID)
			packet.messageServer()
		}

		minYIncreaseButton = ImageButton(
			yButtonX,
			increaseMinButtonY,
			16,
			16,
			UP
		) {
			val packet = ClientClickedMenuButton(EnderPorcupineMenu.INCREASE_MIN_Y_BUTTON_ID)
			packet.messageServer()
		}

		minYDecreaseButton = ImageButton(
			yButtonX,
			decreaseMinButtonY,
			16,
			16,
			DOWN
		) {
			val packet = ClientClickedMenuButton(EnderPorcupineMenu.DECREASE_MIN_Y_BUTTON_ID)
			packet.messageServer()
		}

		minZIncreaseButton = ImageButton(
			zButtonX,
			increaseMinButtonY,
			16,
			16,
			UP
		) {
			val packet = ClientClickedMenuButton(EnderPorcupineMenu.INCREASE_MIN_Z_BUTTON_ID)
			packet.messageServer()
		}

		minZDecreaseButton = ImageButton(
			zButtonX,
			decreaseMinButtonY,
			16,
			16,
			DOWN
		) {
			val packet = ClientClickedMenuButton(EnderPorcupineMenu.DECREASE_MIN_Z_BUTTON_ID)
			packet.messageServer()
		}

		addRenderableWidget(minXIncreaseButton)
		addRenderableWidget(minXDecreaseButton)
		addRenderableWidget(minYIncreaseButton)
		addRenderableWidget(minYDecreaseButton)
		addRenderableWidget(minZIncreaseButton)
		addRenderableWidget(minZDecreaseButton)

		val increaseMaxButtonY = topPos + 124
		val decreaseMaxButtonY = topPos + 156

		maxXIncreaseButton = ImageButton(
			xButtonX,
			increaseMaxButtonY,
			16,
			16,
			UP
		) {
			val packet = ClientClickedMenuButton(EnderPorcupineMenu.INCREASE_MAX_X_BUTTON_ID)
			packet.messageServer()
		}

		maxXDecreaseButton = ImageButton(
			xButtonX,
			decreaseMaxButtonY,
			16,
			16,
			DOWN
		) {
			val packet = ClientClickedMenuButton(EnderPorcupineMenu.DECREASE_MAX_X_BUTTON_ID)
			packet.messageServer()
		}

		maxYIncreaseButton = ImageButton(
			yButtonX,
			increaseMaxButtonY,
			16,
			16,
			UP
		) {
			val packet = ClientClickedMenuButton(EnderPorcupineMenu.INCREASE_MAX_Y_BUTTON_ID)
			packet.messageServer()
		}

		maxYDecreaseButton = ImageButton(
			yButtonX,
			decreaseMaxButtonY,
			16,
			16,
			DOWN
		) {
			val packet = ClientClickedMenuButton(EnderPorcupineMenu.DECREASE_MAX_Y_BUTTON_ID)
			packet.messageServer()
		}

		maxZIncreaseButton = ImageButton(
			zButtonX,
			increaseMaxButtonY,
			16,
			16,
			UP
		) {
			val packet = ClientClickedMenuButton(EnderPorcupineMenu.INCREASE_MAX_Z_BUTTON_ID)
			packet.messageServer()
		}

		maxZDecreaseButton = ImageButton(
			zButtonX,
			decreaseMaxButtonY,
			16,
			16,
			DOWN
		) {
			val packet = ClientClickedMenuButton(EnderPorcupineMenu.DECREASE_MAX_Z_BUTTON_ID)
			packet.messageServer()
		}

		addRenderableWidget(maxXIncreaseButton)
		addRenderableWidget(maxXDecreaseButton)
		addRenderableWidget(maxYIncreaseButton)
		addRenderableWidget(maxYDecreaseButton)
		addRenderableWidget(maxZIncreaseButton)
		addRenderableWidget(maxZDecreaseButton)

	}

	override fun renderLabels(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int) {
		super.renderLabels(guiGraphics, mouseX, mouseY)

		val currentOffsetX = menu.getCurrentOffsetX()
		val currentOffsetY = menu.getCurrentOffsetY()
		val currentOffsetZ = menu.getCurrentOffsetZ()

		guiGraphics.drawString(
			font,
			Component.literal("Current Offset: ($currentOffsetX, $currentOffsetY, $currentOffsetZ)"),
			8,
			24,
			4210752,
			false
		)

		val minX = menu.getMinX().toString()
		val minY = menu.getMinY().toString()
		val minZ = menu.getMinZ().toString()

		val xStringX = 16
		val yStringX = 32
		val zStringX = 48

		guiGraphics.drawString(
			font,
			Component.literal("Minimum Offset: ($minX, $minY, $minZ)"),
			8,
			40,
			4210752,
			false
		)

		guiGraphics.drawString(
			font,
			minX,
			xStringX - (font.width(minX) / 2),
			72,
			4210752,
			false
		)

		guiGraphics.drawString(
			font,
			minY,
			yStringX - (font.width(minY) / 2),
			72,
			4210752,
			false
		)

		guiGraphics.drawString(
			font,
			minZ,
			zStringX - (font.width(minZ) / 2),
			72,
			4210752,
			false
		)

		val maxX = menu.getMaxX().toString()
		val maxY = menu.getMaxY().toString()
		val maxZ = menu.getMaxZ().toString()

		guiGraphics.drawString(
			font,
			Component.literal("Maximum Offset: ($maxX, $maxY, $maxZ)"),
			8,
			116,
			4210752,
			false
		)

		guiGraphics.drawString(
			font,
			maxX,
			xStringX - (font.width(maxX) / 2),
			148,
			4210752,
			false
		)

		guiGraphics.drawString(
			font,
			maxY,
			yStringX - (font.width(maxY) / 2),
			148,
			4210752,
			false
		)

		guiGraphics.drawString(
			font,
			maxZ,
			zStringX - (font.width(maxZ) / 2),
			148,
			4210752,
			false
		)

	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/ender_porcupine.png"), 256, 256)

		val UP = WidgetSprites(
			ExcessiveUtilities.modResource("button_up"),
			ExcessiveUtilities.modResource("button_up")
		)

		val DOWN = WidgetSprites(
			ExcessiveUtilities.modResource("button_down"),
			ExcessiveUtilities.modResource("button_down")
		)

	}

}