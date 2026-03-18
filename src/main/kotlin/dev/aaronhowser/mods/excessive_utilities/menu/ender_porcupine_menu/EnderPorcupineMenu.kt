package dev.aaronhowser.mods.excessive_utilities.menu.ender_porcupine_menu

import dev.aaronhowser.mods.aaron.menu.MenuWithButtons
import dev.aaronhowser.mods.excessive_utilities.block_entity.EnderPorcupineBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack

class EnderPorcupineMenu(
	containerId: Int,
	val containerData: ContainerData
) : AbstractContainerMenu(ModMenuTypes.RESONATOR.get(), containerId), MenuWithButtons {

	init {
		addDataSlots(containerData)
	}

	override fun handleButtonPressed(buttonId: Int) {
		val dataIndex = when (buttonId) {
			INCREASE_MIN_X_BUTTON_ID, DECREASE_MIN_X_BUTTON_ID -> EnderPorcupineBlockEntity.MIN_X_DATA_INDEX
			INCREASE_MIN_Y_BUTTON_ID, DECREASE_MIN_Y_BUTTON_ID -> EnderPorcupineBlockEntity.MIN_Y_DATA_INDEX
			INCREASE_MIN_Z_BUTTON_ID, DECREASE_MIN_Z_BUTTON_ID -> EnderPorcupineBlockEntity.MIN_Z_DATA_INDEX

			INCREASE_MAX_X_BUTTON_ID, DECREASE_MAX_X_BUTTON_ID -> EnderPorcupineBlockEntity.MAX_X_DATA_INDEX
			INCREASE_MAX_Y_BUTTON_ID, DECREASE_MAX_Y_BUTTON_ID -> EnderPorcupineBlockEntity.MAX_Y_DATA_INDEX
			INCREASE_MAX_Z_BUTTON_ID, DECREASE_MAX_Z_BUTTON_ID -> EnderPorcupineBlockEntity.MAX_Z_DATA_INDEX
			else -> return
		}

		val current = containerData.get(dataIndex)
		val next = when (buttonId) {
			INCREASE_MIN_X_BUTTON_ID, INCREASE_MIN_Y_BUTTON_ID, INCREASE_MIN_Z_BUTTON_ID,
			INCREASE_MAX_X_BUTTON_ID, INCREASE_MAX_Y_BUTTON_ID, INCREASE_MAX_Z_BUTTON_ID -> current + 1

			DECREASE_MIN_X_BUTTON_ID, DECREASE_MIN_Y_BUTTON_ID, DECREASE_MIN_Z_BUTTON_ID,
			DECREASE_MAX_X_BUTTON_ID, DECREASE_MAX_Y_BUTTON_ID, DECREASE_MAX_Z_BUTTON_ID -> current - 1

			else -> return
		}

		containerData.set(dataIndex, next)
	}

	override fun quickMoveStack(player: Player, index: Int): ItemStack = ItemStack.EMPTY
	override fun stillValid(player: Player): Boolean = true

	companion object {
		const val INCREASE_MIN_X_BUTTON_ID = 0
		const val INCREASE_MIN_Y_BUTTON_ID = 1
		const val INCREASE_MIN_Z_BUTTON_ID = 2

		const val DECREASE_MIN_X_BUTTON_ID = 3
		const val DECREASE_MIN_Y_BUTTON_ID = 4
		const val DECREASE_MIN_Z_BUTTON_ID = 5

		const val INCREASE_MAX_X_BUTTON_ID = 6
		const val INCREASE_MAX_Y_BUTTON_ID = 7
		const val INCREASE_MAX_Z_BUTTON_ID = 8

		const val DECREASE_MAX_X_BUTTON_ID = 9
		const val DECREASE_MAX_Y_BUTTON_ID = 10
		const val DECREASE_MAX_Z_BUTTON_ID = 11
	}

}