package dev.aaronhowser.mods.excessive_utilities.menu.crusher

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.aaron.menu.components.OutputSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.CrusherBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.SimpleMachineBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class CrusherMenu(
	containerId: Int,
	playerInventory: Inventory,
	val machineContainer: Container,
	val machineContainerData: ContainerData
) : MenuWithInventory(ModMenuTypes.CRUSHER.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(CrusherBlockEntity.CONTAINER_SIZE),
				SimpleContainerData(CrusherBlockEntity.CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(machineContainer, CrusherBlockEntity.CONTAINER_SIZE)
		checkContainerDataCount(machineContainerData, CrusherBlockEntity.CONTAINER_DATA_SIZE)

		addSlots()
		addDataSlots(machineContainerData)
		addPlayerInventorySlots(98)
	}

	override fun addSlots() {
		val inputSlot = Slot(machineContainer, CrusherBlockEntity.INPUT_SLOT, 47, 41)
		val outputSlot = OutputSlot(machineContainer, CrusherBlockEntity.PRIMARY_OUTPUT_SLOT, 101, 41)
		val secondaryOutputSlot = OutputSlot(machineContainer, CrusherBlockEntity.SECONDARY_OUTPUT_SLOT, 133, 41)

		val upgradeSlot = FilteredSlot(machineContainer, CrusherBlockEntity.UPGRADE_SLOT, 153, 5) {
			it.isItem(ModItemTagsProvider.SPEED_UPGRADES)
		}

		addSlot(inputSlot)
		addSlot(outputSlot)
		addSlot(secondaryOutputSlot)
		addSlot(upgradeSlot)
	}

	fun getCurrentEnergy(): Int = machineContainerData.get(CrusherBlockEntity.CURRENT_ENERGY_DATA_INDEX)
	fun getMaxEnergy(): Int = machineContainerData.get(CrusherBlockEntity.MAX_ENERGY_DATA_INDEX)
	fun getProgress(): Int = machineContainerData.get(CrusherBlockEntity.PROGRESS_DATA_INDEX)
	fun getMaxProgress(): Int = machineContainerData.get(CrusherBlockEntity.MAX_PROGRESS_DATA_INDEX)

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return machineContainer.stillValid(player)
	}
}