package dev.aaronhowser.mods.excessive_utilities.menu.quantum_quarry

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.components.EnergyBar
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class QuantumQuarryScreen(
	menu: QuantumQuarryMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<QuantumQuarryMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val inventoryLabelOffsetY: Int = 12

	private lateinit var energyBar: EnergyBar

	override fun baseInit() {
		super.baseInit()

		energyBar = EnergyBar(
			x = leftPos + 7,
			y = topPos + 21,
			maxGetter = { 1_000_000 },
			currentGetter = { menu.getCurrentEnergy() },
			font = font
		)

		addRenderableWidget(energyBar)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/quantum_quarry.png"), 176, 198)
	}

}