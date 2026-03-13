package dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.item.ItemFilterItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.SlotItemHandler

class ItemFilterMenu(
	containerId: Int,
	playerInventory: Inventory,
	val hand: InteractionHand
) : MenuWithInventory(ModMenuTypes.ITEM_FILTER.get(), containerId, playerInventory) {

	constructor(
		containerId: Int,
		playerInventory: Inventory,
		data: RegistryFriendlyByteBuf
	) : this(containerId, playerInventory, data.readEnum(InteractionHand::class.java))

	private val filterItems =
		object : ItemStackHandler(ItemFilterItem.CONTAINER_SIZE) {
			override fun onContentsChanged(slot: Int) {
				val stackThere = getStackInSlot(slot)
				val filterStack = getFilterStack()
				ItemFilterItem.placeGhostInSlot(filterStack, slot, stackThere)
			}
		}

	init {
		val filterStack = getFilterStack()
		for (i in 0 until ItemFilterItem.CONTAINER_SIZE) {
			val ghostStack = ItemFilterItem.getGhostStack(filterStack, i)
			filterItems.setStackInSlot(i, ghostStack)
		}

		addSlots()
		addPlayerInventorySlots(91)
	}

	private fun getFilterStack(): ItemStack {
		return playerInventory.player.getItemInHand(hand)
	}

	override fun addSlots() {
		for (i in 0 until ItemFilterItem.CONTAINER_SIZE) {
			val x = 8 + (i % 4) * 18
			val y = 8 + (i / 4) * 18

			val slot = object : SlotItemHandler(filterItems, i, x, y) {
				override fun mayPlace(stack: ItemStack): Boolean = true
				override fun mayPickup(player: Player): Boolean = true
				override fun getMaxStackSize(): Int = 1
				override fun getMaxStackSize(stack: ItemStack): Int = 1
			}

			addSlot(slot)
		}
	}

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return getFilterStack().isItem(ModItems.ITEM_FILTER)
	}
}