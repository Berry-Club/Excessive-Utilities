package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.registryHolder
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.resources.RegistryOps
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler

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

	private val itemHandler: IItemHandler = FilingCabinetItemHandler()

	fun getItemHandler(direction: Direction?): IItemHandler = itemHandler

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

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val item = storedItem ?: return
		tag.putString(
			ITEM_NBT,
			item.registryHolder()
				.key()
				.location()
				.toString()
		)

		val registryOps = RegistryOps.create(NbtOps.INSTANCE, registries)

		val entriesList = ListTag()
		for ((data, count) in storedEntries) {
			val entryTag = CompoundTag()
			entryTag.putInt(COUNT_NBT, count)

			val dataTag = DataComponentPatch.CODEC.encodeStart(registryOps, data).getOrThrow()
			entryTag.put(DATA_NBT, dataTag)

			entriesList.add(entryTag)
		}

		tag.put(ENTRIES_NBT, entriesList)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		storedItem = null
		storedEntries.clear()

		val itemString = tag.getString(ITEM_NBT)
		if (itemString.isEmpty()) return

		val itemId = ResourceLocation.tryParse(itemString) ?: return
		val itemRk = ResourceKey.create(
			Registries.ITEM,
			itemId
		)

		val item = registries
			.lookupOrThrow(Registries.ITEM)
			.get(itemRk)
			.orElse(null)
			?: return

		val registryOps = RegistryOps.create(NbtOps.INSTANCE, registries)
		val entriesList = tag.getList(ENTRIES_NBT, Tag.TAG_COMPOUND.toInt())
		for (i in entriesList.indices) {
			val entryTag = entriesList.getCompound(i)

			val savedCount = entryTag.getInt(COUNT_NBT)
			if (savedCount <= 0) continue

			val dataTag = entryTag.getCompound(DATA_NBT)
			val decodedData = DataComponentPatch.CODEC
				.parse(registryOps, dataTag)
				.result()
				.orElse(null)
				?: continue

			val currentCount = storedEntries.getOrDefault(decodedData, 0)
			val combinedCount = currentCount.toLong() + savedCount
			storedEntries[decodedData] = combinedCount
				.coerceAtMost(Int.MAX_VALUE.toLong())
				.toInt()
		}

		if (storedEntries.isNotEmpty()) {
			storedItem = item.value()
		}
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

	private inner class FilingCabinetItemHandler : IItemHandler {

		override fun getSlots(): Int = getMaxAmount()

		override fun getStackInSlot(slot: Int): ItemStack {
			if (slot !in 0 until slots) return ItemStack.EMPTY

			val item = storedItem ?: return ItemStack.EMPTY

			val (data, count) = storedEntries.entries.elementAtOrNull(slot) ?: return ItemStack.EMPTY
			val stack = recreateStack(item, data)
			stack.count = count.coerceAtMost(stack.maxStackSize)

			return stack
		}

		override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
			if (slot !in 0 until slots || stack.isEmpty) return stack

			val item = storedItem
			if (item != null && stack.item != item) {
				return stack
			}

			val currentTotalCount = getItemCount()
			val amountToInsert = stack.count.coerceAtMost(getMaxAmount() - currentTotalCount)
			if (amountToInsert <= 0) {
				return stack
			}

			val stackComponents = stack.componentsPatch
			val currentCount = storedEntries.getOrDefault(stackComponents, 0)

			if (!simulate) {
				if (storedItem == null) {
					storedItem = stack.item
				}

				storedEntries[stackComponents] = currentCount + amountToInsert
				setChanged()
			}

			val remainder = stack.copy()
			remainder.count = stack.count - amountToInsert
			return remainder
		}

		override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
			if (slot !in 0 until slots) return ItemStack.EMPTY

			val item = storedItem

			if (item == null || amount <= 0) {
				return ItemStack.EMPTY
			}

			val (data, amountStored) = storedEntries.entries.elementAtOrNull(slot) ?: return ItemStack.EMPTY

			val stack = recreateStack(item, data)
			val amountToRemove = amount
				.coerceAtMost(amountStored)
				.coerceAtMost(stack.maxStackSize)
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

			stack.count = amountToRemove
			return stack
		}

		override fun getSlotLimit(slot: Int): Int {
			if (slot !in 0 until slots) return 0

			return getMaxAmount()
		}

		override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
			if (slot !in 0 until slots || stack.isEmpty) return false

			if (storedItem == null) {
				return true
			}

			return stack.item == storedItem
		}
	}

}