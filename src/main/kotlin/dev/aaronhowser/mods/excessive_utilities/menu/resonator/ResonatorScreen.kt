package dev.aaronhowser.mods.excessive_utilities.menu.resonator

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class ResonatorScreen(
	menu: ResonatorMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<ResonatorMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/resonator.png"), 176, 172)
	}

}