package dev.aaronhowser.mods.excessive_utilities.menu.energy_transfer_node

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.components.EnergyBar
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

//TODO: Render the ping XYZ
class EnergyTransferNodeScreen(
	menu: EnergyTransferNodeMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<EnergyTransferNodeMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val inventoryLabelOffsetY: Int = 22

	private lateinit var energyBar: EnergyBar

	override fun baseInit() {
		energyBar = EnergyBar(
			x = leftPos + 7,
			y = topPos + 21,
			maxGetter = { menu.getMaxEnergy() },
			currentGetter = { menu.getStoredEnergy() },
			font = font,
			isSmall = true
		)

		addRenderableWidget(energyBar)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/energy_transfer_node.png"), 176, 190)
	}

}