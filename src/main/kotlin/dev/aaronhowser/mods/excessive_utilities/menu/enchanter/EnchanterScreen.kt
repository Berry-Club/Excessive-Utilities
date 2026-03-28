package dev.aaronhowser.mods.excessive_utilities.menu.enchanter

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block_entity.EUFurnaceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.menu.components.EnergyBar
import dev.aaronhowser.mods.excessive_utilities.menu.components.ProgressArrow
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class EnchanterScreen(
	menu: EnchanterMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<EnchanterMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val inventoryLabelOffsetY: Int = 4

	private lateinit var energyBar: EnergyBar
	private lateinit var progressArrow: ProgressArrow

	override fun baseInit() {
		super.baseInit()

		energyBar = EnergyBar(
			x = leftPos + 7,
			y = topPos + 14,
			maxGetter = { EUFurnaceBlockEntity.MAX_ENERGY },
			currentGetter = { menu.getCurrentEnergy() },
			font = font
		)

		progressArrow = ProgressArrow(
			x = leftPos + 83,
			y = topPos + 43,
			font = font,
			texture = PROGRESS_ARROW_TEXTURE,
			percentDoneFunction = { menu.getProgress().toFloat() / menu.getMaxProgress() },
			shouldRenderProgress = { menu.getProgress() > 0 }
		)

		addRenderableWidget(energyBar)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/enchanter.png"), 176, 180)
		val PROGRESS_ARROW_TEXTURE = ExcessiveUtilities.modResource("enchanter_arrow_right")
	}

}