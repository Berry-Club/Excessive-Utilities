package dev.aaronhowser.mods.excessive_utilities.menu.mechanical_miner

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.MechanicalInteractorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.MechanicalMinerBlockEntity
import dev.aaronhowser.mods.excessive_utilities.menu.mechanical_interactor.BaseMechanicalInteractorMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot
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
		initializeMenu(MechanicalInteractorBlockEntity.MENU_DATA_SIZE)
	}

	override fun getExpectedContainerSize(): Int = MechanicalMinerBlockEntity.CONTAINER_SIZE
	override fun getUpgradeSlotX(): Int = 152
	override fun getUpgradeSlotY(): Int = 120

	override fun addContainerSlots() {
		super.addContainerSlots()

		addSlot(
			object : Slot(machineContainer, MechanicalMinerBlockEntity.ENCHANTMENT_SLOT, 8, 120) {
				override fun mayPlace(stack: ItemStack): Boolean = stack.`is`(Items.ENCHANTED_BOOK)
			}
		)
	}

	override fun moveMachineSpecificStack(stack: ItemStack): Boolean {
		if (!stack.`is`(Items.ENCHANTED_BOOK)) return false

		return moveItemStackTo(
			stack,
			FIRST_MACHINE_SPECIFIC_MENU_SLOT,
			FIRST_MACHINE_SPECIFIC_MENU_SLOT + 1,
			false
		)
	}

}