package dev.aaronhowser.mods.excessive_utilities.menu.mechanical_user

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.menu.mechanical_interactor.BaseMechanicalInteractorScreen
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class MechanicalUserScreen(
	menu: MechanicalUserMenu,
	playerInventory: Inventory,
	title: Component
) : BaseMechanicalInteractorScreen<MechanicalUserMenu>(menu, playerInventory, title) {
	override val background: ResourceLocation = BACKGROUND

	private lateinit var interactionModeButton: Button
	private lateinit var leftClickButton: Button
	private lateinit var upperLeftSlotButton: Button
	private lateinit var sneakingButton: Button

	override fun addMachineSpecificButtons() {
		interactionModeButton = addRenderableWidget(createButton(80, 104, 68, 16, MechanicalUserMenu.CYCLE_INTERACTION_MODE_BUTTON))
		leftClickButton = addRenderableWidget(createButton(8, 122, 68, 16, MechanicalUserMenu.TOGGLE_LEFT_CLICK_BUTTON))
		upperLeftSlotButton = addRenderableWidget(createButton(80, 122, 68, 16, MechanicalUserMenu.TOGGLE_UPPER_LEFT_SLOT_ONLY_BUTTON))
		sneakingButton = addRenderableWidget(createButton(80, 140, 68, 16, MechanicalUserMenu.TOGGLE_SNEAKING_BUTTON))
	}

	override fun createRedstoneModeButton(): Button {
		return createRedstoneModeButton(32, 102)
	}

	override fun updateMachineSpecificButtonMessages() {
		interactionModeButton.message = menu.interactionMode.langKey.toComponent()
		leftClickButton.message = if (menu.isLeftClick) {
			ModMenuLang.MECHANICAL_USER_CLICK_LEFT.toComponent()
		} else {
			ModMenuLang.MECHANICAL_USER_CLICK_RIGHT.toComponent()
		}
		upperLeftSlotButton.message = if (menu.useUpperLeftSlotOnly) {
			ModMenuLang.MECHANICAL_USER_SLOT_UPPER_LEFT.toComponent()
		} else {
			ModMenuLang.MECHANICAL_USER_SLOT_RANDOM.toComponent()
		}
		sneakingButton.message = if (menu.isSneaking) {
			ModMenuLang.MECHANICAL_USER_SNEAKING_ON.toComponent()
		} else {
			ModMenuLang.MECHANICAL_USER_SNEAKING_OFF.toComponent()
		}
	}

	override fun renderMachineSpecificBackground(guiGraphics: GuiGraphics) {
		renderSlotBackground(guiGraphics, 152, 138)
	}

	companion object {
		val BACKGROUND: ResourceLocation =
			ExcessiveUtilities.modResource("textures/gui/mechanical_user.png")
	}

}