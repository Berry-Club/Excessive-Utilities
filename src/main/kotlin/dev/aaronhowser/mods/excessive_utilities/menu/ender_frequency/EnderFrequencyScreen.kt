package dev.aaronhowser.mods.excessive_utilities.menu.ender_frequency

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isTrue
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.item.component.EnderFrequencyComponent
import dev.aaronhowser.mods.excessive_utilities.menu.ExcessiveUtilitiesScreen
import dev.aaronhowser.mods.excessive_utilities.packet.client_to_server.ConfigureEnderFrequencyPacket
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class EnderFrequencyScreen(
	menu: EnderFrequencyMenu,
	inventory: Inventory,
	title: Component
) : ExcessiveUtilitiesScreen<EnderFrequencyMenu>(menu, inventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val showInventoryLabel: Boolean = false

	private lateinit var nameBox: EditBox
	private var isPrivate: Boolean = menu.initiallyPrivate

	override fun baseInit() {
		super.baseInit()

		nameBox = EditBox(
			font,
			leftPos + 10,
			topPos + 28,
			imageWidth - 20,
			20,
			ModMenuLang.ENDER_FREQUENCY_NAME.toComponent()
		)

		nameBox.setMaxLength(EnderFrequencyComponent.MAX_NAME_LENGTH)
		nameBox.value = menu.initialName
		nameBox.setResponder { saveFrequency() }
		addRenderableWidget(nameBox)

		val privacyButton = Button.builder(privacyMessage()) {
			isPrivate = !isPrivate
			it.message = privacyMessage()
			saveFrequency()
		}.bounds(leftPos + 10, topPos + 55, imageWidth - 20, 20).build()

		addRenderableWidget(privacyButton)

		setInitialFocus(nameBox)
	}

	private fun saveFrequency() {
		ConfigureEnderFrequencyPacket(menu.hand, nameBox.value, isPrivate).messageServer()
	}

	private fun privacyMessage(): Component {
		return if (isPrivate) {
			ModMenuLang.ENDER_FREQUENCY_PRIVATE.toComponent()
		} else {
			ModMenuLang.ENDER_FREQUENCY_PUBLIC.toComponent()
		}
	}

	override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
		val inventoryKey = minecraft?.options?.keyInventory
		if (nameBox.isFocused && inventoryKey?.matches(keyCode, scanCode).isTrue()) {
			return true
		}

		return super.keyPressed(keyCode, scanCode, modifiers)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/ender_frequency.png"), 220, 110)
	}

}