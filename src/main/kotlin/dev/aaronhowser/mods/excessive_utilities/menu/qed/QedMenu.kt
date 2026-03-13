package dev.aaronhowser.mods.excessive_utilities.menu.qed

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.OutputSlot
import dev.aaronhowser.mods.excessive_utilities.block_entity.QedBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class QedMenu(
	containerId: Int,
	playerInventory: Inventory,
	val qedContainer: Container,
) : MenuWithInventory(ModMenuTypes.QED.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(QedBlockEntity.CONTAINER_SIZE),
			)

	init {
		checkContainerSize(qedContainer, QedBlockEntity.CONTAINER_SIZE)

		addSlots()
		addPlayerInventorySlots(84)
	}

	override fun addSlots() {
		val outputSlot = OutputSlot(qedContainer, QedBlockEntity.OUTPUT_SLOT, 124, 35)
		addSlot(outputSlot)

		for (i in 0 until 3) for (j in 0 until 3) {
			val index = j * 3 + i
			val x = 30 + i * 18
			val y = 17 + j * 18
			val slot = Slot(qedContainer, index, x, y)
			addSlot(slot)
		}
	}

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return qedContainer.stillValid(player)
	}

}