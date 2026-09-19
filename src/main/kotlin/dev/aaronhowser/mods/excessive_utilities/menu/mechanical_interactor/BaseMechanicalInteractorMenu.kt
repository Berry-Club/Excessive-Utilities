package dev.aaronhowser.mods.excessive_utilities.menu.mechanical_interactor

import dev.aaronhowser.mods.aaron.menu.MenuWithButtons
import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextEnum
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.prevEnum
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.MechanicalInteractorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.MechanicalInteractorBlockEntity.RedstoneMode
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

abstract class BaseMechanicalInteractorMenu(
	menuType: MenuType<*>,
	containerId: Int,
	playerInventory: Inventory,
	protected val machineContainer: Container,
	val machineData: ContainerData
) : MenuWithInventory(menuType, containerId, playerInventory), MenuWithButtons {

	var redstoneMode: RedstoneMode
		get() {
			val ordinal = machineData.get(MechanicalInteractorBlockEntity.REDSTONE_MODE_DATA_INDEX)
			return RedstoneMode.fromOrdinal(ordinal)
		}
		set(value) {
			machineData.set(MechanicalInteractorBlockEntity.REDSTONE_MODE_DATA_INDEX, value.ordinal)
		}

	protected fun initializeMenu(expectedDataSize: Int) {
		checkContainerSize(machineContainer, getExpectedContainerSize())
		checkContainerDataCount(machineData, expectedDataSize)
		addSlots(158)
		addDataSlots(machineData)
	}

	protected abstract fun getExpectedContainerSize(): Int
	protected abstract fun getUpgradeSlotX(): Int
	protected abstract fun getUpgradeSlotY(): Int

	override fun addContainerSlots() {
		for (row in 0 until 3) {
			for (column in 0 until 3) {
				val slot = column + row * 3
				addSlot(Slot(machineContainer, slot, 62 + column * 18, 43 + row * 18))
			}
		}

		addSlot(
			FilteredSlot(machineContainer, MechanicalInteractorBlockEntity.UPGRADE_SLOT, getUpgradeSlotX(), getUpgradeSlotY()) {
				it.isItem(ModItemTagsProvider.SPEED_UPGRADES)
			}
		)
	}

	override fun handleButtonPressed(buttonId: Int, isShiftDown: Boolean) {
		when (buttonId) {
			CYCLE_REDSTONE_MODE_BUTTON -> redstoneMode = if (isShiftDown) {
				redstoneMode.prevEnum()
			} else {
				redstoneMode.nextEnum()
			}
		}
	}

	override fun quickMoveStack(player: Player, clickedSlotIndex: Int): ItemStack {
		val clickedSlot = slots.getOrNull(clickedSlotIndex)
		if (clickedSlot == null || !clickedSlot.hasItem()) return ItemStack.EMPTY

		val clickedStack = clickedSlot.item
		val originalStack = clickedStack.copy()
		val moved = if (clickedSlotIndex < PLAYER_INVENTORY_SLOT_COUNT) {
			movePlayerStack(clickedStack, clickedSlotIndex)
		} else {
			moveItemStackTo(clickedStack, 0, PLAYER_INVENTORY_SLOT_COUNT, true)
		}

		if (!moved) return ItemStack.EMPTY

		if (clickedStack.isEmpty) {
			clickedSlot.setByPlayer(ItemStack.EMPTY)
		} else {
			clickedSlot.setChanged()
		}

		if (clickedStack.count == originalStack.count) return ItemStack.EMPTY
		clickedSlot.onTake(player, clickedStack)
		return originalStack
	}

	private fun movePlayerStack(stack: ItemStack, clickedSlotIndex: Int): Boolean {
		if (stack.isItem(ModItemTagsProvider.SPEED_UPGRADES)) {
			if (moveItemStackTo(stack, UPGRADE_MENU_SLOT, UPGRADE_MENU_SLOT + 1, false)) return true
		}

		if (moveMachineSpecificStack(stack)) return true
		if (moveItemStackTo(stack, FIRST_INVENTORY_MENU_SLOT, UPGRADE_MENU_SLOT, false)) return true

		return if (clickedSlotIndex < HOTBAR_START_INDEX) {
			moveItemStackTo(stack, HOTBAR_START_INDEX, PLAYER_INVENTORY_SLOT_COUNT, false)
		} else {
			moveItemStackTo(stack, 0, HOTBAR_START_INDEX, false)
		}
	}

	protected open fun moveMachineSpecificStack(stack: ItemStack): Boolean = false

	override fun stillValid(player: Player): Boolean = machineContainer.stillValid(player)

	companion object {
		const val CYCLE_REDSTONE_MODE_BUTTON = 0

		protected const val FIRST_INVENTORY_MENU_SLOT = PLAYER_INVENTORY_SLOT_COUNT
		protected const val UPGRADE_MENU_SLOT = FIRST_INVENTORY_MENU_SLOT + MechanicalInteractorBlockEntity.INVENTORY_SIZE
		protected const val FIRST_MACHINE_SPECIFIC_MENU_SLOT = UPGRADE_MENU_SLOT + 1
	}

}