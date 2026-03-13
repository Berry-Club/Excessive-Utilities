package dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu

import dev.aaronhowser.mods.aaron.menu.MenuWithButtons
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
) : MenuWithInventory(ModMenuTypes.ITEM_FILTER.get(), containerId, playerInventory), MenuWithButtons {

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

	private fun getFilterStack(): ItemStack = playerInventory.player.getItemInHand(hand)

	private fun getFlags(): Set<ItemFilterItem.Flag> = ItemFilterItem.getFlags(getFilterStack())

	fun isInverted(): Boolean = ItemFilterItem.Flag.INVERTED in getFlags()
	fun useTags(): Boolean = ItemFilterItem.Flag.USE_TAGS in getFlags()
	fun ignoreDamage(): Boolean = ItemFilterItem.Flag.IGNORE_DAMAGE in getFlags()
	fun ignoreAllComponents(): Boolean = ItemFilterItem.Flag.IGNORE_ALL_COMPONENTS in getFlags()

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

	override fun handleButtonPressed(buttonId: Int) {
		val filterStack = getFilterStack()

		val toggledFlag = when (buttonId) {
			TOGGLE_INVERTED_BUTTON_ID -> ItemFilterItem.Flag.INVERTED
			TOGGLE_USE_TAGS_BUTTON_ID -> ItemFilterItem.Flag.USE_TAGS
			TOGGLE_IGNORE_DAMAGE_BUTTON_ID -> ItemFilterItem.Flag.IGNORE_DAMAGE
			TOGGLE_IGNORE_ALL_COMPONENTS_BUTTON_ID -> ItemFilterItem.Flag.IGNORE_ALL_COMPONENTS
			else -> return
		}

		val flags = getFlags().toMutableList()

		if (toggledFlag in flags) {
			flags.remove(toggledFlag)
		} else {
			flags.add(toggledFlag)
		}

		ItemFilterItem.setFlags(filterStack, *flags.toTypedArray())
	}

	companion object {
		const val TOGGLE_INVERTED_BUTTON_ID = 0
		const val TOGGLE_USE_TAGS_BUTTON_ID = 1
		const val TOGGLE_IGNORE_DAMAGE_BUTTON_ID = 2
		const val TOGGLE_IGNORE_ALL_COMPONENTS_BUTTON_ID = 3
	}

}