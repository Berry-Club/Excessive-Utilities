package dev.aaronhowser.mods.excessive_utilities.menu.bag_of_holding

import dev.aaronhowser.mods.excessive_utilities.handler.bag_of_holding.BagOfHolding
import dev.aaronhowser.mods.excessive_utilities.item.BagOfHoldingItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.aaron.menu.HeldItemMenu
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack
import java.util.*

class BagOfHoldingMenu(
	containerId: Int,
	playerInventory: Inventory,
	private val bag: BagOfHolding,
	private val bagId: UUID,
	usedHand: InteractionHand
) : HeldItemMenu(MenuType.GENERIC_9x6, containerId, playerInventory, usedHand) {

	init {
		checkContainerSize(bag.container, BAG_SLOT_COUNT)
		bag.container.startOpen(playerInventory.player)
		addSlots(103 + (BAG_ROWS - 4) * SLOT_SPACING)
	}

	override fun addContainerSlots() {
		for (row in 0 until BAG_ROWS) {
			for (column in 0 until SLOTS_PER_ROW) {
				val slotIndex = column + row * SLOTS_PER_ROW
				addSlot(Slot(bag.container, slotIndex, 8 + column * SLOT_SPACING, 18 + row * SLOT_SPACING))
			}
		}
	}

	override fun isValidHeldItem(heldItem: ItemStack): Boolean {
		return bag.isActive &&
				heldItem.item is BagOfHoldingItem &&
				heldItem.get(ModDataComponents.BAG_OF_HOLDING_ID) == bagId
	}

	override fun removed(player: Player) {
		super.removed(player)
		bag.container.stopOpen(player)
	}

	companion object {
		private const val BAG_ROWS = 6
		private const val BAG_SLOT_COUNT = BAG_ROWS * 9
		private const val SLOTS_PER_ROW = 9
		private const val SLOT_SPACING = 18
	}
}