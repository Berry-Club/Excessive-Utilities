package dev.aaronhowser.mods.excessive_utilities.menu.simple_machine

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.ContainerSlot
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.SimpleMachineBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData

class SimpleMachineMenu(
	containerId: Int,
	playerInventory: Inventory,
	val machineContainer: Container,
	val machineContainerData: ContainerData
) : MenuWithInventory(ModMenuTypes.SIMPLE_MACHINE.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(SimpleMachineBlockEntity.CONTAINER_SIZE),
				SimpleContainerData(SimpleMachineBlockEntity.CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(machineContainer, SimpleMachineBlockEntity.CONTAINER_SIZE)
		checkContainerDataCount(machineContainerData, SimpleMachineBlockEntity.CONTAINER_DATA_SIZE)

		addSlots(98)
		addDataSlots(machineContainerData)
	}

	override fun addContainerSlots() {
		val inputSlot = ContainerSlot(machineContainer, SimpleMachineBlockEntity.INPUT_SLOT, 62, 41)
		val outputSlot = ContainerSlot(machineContainer, SimpleMachineBlockEntity.OUTPUT_SLOT, 116, 41)
		val upgradeSlot = ContainerSlot(machineContainer, SimpleMachineBlockEntity.UPGRADE_SLOT, 153, 5)

		addSlot(outputSlot)
		addSlot(inputSlot)
		addSlot(upgradeSlot)
	}

	fun getCurrentEnergy(): Int = machineContainerData.get(SimpleMachineBlockEntity.CURRENT_ENERGY_DATA_INDEX)
	fun getMaxEnergy(): Int = machineContainerData.get(SimpleMachineBlockEntity.MAX_ENERGY_DATA_INDEX)
	fun getProgress(): Int = machineContainerData.get(SimpleMachineBlockEntity.PROGRESS_DATA_INDEX)
	fun getMaxProgress(): Int = machineContainerData.get(SimpleMachineBlockEntity.MAX_PROGRESS_DATA_INDEX)

	override fun stillValid(player: Player): Boolean {
		return machineContainer.stillValid(player)
	}
}