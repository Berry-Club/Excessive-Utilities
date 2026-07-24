package dev.aaronhowser.mods.excessive_utilities.menu.energy_transfer_node

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node.EnergyTransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.menu.TransferNodeUpgradeSlot
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData

class EnergyTransferNodeMenu(
	containerId: Int,
	playerInventory: Inventory,
	val upgradesContainer: Container,
	val containerData: ContainerData
) : MenuWithInventory(ModMenuTypes.ENERGY_TRANSFER_NODE.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(TransferNodeBlockEntity.UPGRADE_CONTAINER_SIZE),
				SimpleContainerData(EnergyTransferNodeBlockEntity.CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(upgradesContainer, TransferNodeBlockEntity.UPGRADE_CONTAINER_SIZE)

		addSlots(108)
		addDataSlots(containerData)
	}

	override fun addContainerSlots() {
		for (i in 0 until TransferNodeBlockEntity.UPGRADE_CONTAINER_SIZE) {
			val x = 35 + i * 18
			val y = 68

			val slot = TransferNodeUpgradeSlot(upgradesContainer, i, x, y)

			this.addSlot(slot)
		}
	}

	fun getPingX(): Int = containerData.get(TransferNodeBlockEntity.X_DATA_INDEX)
	fun getPingY(): Int = containerData.get(TransferNodeBlockEntity.Y_DATA_INDEX)
	fun getPingZ(): Int = containerData.get(TransferNodeBlockEntity.Z_DATA_INDEX)
	fun getStoredEnergy(): Int = containerData.get(EnergyTransferNodeBlockEntity.STORED_ENERGY_DATA_INDEX)
	fun getMaxEnergy(): Int = containerData.get(EnergyTransferNodeBlockEntity.MAX_ENERGY_DATA_INDEX)

	override fun stillValid(player: Player): Boolean {
		return upgradesContainer.stillValid(player)
	}

}