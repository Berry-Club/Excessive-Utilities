package dev.aaronhowser.mods.excessive_utilities.menu.furnace

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block_entity.EUFurnaceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.menu.components.EnergyBar
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class EUFurnaceScreen(
	menu: EUFurnaceMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<EUFurnaceMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND

	private lateinit var energyBar: EnergyBar

	override fun baseInit() {
		super.baseInit()

		inventoryLabelY += 4

		energyBar = EnergyBar(
			x = leftPos + 7,
			y = topPos + 14,
			maxGetter = { EUFurnaceBlockEntity.MAX_ENERGY },
			currentGetter = { menu.getCurrentEnergy() },
			font = font
		)

		addRenderableWidget(energyBar)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/furnace.png"), 176, 180)
	}

}