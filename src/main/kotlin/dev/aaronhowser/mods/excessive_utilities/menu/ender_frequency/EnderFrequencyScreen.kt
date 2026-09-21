package dev.aaronhowser.mods.excessive_utilities.menu.ender_frequency

import dev.aaronhowser.mods.aaron.menu.components.TexturedLabel
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isTrue
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.item.component.EnderFrequencyComponent
import dev.aaronhowser.mods.excessive_utilities.packet.client_to_server.ConfigureEnderFrequencyPacket
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.EditBox
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import java.util.function.Supplier

class EnderFrequencyScreen(
	menu: EnderFrequencyMenu,
	inventory: Inventory,
	title: Component
) : AbstractContainerScreen<EnderFrequencyMenu>(menu, inventory, title) {

	private lateinit var nameBox: EditBox
	private var isPrivate: Boolean = menu.initiallyPrivate

	init {
		imageWidth = 220
		imageHeight = 110
	}

	override fun init() {
		super.init()

		val titleLabel = TexturedLabel(
			x = leftPos,
			y = topPos,
			font = font,
			messageGetter = Supplier(::getTitleLabelMessage),
			backgroundSprite = ExcessiveUtilities.modResource("label/plain"),
			textColor = 4210752
		)

		addRenderableWidget(titleLabel)

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

	private fun getTitleLabelMessage(): Component = title

	override fun renderBg(graphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
		graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFF202020.toInt())
		graphics.fill(leftPos + 2, topPos + 2, leftPos + imageWidth - 2, topPos + imageHeight - 2, 0xFF404040.toInt())
	}

	override fun renderLabels(graphics: GuiGraphics, mouseX: Int, mouseY: Int) {}

	override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
		renderBackground(graphics, mouseX, mouseY, partialTick)
		super.render(graphics, mouseX, mouseY, partialTick)
		renderTooltip(graphics, mouseX, mouseY)
	}

	override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
		val inventoryKey = minecraft?.options?.keyInventory
		if (nameBox.isFocused && inventoryKey?.matches(keyCode, scanCode).isTrue()) {
			return true
		}

		return super.keyPressed(keyCode, scanCode, modifiers)
	}

}