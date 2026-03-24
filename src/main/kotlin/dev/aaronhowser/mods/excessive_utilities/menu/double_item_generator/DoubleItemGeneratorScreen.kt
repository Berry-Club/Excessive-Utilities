package dev.aaronhowser.mods.excessive_utilities.menu.double_item_generator

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.components.EnergyBar
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class DoubleItemGeneratorScreen(
	menu: DoubleItemGeneratorMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<DoubleItemGeneratorMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND

	private lateinit var energyBar: EnergyBar

	override fun baseInit() {
		super.baseInit()

		energyBar = EnergyBar(
			x = leftPos + 151,
			y = topPos + 15,
			maxGetter = { menu.getMaxEnergy() },
			currentGetter = { menu.getCurrentEnergy() },
			font = font
		)

		addRenderableWidget(energyBar)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/double_item_generator.png"), 176, 178)
	}

}