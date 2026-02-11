package dev.aaronhowser.mods.excessive_utilities.menu

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.registries.DeferredItem

abstract class HeldItemContainerMenu(
	protected val item: Item,
	menuType: MenuType<*>?,
	containerId: Int,
	playerInventory: Inventory,
) : MenuWithInventory(menuType, containerId, playerInventory) {

	constructor(
		deferredItem: DeferredItem<out Item>,
		menuType: MenuType<*>?,
		containerId: Int,
		playerInventory: Inventory
	) : this(deferredItem.get(), menuType, containerId, playerInventory)

	open fun getHeldItemStack(): ItemStack {
		return if (playerInventory.player.mainHandItem.isItem(item)) {
			playerInventory.player.mainHandItem
		} else {
			playerInventory.player.offhandItem
		}
	}

	protected val hand: InteractionHand =
		if (playerInventory
				.player
				.getItemInHand(InteractionHand.MAIN_HAND) === getHeldItemStack()
		) InteractionHand.MAIN_HAND else InteractionHand.OFF_HAND

	override fun stillValid(player: Player): Boolean {
		return player.getItemInHand(hand).isItem(item)
	}

}