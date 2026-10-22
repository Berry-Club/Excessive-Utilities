package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.Identifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class FilingCabinetBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.FILING_CABINET.get(), pos, blockState) {

	private var storedItem: Item? = null
	private val storedEntries: MutableMap<DataComponentPatch, Int> = mutableMapOf()

	fun getItemCount(): Int {
		var total = 0L
		for (count in storedEntries.values) {
			total += count
		}

		return total.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
	}

	fun getMaxAmount(): Int {
		return if (blockState.isBlock(ModBlocks.FILING_CABINET)) {
			ServerConfig.CONFIG.basicFilingCabinetCapacity.get()
		} else {
			ServerConfig.CONFIG.advancedFilingCabinetCapacity.get()
		}
	}

	private val itemHandler: ResourceHandler<ItemResource> = FilingCabinetItemHandler()

	fun getItemHandler(direction: Direction?): ResourceHandler<ItemResource> = itemHandler

	fun dropAllItems() {
		val level = level ?: return
		val item = storedItem ?: return

		for ((data, count) in storedEntries) {
			var remaining = count
			while (remaining > 0) {
				val stack = recreateStack(item, data)
				stack.count = remaining.coerceAtMost(stack.maxStackSize)

				Block.popResource(level, worldPosition, stack)
				remaining -= stack.count
			}
		}

		storedItem = null
		storedEntries.clear()
		setChanged()
	}

    override fun saveAdditional(tag: ValueOutput) {
        super.saveAdditional(tag)
        val item = storedItem ?: return
        tag.store(ITEM_NBT, Item.CODEC, BuiltInRegistries.ITEM.wrapAsHolder(item))
        val entries = tag.childrenList(ENTRIES_NBT)
        for ((data, count) in storedEntries) {
            require(count > 0) { "Filing Cabinet contains a non-positive item count" }
            entries.addChild().apply {
                putInt(COUNT_NBT, count)
                store(DATA_NBT, DataComponentPatch.CODEC, data)
            }
        }
    }

    override fun loadAdditional(tag: ValueInput) {
        super.loadAdditional(tag)
        storedItem = null
        storedEntries.clear()
        val item = tag.read(ITEM_NBT, Item.CODEC).orElse(null) ?: return
        val entries = tag.childrenList(ENTRIES_NBT).orElseThrow()
        for (entry in entries) {
            val count = entry.getInt(COUNT_NBT).orElseThrow()
            require(count > 0) { "Invalid Filing Cabinet item count: $count" }
            val data = entry.read(DATA_NBT, DataComponentPatch.CODEC).orElseThrow()
            storedEntries[data] = Math.addExact(storedEntries.getOrDefault(data, 0), count)
        }
        if (storedEntries.isNotEmpty()) storedItem = item.value()
    }

	companion object {
		const val ITEM_NBT = "StoredItem"
		const val ENTRIES_NBT = "StoredEntries"
		const val COUNT_NBT = "Count"
		const val DATA_NBT = "Data"

		private fun recreateStack(item: Item, data: DataComponentPatch): ItemStack {
			val stack = ItemStack(item)
			stack.applyComponents(data)
			return stack
		}
	}

	private data class FilingSnapshot(val item: Item?, val entries: Map<DataComponentPatch, Int>)

	private inner class FilingCabinetItemHandler : SnapshotJournal<FilingSnapshot>(), ResourceHandler<ItemResource> {

		override fun createSnapshot(): FilingSnapshot = FilingSnapshot(storedItem, storedEntries.toMap())
		override fun revertToSnapshot(snapshot: FilingSnapshot) {
			storedItem = snapshot.item
			storedEntries.clear()
			storedEntries.putAll(snapshot.entries)
		}
		override fun onRootCommit(originalState: FilingSnapshot) { setChanged() }

		override fun size(): Int = getMaxAmount()

		override fun getResource(index: Int): ItemResource {
			if (index !in 0 until size()) return ItemResource.EMPTY

			val item = storedItem ?: return ItemResource.EMPTY

			val (data, count) = storedEntries.entries.elementAtOrNull(index) ?: return ItemResource.EMPTY
			val stack = recreateStack(item, data)
			stack.count = count.coerceAtMost(stack.maxStackSize)
			return ItemResource.of(stack)
		}

		override fun getAmountAsLong(index: Int): Long {
			if (index !in 0 until size()) return 0
			return storedEntries.entries.elementAtOrNull(index)?.value?.toLong() ?: 0
		}

		override fun getCapacityAsLong(index: Int, resource: ItemResource): Long = 64

		override fun isValid(index: Int, resource: ItemResource): Boolean =
			index in 0 until size() && (resource.isEmpty() || storedItem == null || resource.toStack().item == storedItem)

		override fun insert(index: Int, resource: ItemResource, amount: Int, transaction: TransactionContext): Int {
			if (!isValid(index, resource) || resource.isEmpty() || amount <= 0) return 0
			val stack = resource.toStack(amount)

			val item = storedItem
			if (item != null && stack.item != item) {
				return 0
			}

			val currentTotalCount = getItemCount()
			val amountToInsert = stack.count.coerceAtMost(getMaxAmount() - currentTotalCount)
			if (amountToInsert <= 0) {
				return 0
			}

			val stackComponents = stack.componentsPatch
			val currentCount = storedEntries.getOrDefault(stackComponents, 0)

			updateSnapshots(transaction)
			if (storedItem == null) storedItem = stack.item
			storedEntries[stackComponents] = currentCount + amountToInsert
			return amountToInsert
		}

		override fun extract(index: Int, resource: ItemResource, amount: Int, transaction: TransactionContext): Int {
			if (index !in 0 until size() || amount <= 0) return 0

			val item = storedItem

			if (item == null || amount <= 0) {
				return 0
			}

			val (data, amountStored) = storedEntries.entries.elementAtOrNull(index) ?: return 0

			val stack = recreateStack(item, data)
			val amountToRemove = amount
				.coerceAtMost(amountStored)
				.coerceAtMost(stack.maxStackSize)
			if (amountToRemove <= 0) {
				return 0
			}

			updateSnapshots(transaction)
			val newCount = amountStored - amountToRemove

				if (newCount <= 0) {
					storedEntries.remove(data)
					if (storedEntries.isEmpty()) {
						storedItem = null
					}
				} else {
					storedEntries[data] = newCount
				}

			return amountToRemove
		}
	}

}
