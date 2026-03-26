package dev.aaronhowser.mods.excessive_utilities.menu.item_transfer_node

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

//TODO: Render the ping XYZ
class ItemTransferNodeScreen(
	menu: ItemTransferNodeMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<ItemTransferNodeMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val inventoryLabelOffsetY: Int = 8

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/item_transfer_node.png"), 176, 173)
	}

}