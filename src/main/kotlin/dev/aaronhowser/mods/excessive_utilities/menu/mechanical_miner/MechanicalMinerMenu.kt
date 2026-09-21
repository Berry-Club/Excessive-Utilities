package dev.aaronhowser.mods.excessive_utilities.menu.mechanical_miner

import dev.aaronhowser.mods.aaron.menu.components.ContainerSlot

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.MechanicalMinerBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.MechanicalInteractorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.menu.base.BaseMechanicalInteractorMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class MechanicalMinerMenu(
	containerId: Int,
	playerInventory: Inventory,
	machineContainer: Container,
	machineData: ContainerData
) : BaseMechanicalInteractorMenu(
	ModMenuTypes.MECHANICAL_MINER.get(),
	containerId,
	playerInventory,
	machineContainer,
	machineData
) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(MechanicalMinerBlockEntity.CONTAINER_SIZE),
				SimpleContainerData(MechanicalInteractorBlockEntity.MENU_DATA_SIZE)
			)

	init {
		checkContainerSize(machineContainer, MechanicalMinerBlockEntity.CONTAINER_SIZE)
		checkContainerDataCount(machineData, MechanicalInteractorBlockEntity.MENU_DATA_SIZE)
		addSlots(117)
		addDataSlots(machineData)
	}

	override fun addContainerSlots() {
		super.addContainerSlots()

		val bookSlot = ContainerSlot(machineContainer, MechanicalMinerBlockEntity.ENCHANTMENT_SLOT, 135, 5)

		addSlot(bookSlot)
	}

	override fun moveMachineSpecificStack(stack: ItemStack): Boolean {
		if (!stack.isItem(Items.ENCHANTED_BOOK)) return false

		return moveItemStackTo(
			stack,
			FIRST_MACHINE_SPECIFIC_MENU_SLOT,
			FIRST_MACHINE_SPECIFIC_MENU_SLOT + 1,
			false
		)
	}

}