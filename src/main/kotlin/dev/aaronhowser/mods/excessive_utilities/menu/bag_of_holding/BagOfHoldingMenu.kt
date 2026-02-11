package dev.aaronhowser.mods.excessive_utilities.menu.bag_of_holding

import dev.aaronhowser.mods.excessive_utilities.menu.HeldItemContainerMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class BagOfHoldingMenu(
	containerId: Int,
	playerInventory: Inventory
) : HeldItemContainerMenu(
	ModItems.BAG_OF_HOLDING,
	ModMenuTypes.BAG_OF_HOLDING.get(),
	containerId,
	playerInventory
) {

	init {
		addSlots()
		addPlayerInventorySlots(51)
	}

	override val containerSlots: Int = 9 * 6

	override fun addSlots() {
		for (row in 0 until 6) {
			for (col in 0 until 9) {
				val index = col + row * 9
				val slot = Slot(fakeContainer, index, 8 + col * 18, 8 + row * 18)
				addSlot(slot)
			}
		}
	}

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

}