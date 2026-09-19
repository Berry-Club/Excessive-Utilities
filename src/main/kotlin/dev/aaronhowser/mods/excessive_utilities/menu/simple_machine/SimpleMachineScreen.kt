package dev.aaronhowser.mods.excessive_utilities.menu.simple_machine

import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.menu.ExcessiveUtilitiesScreen
import dev.aaronhowser.mods.excessive_utilities.menu.components.EnergyBar
import dev.aaronhowser.mods.excessive_utilities.menu.components.ProgressArrow
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class SimpleMachineScreen(
	menu: SimpleMachineMenu,
	playerInventory: Inventory,
	title: Component
) : ExcessiveUtilitiesScreen<SimpleMachineMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val titleLabelSprite: ResourceLocation = YELLOW_LABEL

	private lateinit var energyBar: EnergyBar
	private lateinit var progressArrow: ProgressArrow

	override fun baseInit() {
		super.baseInit()

		energyBar = EnergyBar(
			x = leftPos + 7,
			y = topPos + 21,
			maxGetter = { menu.getMaxEnergy() },
			currentGetter = { menu.getCurrentEnergy() },
			font = font
		)

		progressArrow = ProgressArrow(
			x = leftPos + 84,
			y = topPos + 41,
			font = font,
			percentDoneFunction = { menu.getProgress().toFloat() / menu.getMaxProgress() },
			shouldRenderProgress = { menu.getProgress() > 0 }
		)

		addRenderableWidget(energyBar)
		addRenderableWidget(progressArrow)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/simple_machine.png"), 176, 180)
	}

}