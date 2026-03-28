package dev.aaronhowser.mods.excessive_utilities.menu.enchanter

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.aaron.menu.components.OutputSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.EnchanterBlockEntity
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

//TODO: Show progress arrow and energy bar
class EnchanterMenu(
	containerId: Int,
	playerInventory: Inventory,
	val enchanterContainer: Container,
	val enchanterContainerData: ContainerData
) : MenuWithInventory(ModMenuTypes.ENCHANTER.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(EnchanterBlockEntity.CONTAINER_SIZE),
				SimpleContainerData(EnchanterBlockEntity.CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(enchanterContainer, EnchanterBlockEntity.CONTAINER_SIZE)
		checkContainerDataCount(enchanterContainerData, EnchanterBlockEntity.CONTAINER_DATA_SIZE)

		addSlots()
		addDataSlots(enchanterContainerData)
		addPlayerInventorySlots(98)
	}

	fun getMaxEnergy(): Int = enchanterContainerData.get(EnchanterBlockEntity.MAX_ENERGY_DATA_INDEX)
	fun getCurrentEnergy(): Int = enchanterContainerData.get(EnchanterBlockEntity.CURRENT_ENERGY_DATA_INDEX)
	fun getProgress(): Int = enchanterContainerData.get(EnchanterBlockEntity.PROGRESS_DATA_INDEX)
	fun getMaxProgress(): Int = enchanterContainerData.get(EnchanterBlockEntity.MAX_PROGRESS_DATA_INDEX)

	override fun addSlots() {
		val leftInputSlot = Slot(enchanterContainer, EnchanterBlockEntity.LEFT_INPUT_SLOT, 49, 41)
		val rightOutputSlot = Slot(enchanterContainer, EnchanterBlockEntity.RIGHT_INPUT_SLOT, 73, 41)

		val outputSlot = OutputSlot(enchanterContainer, EnchanterBlockEntity.OUTPUT_SLOT, 127, 41)

		val upgradeSlot = FilteredSlot(enchanterContainer, EnchanterBlockEntity.UPGRADE_SLOT, 153, 5) {
			it.isItem(ModItemTagsProvider.SPEED_UPGRADES)
		}

		addSlot(leftInputSlot)
		addSlot(rightOutputSlot)
		addSlot(outputSlot)
		addSlot(upgradeSlot)
	}

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return enchanterContainer.stillValid(player)
	}

}