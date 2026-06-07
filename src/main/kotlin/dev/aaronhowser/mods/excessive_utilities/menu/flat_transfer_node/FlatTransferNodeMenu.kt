package dev.aaronhowser.mods.excessive_utilities.menu.flat_transfer_node

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.entity.FlatTransferNodeEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class FlatTransferNodeMenu(
	containerId: Int,
	playerInventory: Inventory,
	val filterContainer: Container,
	val filterEntity: FlatTransferNodeEntity
) : MenuWithInventory(ModMenuTypes.FLAT_TRANSFER_NODE.get(), containerId, playerInventory) {

	init {
		checkContainerSize(filterContainer, 1)

		addSlots()
		addPlayerInventorySlots(84)
	}

	fun isItemNode(): Boolean = filterEntity.isItemNode

	override fun addSlots() {
		val filterSlot = FilteredSlot(filterContainer, 0, 80, 34) { it.isItem(ModItemTagsProvider.FILTERS) }
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