package dev.aaronhowser.mods.excessive_utilities.menu.energy_transfer_node

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node.ItemTransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack

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
				SimpleContainerData(ItemTransferNodeBlockEntity.CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(upgradesContainer, TransferNodeBlockEntity.UPGRADE_CONTAINER_SIZE)

		addSlots()
		addPlayerInventorySlots(108)
	}

	override fun addSlots() {
		for (i in 0 until TransferNodeBlockEntity.UPGRADE_CONTAINER_SIZE) {
			val x = 35 + i * 18
			val y = 68

			val slot = FilteredSlot(upgradesContainer, i, x, y) {
				it.isItem(ModItemTagsProvider.TRANSFER_NODE_UPGRADES) || it.isItem(ModItemTagsProvider.RETRIEVAL_NODE_UPGRADES)
			}

			this.addSlot(slot)
		}
	}

	fun getPingX(): Int = containerData.get(ItemTransferNodeBlockEntity.X_DATA_INDEX)
	fun getPingY(): Int = containerData.get(ItemTransferNodeBlockEntity.Y_DATA_INDEX)
	fun getPingZ(): Int = containerData.get(ItemTransferNodeBlockEntity.Z_DATA_INDEX)

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return upgradesContainer.stillValid(player)
	}

}