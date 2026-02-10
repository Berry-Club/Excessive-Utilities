package dev.aaronhowser.mods.excessive_utilities.menu.mini_chest

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class MiniChestScreen(
	menu: MiniChestMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<MiniChestMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/mini_chest.png"), 176, 166)
	}

}