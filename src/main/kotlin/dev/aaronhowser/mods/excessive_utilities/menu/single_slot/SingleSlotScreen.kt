package dev.aaronhowser.mods.excessive_utilities.menu.single_slot

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class SingleSlotScreen(
	menu: SingleSlotMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<SingleSlotMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val inventoryLabelOffsetY: Int = -1

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/single_slot.png"), 176, 166)
	}

}