package dev.aaronhowser.mods.excessive_utilities.menu.item_fluid_generator

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.components.EnergyBar
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

//TODO: Render the fluid
class ItemFluidGeneratorScreen(
	menu: ItemFluidGeneratorMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<ItemFluidGeneratorMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val inventoryLabelOffsetY: Int
		get() = 10

	private lateinit var energyBar: EnergyBar

	override fun baseInit() {
		super.baseInit()

		energyBar = EnergyBar(
			x = leftPos + 114,
			y = topPos + 18,
			maxGetter = { menu.getMaxEnergy() },
			currentGetter = { menu.getCurrentEnergy() },
			font = font
		)

		addRenderableWidget(energyBar)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/item_fluid_generator.png"), 176, 178)
	}

}