package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.item.ItemFilterItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
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
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class TrashCanBlockEntity(
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

	private val itemHandler: ResourceHandler<ItemResource> =
		object : ResourceHandler<ItemResource> {
			override fun size(): Int = 27
			override fun getResource(index: Int): ItemResource = ItemResource.EMPTY
			override fun getAmountAsLong(index: Int): Long = 0
			override fun getCapacityAsLong(index: Int, resource: ItemResource): Long = 64
			override fun isValid(index: Int, resource: ItemResource): Boolean = true
			override fun insert(index: Int, resource: ItemResource, amount: Int, transaction: TransactionContext): Int {
			return if (resource.isEmpty() || !passesFilter(resource.toStack())) 0 else amount
		}
			override fun extract(index: Int, resource: ItemResource, amount: Int, transaction: TransactionContext): Int = 0
		}

	fun getItemHandler(direction: Direction?): ResourceHandler<ItemResource> = itemHandler

	fun passesFilter(stack: ItemStack): Boolean {
		if (filterStack.isEmpty) return true
		return ItemFilterItem.passesFilter(filterStack, stack)
	}

	override fun getDisplayName(): Component {
		return blockState.block.name
	}

	//TODO: Make a new menu that has a slot for the Filter
	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? {
		return if (isChest) {
			ChestMenu.threeRows(containerId, playerInventory)
		} else {
			ChestMenu.oneRow(containerId, playerInventory)
		}
	}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)
		tag.putBoolean(IS_CHEST_TAG, isChest)
		ContainerHelper.saveAllItems(tag, filterContainer.items)
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)
		isChest = tag.getBooleanOr(IS_CHEST_TAG, false)
		ContainerHelper.loadAllItems(tag, filterContainer.items)
	}

	companion object {
		const val IS_CHEST_TAG = "IsChest"
		const val FILTER_SLOT = 0
	}

}
