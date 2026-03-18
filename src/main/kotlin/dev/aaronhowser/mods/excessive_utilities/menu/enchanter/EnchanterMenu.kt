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

class EnchanterMenu(
	containerId: Int,
	playerInventory: Inventory,
	val enchanterContainer: Container,
	val enchanterContainerData: ContainerData
) : MenuWithInventory(ModMenuTypes.FURNACE.get(), containerId, playerInventory) {

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
		addPlayerInventorySlots(90)
	}

	override fun addSlots() {
		val leftInputSlot = Slot(enchanterContainer, EnchanterBlockEntity.LEFT_INPUT_SLOT, 63, 42)
		val rightOutputSlot = Slot(enchanterContainer, EnchanterBlockEntity.RIGHT_INPUT_SLOT, 63, 52)

		val outputSlot = OutputSlot(enchanterContainer, EnchanterBlockEntity.OUTPUT_SLOT, 110, 42)

		val upgradeSlot = FilteredSlot(enchanterContainer, EnchanterBlockEntity.UPGRADE_SLOT, 26, 54) {
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