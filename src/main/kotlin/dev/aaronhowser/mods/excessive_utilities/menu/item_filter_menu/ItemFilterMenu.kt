package dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

class ItemFilterMenu(
	containerId: Int,
	playerInventory: Inventory,
	val hand: InteractionHand
) : MenuWithInventory(ModMenuTypes.ITEM_TRANSFER_NODE.get(), containerId, playerInventory) {

	private val filterStack: ItemStack
		get() = playerInventory.player.getItemInHand(hand)

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return filterStack.isItem(ModItems.ITEM_FILTER)
	}
}