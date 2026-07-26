package dev.aaronhowser.mods.excessive_utilities.menu.bag_of_holding

import dev.aaronhowser.mods.excessive_utilities.handler.bag_of_holding.BagOfHolding
import dev.aaronhowser.mods.excessive_utilities.item.BagOfHoldingItem
import dev.aaronhowser.mods.excessive_utilities.menu.UnmodifiableSlot
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ClickType
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import java.util.*

class BagOfHoldingMenu(
	containerId: Int,
	private val playerInventory: Inventory,
	private val bag: BagOfHolding,
	private val bagId: UUID,
	private val usedHand: InteractionHand
) : AbstractContainerMenu(MenuType.GENERIC_9x6, containerId) {

	private val lockedInventorySlot = when (usedHand) {
		InteractionHand.MAIN_HAND -> playerInventory.selected
		InteractionHand.OFF_HAND -> Inventory.SLOT_OFFHAND
	}

	private var lockedMenuSlot = NO_SLOT

	init {
		checkContainerSize(bag.container, BAG_SLOT_COUNT)
		bag.container.startOpen(playerInventory.player)
		addBagSlots()
		addPlayerInventorySlots()
	}

	private fun addBagSlots() {
		for (row in 0 until BAG_ROWS) {
			for (column in 0 until SLOTS_PER_ROW) {
				val slotIndex = column + row * SLOTS_PER_ROW
				addSlot(Slot(bag.container, slotIndex, 8 + column * SLOT_SPACING, 18 + row * SLOT_SPACING))
			}
		}
	}

	private fun addPlayerInventorySlots() {
		val inventoryStartY = 103 + (BAG_ROWS - 4) * SLOT_SPACING

		for (row in 0 until PLAYER_INVENTORY_ROWS) {
			for (column in 0 until SLOTS_PER_ROW) {
				val inventorySlot = column + row * SLOTS_PER_ROW + SLOTS_PER_ROW
				addPlayerSlot(inventorySlot, 8 + column * SLOT_SPACING, inventoryStartY + row * SLOT_SPACING)
			}
		}

		for (hotbarSlot in 0 until SLOTS_PER_ROW) {
			addPlayerSlot(hotbarSlot, 8 + hotbarSlot * SLOT_SPACING, inventoryStartY + 58)
		}
	}

	private fun addPlayerSlot(inventorySlot: Int, x: Int, y: Int) {
		val slot = if (inventorySlot == lockedInventorySlot) {
			UnmodifiableSlot(playerInventory, inventorySlot, x, y)
		} else {
			Slot(playerInventory, inventorySlot, x, y)
		}

		addSlot(slot)
		if (inventorySlot == lockedInventorySlot) {
			lockedMenuSlot = slots.lastIndex
		}
	}

	override fun clicked(slotId: Int, button: Int, clickType: ClickType, player: Player) {
		if (slotId == lockedMenuSlot) return
		if (clickType == ClickType.SWAP && button == lockedInventorySlot) return

		super.clicked(slotId, button, clickType, player)
	}

	override fun quickMoveStack(player: Player, slotIndex: Int): ItemStack {
		if (slotIndex == lockedMenuSlot) return ItemStack.EMPTY

		val sourceSlot = slots.getOrNull(slotIndex) ?: return ItemStack.EMPTY
		if (!sourceSlot.hasItem()) return ItemStack.EMPTY

		val sourceStack = sourceSlot.item
		val originalStack = sourceStack.copy()

		val moved = if (slotIndex < BAG_SLOT_COUNT) {
			moveItemStackTo(sourceStack, BAG_SLOT_COUNT, slots.size, true)
		} else {
			moveItemStackTo(sourceStack, 0, BAG_SLOT_COUNT, false)
		}
		if (!moved) return ItemStack.EMPTY

		if (sourceStack.isEmpty) {
			sourceSlot.setByPlayer(ItemStack.EMPTY)
		} else {
			sourceSlot.setChanged()
		}

		sourceSlot.onTake(player, sourceStack)
		return originalStack
	}

	override fun stillValid(player: Player): Boolean {
		val heldBag = player.getItemInHand(usedHand)
		return bag.isActive &&
				heldBag.item is BagOfHoldingItem &&
				heldBag.get(ModDataComponents.BAG_OF_HOLDING_ID) == bagId
	}

	override fun removed(player: Player) {
		super.removed(player)
		bag.container.stopOpen(player)
	}

	companion object {
		private const val BAG_ROWS = 6
		private const val BAG_SLOT_COUNT = BAG_ROWS * 9
		private const val SLOTS_PER_ROW = 9
		private const val PLAYER_INVENTORY_ROWS = 3
		private const val SLOT_SPACING = 18
		private const val NO_SLOT = -1
	}
}