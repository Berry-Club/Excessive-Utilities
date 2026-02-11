package dev.aaronhowser.mods.excessive_utilities.menu.bag_of_holding

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.mini_chest.MiniChestMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class BagOfHoldingScreen(
	menu: BagOfHoldingMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<BagOfHoldingMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/mini_chest.png"), 176, 166)
	}

}