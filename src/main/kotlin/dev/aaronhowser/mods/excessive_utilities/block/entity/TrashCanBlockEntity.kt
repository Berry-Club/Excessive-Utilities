package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.world.ContainerHelper
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

open class TrashCanBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.TRASH_CAN.get(), pos, state), MenuProvider {

	var isChest: Boolean = false
		set(value) {
			field = value
			setChanged()
		}

	private val filterContainer = ImprovedSimpleContainer(this, 1)
	val filterStack: ItemStack get() = filterContainer.getItem(FILTER_SLOT)

	private val itemHandler: IItemHandler =
		object : IItemHandler {
			override fun getSlots(): Int = 27
			override fun getStackInSlot(slot: Int): ItemStack = ItemStack.EMPTY
			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack = ItemStack.EMPTY
			override fun getSlotLimit(slot: Int): Int = 64
			override fun isItemValid(slot: Int, stack: ItemStack): Boolean = true

			override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
				return if (passesFilter(stack)) {
					ItemStack.EMPTY
				} else {
					stack
				}
			}
		}

	fun getItemHandler(direction: Direction?): IItemHandler = itemHandler

	// TODO: Implement filter logic
	fun passesFilter(stack: ItemStack): Boolean {
		if (filterStack.isEmpty) return true
		return true
	}

	override fun getDisplayName(): Component {
		return blockState.block.name
	}

	//TODO: Menu for the regular Trash Can
	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? {
		return if (isChest) {
			ChestMenu.threeRows(containerId, playerInventory)
		} else {
			ChestMenu.oneRow(containerId, playerInventory)
		}
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		tag.putBoolean(IS_CHEST_TAG, isChest)
		ContainerHelper.saveAllItems(tag, filterContainer.items, registries)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)
		isChest = tag.getBoolean(IS_CHEST_TAG)
		ContainerHelper.loadAllItems(tag, filterContainer.items, registries)
	}

	companion object {
		const val IS_CHEST_TAG = "IsChest"
		const val FILTER_SLOT = 0
	}

}