package dev.aaronhowser.mods.excessive_utilities.menu.flat_transfer_node

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.excessive_utilities.entity.FlatTransferNodeEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot

class FlatTransferNodeMenu(
	containerId: Int,
	playerInventory: Inventory,
	val filterContainer: Container,
	val filterEntity: FlatTransferNodeEntity
) : MenuWithInventory(ModMenuTypes.FLAT_TRANSFER_NODE.get(), containerId, playerInventory) {

	init {
		checkContainerSize(filterContainer, 1)

		addSlots(84)
	}

	fun isItemNode(): Boolean = filterEntity.isItemNode

	override fun addContainerSlots() {
		val filterSlot = Slot(filterContainer, 0, 80, 34)
		this.addSlot(filterSlot)
	}

	override fun stillValid(player: Player): Boolean {
		return filterEntity.isAlive
				&& filterContainer.stillValid(player)
				&& player.canInteractWithEntity(filterEntity, player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE))
	}

	companion object {
		fun fromNetwork(
			containerId: Int,
			playerInventory: Inventory,
			data: FriendlyByteBuf
		): FlatTransferNodeMenu {
			val entityId = data.readInt()
			val entity = playerInventory.player.level().getEntity(entityId) as FlatTransferNodeEntity

			return FlatTransferNodeMenu(containerId, playerInventory, SimpleContainer(1), entity)
		}
	}

}