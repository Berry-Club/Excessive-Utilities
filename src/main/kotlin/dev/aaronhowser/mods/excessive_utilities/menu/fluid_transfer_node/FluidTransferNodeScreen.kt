package dev.aaronhowser.mods.excessive_utilities.menu.fluid_transfer_node

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

//TODO: Render the ping XYZ
//TODO: Render the fluid in the buffer
class FluidTransferNodeScreen(
	menu: FluidTransferNodeMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<FluidTransferNodeMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val inventoryLabelOffsetY: Int = 8

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/fluid_transfer_node.png"), 176, 190)
	}

}