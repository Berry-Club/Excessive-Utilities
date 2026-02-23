package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponentMap
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

class FilingCabinetBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.FILING_CABINET.get(), pos, blockState) {

	private var storedItem: Item? = null
	private val storedEntries: MutableMap<DataComponentMap, Int> = mutableMapOf()

	fun getItemCount(): Int = storedEntries.values.sum()

	private val itemHandler: IItemHandler =
		object : IItemHandler {
			override fun getSlots(): Int = MAX_ITEMS

			override fun getStackInSlot(slot: Int): ItemStack {
				val item = storedItem ?: return ItemStack.EMPTY

				val (data, count) = storedEntries.entries.elementAtOrNull(slot) ?: return ItemStack.EMPTY
				val stack = recreateStack(item, data)
				stack.count = count

				return stack
			}

			override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
				if (storedItem == null) {
					storedItem = stack.item
				}

				if (stack.item != storedItem) {
					return stack
				}

				val currentTotalCount = getItemCount()
				val amountToInsert = stack.count.coerceAtMost(MAX_ITEMS - currentTotalCount)
				if (amountToInsert <= 0) {
					return stack
				}

				val stackComponents = stack.components
				val currentCount = storedEntries.getOrDefault(stackComponents, 0)

				if (!simulate) {
					storedEntries[stackComponents] = currentCount + amountToInsert
					setChanged()
				}

				val remainder = stack.copy()
				remainder.count = stack.count - amountToInsert
				return remainder
			}

			override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
				val item = storedItem

				if (item == null || amount <= 0) {
					return ItemStack.EMPTY
				}

				val (data, amountStored) = storedEntries.entries.elementAtOrNull(slot) ?: return ItemStack.EMPTY

				val amountToRemove = amount.coerceAtMost(amountStored)
				if (amountToRemove <= 0) {
					return ItemStack.EMPTY
				}

				if (!simulate) {
					val newCount = amountStored - amountToRemove

					if (newCount <= 0) {
						storedEntries.remove(data)
						if (storedEntries.isEmpty()) {
							storedItem = null
						}
					} else {
						storedEntries[data] = newCount
					}

					setChanged()
				}

				val stack = recreateStack(item, data)
				stack.count = amountToRemove
				return stack
			}

			override fun getSlotLimit(slot: Int): Int {
				TODO("Not yet implemented")
			}

			override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
				TODO("Not yet implemented")
			}

		}

	companion object {
		const val ITEM_NBT = "StoredItem"
		const val ENTRIES_NBT = "StoredEntries"

		const val MAX_ITEMS = 540

		private fun recreateStack(item: Item, data: DataComponentMap): ItemStack {
			val stack = ItemStack(item)
			stack.applyComponents(data)
			return stack
		}
	}

}