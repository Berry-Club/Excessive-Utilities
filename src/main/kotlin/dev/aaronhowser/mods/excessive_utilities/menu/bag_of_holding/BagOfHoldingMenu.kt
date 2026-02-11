package dev.aaronhowser.mods.excessive_utilities.menu.bag_of_holding

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class BagOfHoldingMenu(
	containerId: Int,
	playerInventory: Inventory,
	val bagContainer: Container
) : MenuWithInventory(ModMenuTypes.BAG_OF_HOLDING.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(9 * 6)
			)

	init {
		addSlots()
		addPlayerInventorySlots(51)
	}

	override fun addSlots() {
		for (row in 0 until 6) {
			for (col in 0 until 9) {
				val index = col + row * 9
				val slot = Slot(bagContainer, index, 8 + col * 18, 8 + row * 18)
				addSlot(slot)
			}
		}
	}

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return player.isHolding { it.isItem(ModItems.BAG_OF_HOLDING) }
	}

}