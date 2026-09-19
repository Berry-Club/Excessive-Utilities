package dev.aaronhowser.mods.excessive_utilities.menu.item_fluid_generator

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.ExcessiveUtilitiesScreen
import dev.aaronhowser.mods.excessive_utilities.menu.components.EnergyBar
import dev.aaronhowser.mods.excessive_utilities.menu.components.FluidBar
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory
import net.neoforged.neoforge.fluids.FluidStack

class ItemFluidGeneratorScreen(
	menu: ItemFluidGeneratorMenu,
	playerInventory: Inventory,
	title: Component
) : ExcessiveUtilitiesScreen<ItemFluidGeneratorMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND

	private lateinit var energyBar: EnergyBar
	private lateinit var fluidBar: FluidBar

	override fun baseInit() {
		super.baseInit()

		energyBar = EnergyBar(
			x = leftPos + 114,
			y = topPos + 18,
			maxGetter = { menu.getMaxEnergy() },
			currentGetter = { menu.getCurrentEnergy() },
			font = font
		)

		fluidBar = FluidBar(
			x = leftPos + 69,
			y = topPos + 40,
			capacityGetter = { menu.blockEntity?.tank?.capacity ?: 0 },
			fluidGetter = { menu.blockEntity?.tank?.fluid ?: FluidStack.EMPTY },
			font = font
		)

		addRenderableWidget(energyBar)
		addRenderableWidget(fluidBar)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/item_fluid_generator.png"), 176, 178)
	}

}