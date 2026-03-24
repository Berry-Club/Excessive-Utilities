package dev.aaronhowser.mods.excessive_utilities.menu.energy_transfer_node

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node.FluidTransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node.ItemTransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.network.FriendlyByteBuf
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
	val transferNode: FluidTransferNodeBlockEntity,
	val filterContainer: Container,
	val containerData: ContainerData
) : MenuWithInventory(ModMenuTypes.FLUID_TRANSFER_NODE.get(), containerId, playerInventory) {

	init {
		checkContainerSize(upgradesContainer, TransferNodeBlockEntity.UPGRADE_CONTAINER_SIZE)

		addSlots()
		addPlayerInventorySlots(91)
	}

	override fun addSlots() {
		val filterSlot = FilteredSlot(filterContainer, 0, 8, 58) { it.isItem(ModItemTagsProvider.FILTERS) }
		this.addSlot(filterSlot)

		for (i in 0 until TransferNodeBlockEntity.UPGRADE_CONTAINER_SIZE) {
			val x = 35 + i * 18
			val y = 58

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

	companion object {
		fun fromNetwork(
			containerId: Int,
			playerInventory: Inventory,
			data: FriendlyByteBuf
		): EnergyTransferNodeMenu {
			val upgradeContainer = SimpleContainer(TransferNodeBlockEntity.UPGRADE_CONTAINER_SIZE)
			val filterContainer = SimpleContainer(FluidTransferNodeBlockEntity.FILTER_CONTAINER_SIZE)
			val containerData = SimpleContainerData(FluidTransferNodeBlockEntity.CONTAINER_DATA_SIZE)

			val nodePos = data.readBlockPos()
			val node = playerInventory.player.level().getBlockEntity(nodePos)
			require(node is FluidTransferNodeBlockEntity) { "Opened Fluid Transfer Node menu for a block that isn't a Fluid Transfer Node" }

			return EnergyTransferNodeMenu(
				containerId,
				playerInventory,
				upgradeContainer,
				node,
				filterContainer,
				containerData
			)
		}
	}

}