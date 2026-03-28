package dev.aaronhowser.mods.excessive_utilities.menu.flat_transfer_node

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class FlatTransferNodeScreen(
	menu: FlatTransferNodeMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<FlatTransferNodeMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = if (menu.isItemNode()) ITEM_BACKGROUND else FLUID_BACKGROUND

	override val inventoryLabelOffsetY: Int = -2

	companion object {
		val FLUID_BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/fluid_flat_transfer_node.png"), 176, 166)
		val ITEM_BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/item_flat_transfer_node.png"), 176, 166)
	}

}