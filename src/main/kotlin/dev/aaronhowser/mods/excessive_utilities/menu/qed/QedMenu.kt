package dev.aaronhowser.mods.excessive_utilities.menu.qed

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.OutputSlot
import dev.aaronhowser.mods.excessive_utilities.block_entity.QedBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot

class QedMenu(
	containerId: Int,
	playerInventory: Inventory,
	val qedContainer: Container,
	val qedContainerData: ContainerData
) : MenuWithInventory(ModMenuTypes.QED.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(QedBlockEntity.CONTAINER_SIZE),
				SimpleContainerData(QedBlockEntity.CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(qedContainer, QedBlockEntity.CONTAINER_SIZE)
		checkContainerDataCount(qedContainerData, QedBlockEntity.CONTAINER_DATA_SIZE)

		addSlots()
		addDataSlots(qedContainerData)
		addPlayerInventorySlots(98)
	}

	fun getProgress(): Int = qedContainerData.get(QedBlockEntity.CURRENT_PROGRESS_DATA_INDEX)
	fun getMaxProgress(): Int = qedContainerData.get(QedBlockEntity.MAX_PROGRESS_DATA_INDEX)
	fun getAmountNearbyCrystals(): Int = qedContainerData.get(QedBlockEntity.AMOUNT_NEARBY_CRYSTALS_DATA_INDEX)

	override fun addSlots() {
		val outputSlot = OutputSlot(qedContainer, QedBlockEntity.OUTPUT_SLOT, 124, 41)
		addSlot(outputSlot)

		for (i in 0 until 3) for (j in 0 until 3) {
			val index = j * 3 + i
			val x = 30 + i * 18
			val y = 23 + j * 18
			val slot = Slot(qedContainer, index, x, y)
			addSlot(slot)
		}
	}

	override fun stillValid(player: Player): Boolean {
		return qedContainer.stillValid(player)
	}

}