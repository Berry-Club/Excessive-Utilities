package dev.aaronhowser.mods.excessive_utilities.menu.mechanical_interactor

import dev.aaronhowser.mods.aaron.menu.components.MultiStageSpriteButton
import dev.aaronhowser.mods.aaron.menu.textures.ScreenSprite
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.aaron.packet.c2s.ClientClickedMenuButton
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.MechanicalInteractorBlockEntity.RedstoneMode
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.screens.Screen
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

abstract class BaseMechanicalInteractorScreen<T : BaseMechanicalInteractorMenu>(
	menu: T,
	playerInventory: Inventory,
	title: Component
) : AbstractContainerScreen<T>(menu, playerInventory, title) {

	protected abstract val background: ResourceLocation

	init {
		imageWidth = 176
		imageHeight = 240
	}

	override fun init() {
		super.init()

		addMachineSpecificButtons()
		addRenderableWidget(createRedstoneModeButton())
		updateButtonMessages()
	}

	protected open fun addMachineSpecificButtons() {}

	protected abstract fun createRedstoneModeButton(): Button

	protected fun createRedstoneModeButton(x: Int, y: Int): Button {
		return MultiStageSpriteButton.Builder(font)
			.addStage(RedstoneMode.ALWAYS_ON.langKey.toComponent(), ALWAYS_ON_SPRITE)
			.addStage(RedstoneMode.WHILE_POWERED.langKey.toComponent(), REDSTONE_ON_SPRITE)
			.addStage(RedstoneMode.WHILE_UNPOWERED.langKey.toComponent(), REDSTONE_OFF_SPRITE)
			.addStage(RedstoneMode.ON_PULSE.langKey.toComponent(), REDSTONE_PULSE_SPRITE)
			.location(leftPos + x, topPos + y)
			.size(20)
			.currentStageGetter { menu.redstoneMode.ordinal }
			.onPress {
				ClientClickedMenuButton(
					BaseMechanicalInteractorMenu.CYCLE_REDSTONE_MODE_BUTTON,
					Screen.hasShiftDown()
				).messageServer()
			}
			.build()
	}

	protected fun createButton(
		x: Int,
		y: Int,
		width: Int,
		height: Int,
		buttonId: Int
	): Button {
		return Button.builder(Component.empty()) {
			ClientClickedMenuButton(buttonId, Screen.hasShiftDown()).messageServer()
		}
			.bounds(leftPos + x, topPos + y, width, height)
			.build()
	}

	private fun updateButtonMessages() {
		updateMachineSpecificButtonMessages()
	}

	protected open fun updateMachineSpecificButtonMessages() {}

	override fun containerTick() {
		super.containerTick()
		updateButtonMessages()
	}

	override fun render(guiGraphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTick: Float) {
		renderBackground(guiGraphics, mouseX, mouseY, partialTick)
		super.render(guiGraphics, mouseX, mouseY, partialTick)
		renderTooltip(guiGraphics, mouseX, mouseY)
	}

	override fun renderBg(guiGraphics: GuiGraphics, partialTick: Float, mouseX: Int, mouseY: Int) {
		guiGraphics.blit(background, leftPos, topPos, 0f, 0f, imageWidth, imageHeight, 256, 256)
	}

	companion object {
		private val ALWAYS_ON_SPRITE = sprite("always_on")
		private val REDSTONE_ON_SPRITE = sprite("redstone_on")
		private val REDSTONE_OFF_SPRITE = sprite("redstone_off")
		private val REDSTONE_PULSE_SPRITE = sprite("redstone_pulse")

		private fun sprite(name: String): ScreenSprite {
			return ScreenSprite(
				ExcessiveUtilities.modResource("redstone_mode/$name"),
				16,
				16
			)
		}
	}

}