package dev.aaronhowser.mods.excessive_utilities.menu

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.excessive_utilities.entity.FlatTransferNodeEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
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
		TODO("Not yet implemented")
	}

	override fun stillValid(player: Player): Boolean {
		TODO("Not yet implemented")
	}

}