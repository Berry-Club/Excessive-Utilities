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

	override val background: ScreenBackground = BACKGROUND

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/flat_transfer_node.png"), 176, 166)
	}

}