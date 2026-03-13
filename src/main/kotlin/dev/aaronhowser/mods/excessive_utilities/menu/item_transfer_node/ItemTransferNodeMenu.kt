package dev.aaronhowser.mods.excessive_utilities.menu.item_transfer_node

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.ItemTransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack

class ItemTransferNodeMenu(
	containerId: Int,
	playerInventory: Inventory,
	val upgradesContainer: Container,
	val bufferContainer: Container,
	val filterContainer: Container,
	val containerData: ContainerData
) : MenuWithInventory(ModMenuTypes.FLAT_TRANSFER_NODE.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(TransferNodeBlockEntity.UPGRADE_CONTAINER_SIZE),
				SimpleContainer(ItemTransferNodeBlockEntity.BUFFER_CONTAINER_SIZE),
				SimpleContainer(ItemTransferNodeBlockEntity.FILTER_CONTAINER_SIZE),
				SimpleContainerData(ItemTransferNodeBlockEntity.CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(upgradesContainer, TransferNodeBlockEntity.UPGRADE_CONTAINER_SIZE)

		addSlots()
		addPlayerInventorySlots(84)
	}

	override fun addSlots() {
		val filterSlot = FilteredSlot(upgradesContainer, 0, 80, 34) { it.isItem(ModItemTagsProvider.FILTERS) }
		this.addSlot(filterSlot)
	}

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return upgradesContainer.stillValid(player)
	}

}