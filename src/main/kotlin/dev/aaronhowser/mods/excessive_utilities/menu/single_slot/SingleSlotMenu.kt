package dev.aaronhowser.mods.excessive_utilities.menu.single_slot

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class SingleSlotMenu(
	containerId: Int,
	playerInventory: Inventory,
	val chestContainer: Container,
) : MenuWithInventory(ModMenuTypes.SINGLE_SLOT.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(1)
			)

	init {
		checkContainerSize(chestContainer, 1)

		addSlots()
		addPlayerInventorySlots(84)
	}

	override fun addSlots() {
		this.addSlot(Slot(chestContainer, 0, 80, 34))
	}

	override fun stillValid(player: Player): Boolean {
		return chestContainer.stillValid(player)
	}

}