package dev.aaronhowser.mods.excessive_utilities.menu.fluid_filter_menu

import dev.aaronhowser.mods.aaron.menu.MenuWithButtons
import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.item.FluidFilterItem
import dev.aaronhowser.mods.excessive_utilities.item.component.FluidFilterComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.fluids.FluidUtil
import net.neoforged.neoforge.items.ItemStackHandler
import net.neoforged.neoforge.items.SlotItemHandler

class FluidFilterMenu(
	containerId: Int,
	playerInventory: Inventory,
	val hand: InteractionHand
) : MenuWithInventory(ModMenuTypes.FLUID_FILTER.get(), containerId, playerInventory), MenuWithButtons {

	constructor(
		containerId: Int,
		playerInventory: Inventory,
		data: RegistryFriendlyByteBuf
	) : this(containerId, playerInventory, data.readEnum(InteractionHand::class.java))

	private val filterItems =
		object : ItemStackHandler(FluidFilterComponent.CONTAINER_SIZE) {
			override fun onContentsChanged(slot: Int) {
				val stackThere = getStackInSlot(slot)
				val filterStack = getFilterStack()
				FluidFilterItem.setStack(filterStack, slot, stackThere)
			}
		}

	init {
		val filterStack = getFilterStack()
		val container = FluidFilterItem.getFilterItems(filterStack)
		for ((i, ghostStack) in container.withIndex()) {
			filterItems.setStackInSlot(i, ghostStack)
		}

		addSlots()
		addPlayerInventorySlots(141)
	}

	private fun getFilterStack(): ItemStack = playerInventory.player.getItemInHand(hand)

	private fun getFlagComponent(): FluidFilterComponent = FluidFilterItem.getFilterComponent(getFilterStack())

	fun isInverted(): Boolean = getFlagComponent().isInverted
	fun useTags(): Boolean = getFlagComponent().useTags
	fun ignoreAllComponents(): Boolean = getFlagComponent().ignoreAllComponents

	override fun addSlots() {
		for (i in 0 until FluidFilterComponent.CONTAINER_SIZE) {
			val x = 53 + (i % 4) * 18
			val y = 29 + (i / 4) * 18

			val slot = object : SlotItemHandler(filterItems, i, x, y) {
				override fun mayPlace(stack: ItemStack): Boolean = FluidUtil.getFluidContained(stack).isPresent
				override fun mayPickup(player: Player): Boolean = true
				override fun getMaxStackSize(): Int = 1
				override fun getMaxStackSize(stack: ItemStack): Int = 1
			}

			addSlot(slot)
		}
	}

	override fun stillValid(player: Player): Boolean {
		return getFilterStack().isItem(ModItems.FLUID_FILTER)
	}

	override fun handleButtonPressed(buttonId: Int) {
		val filterStack = getFilterStack()

		val toggledFlag = when (buttonId) {
			TOGGLE_INVERTED_BUTTON_ID -> FluidFilterComponent.Flag.INVERTED
			TOGGLE_USE_TAGS_BUTTON_ID -> FluidFilterComponent.Flag.USE_TAGS
			TOGGLE_IGNORE_ALL_COMPONENTS_BUTTON_ID -> FluidFilterComponent.Flag.IGNORE_ALL_COMPONENTS
			else -> return
		}

		val flags = getFlagComponent().flags.toMutableList()

		if (toggledFlag in flags) {
			flags.remove(toggledFlag)
		} else {
			flags.add(toggledFlag)
		}

		FluidFilterItem.setFlags(filterStack, flags)
	}

	companion object {
		const val TOGGLE_INVERTED_BUTTON_ID = 0
		const val TOGGLE_USE_TAGS_BUTTON_ID = 1
		const val TOGGLE_IGNORE_ALL_COMPONENTS_BUTTON_ID = 2
	}

}