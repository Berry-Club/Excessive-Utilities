package dev.aaronhowser.mods.excessive_utilities.menu.mechanical_miner

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.mechanical_interactor.BaseMechanicalInteractorScreen
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class MechanicalMinerScreen(
	menu: MechanicalMinerMenu,
	playerInventory: Inventory,
	title: Component
) : BaseMechanicalInteractorScreen<MechanicalMinerMenu>(menu, playerInventory, title) {
	override val background: ResourceLocation = BACKGROUND

	override fun createRedstoneModeButton(): Button {
		return createRedstoneModeButton(78, 119)
	}

	companion object {
		val BACKGROUND: ResourceLocation =
			ExcessiveUtilities.modResource("textures/gui/mechanical_miner.png")
	}

}