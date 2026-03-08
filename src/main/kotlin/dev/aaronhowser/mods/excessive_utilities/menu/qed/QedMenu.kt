package dev.aaronhowser.mods.excessive_utilities.menu.qed

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.excessive_utilities.block.entity.QedBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class QedMenu(
	containerId: Int,
	playerInventory: Inventory,
	val qedContainer: Container,
) : MenuWithInventory(ModMenuTypes.QED.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(QedBlockEntity.CONTAINER_SIZE),
			)

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return qedContainer.stillValid(player)
	}

}