package dev.aaronhowser.mods.excessive_utilities.menu.ender_porcupine_menu

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.aaron.packet.c2s.ClientClickedMenuButton
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
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

	override val background: ScreenBackground
		get() = TODO("Not yet implemented")

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

		minXIncreaseButton = ImageButton(
			0,
			0,
			16,
			16,
			UP
		) {
			val packet = ClientClickedMenuButton(EnderPorcupineMenu.INCREASE_MIN_X_BUTTON_ID)
			packet.messageServer()
		}
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