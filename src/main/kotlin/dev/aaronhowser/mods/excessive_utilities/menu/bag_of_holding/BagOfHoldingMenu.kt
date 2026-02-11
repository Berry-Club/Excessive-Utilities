package dev.aaronhowser.mods.excessive_utilities.menu.bag_of_holding

import dev.aaronhowser.mods.excessive_utilities.menu.HeldItemContainerMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class BagOfHoldingMenu(
	containerId: Int,
	playerInventory: Inventory
) : HeldItemContainerMenu(
	ModItems.BAG_OF_HOLDING,
	ModMenuTypes.BAG_OF_HOLDING.get(),
	containerId,
	playerInventory
) {

	override val containerSlots: Int = 9 * 6

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

}