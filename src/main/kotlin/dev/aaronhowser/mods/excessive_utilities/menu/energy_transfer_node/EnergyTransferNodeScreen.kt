package dev.aaronhowser.mods.excessive_utilities.menu.energy_transfer_node

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.ExcessiveUtilitiesScreen
import dev.aaronhowser.mods.excessive_utilities.menu.base.components.EnergyBar
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

//TODO: Render the ping XYZ
class EnergyTransferNodeScreen(
	menu: EnergyTransferNodeMenu,
	playerInventory: Inventory,
	title: Component
) : ExcessiveUtilitiesScreen<EnergyTransferNodeMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val titleLabelSprite: ResourceLocation = YELLOW_LABEL

	override fun baseInit() {
		super.baseInit()

		val energyBar = EnergyBar(
			x = leftPos + 79,
			y = topPos + 24,
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