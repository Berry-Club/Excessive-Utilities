package dev.aaronhowser.mods.excessive_utilities.menu.fluid_transfer_node

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.ExcessiveUtilitiesScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

//TODO: Render the ping XYZ
//TODO: Render the fluid in the buffer
class FluidTransferNodeScreen(
	menu: FluidTransferNodeMenu,
	playerInventory: Inventory,
	title: Component
) : ExcessiveUtilitiesScreen<FluidTransferNodeMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val titleLabelSprite: ResourceLocation = BLUE_LABEL

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/fluid_transfer_node.png"), 176, 190)
	}

}