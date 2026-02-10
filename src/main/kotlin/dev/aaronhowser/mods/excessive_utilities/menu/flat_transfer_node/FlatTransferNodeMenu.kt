package dev.aaronhowser.mods.excessive_utilities.menu.flat_transfer_node

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.excessive_utilities.entity.FlatTransferNodeEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
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
	val filterEntity: FlatTransferNodeEntity?
) : MenuWithInventory(ModMenuTypes.FLAT_TRANSFER_NODE.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(1),
				null
			)

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		if (filterEntity == null) return true // Client side

		return filterEntity.isAlive
				&& filterContainer.stillValid(player)
				&& player.canInteractWithEntity(filterEntity, player.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE))
	}

}