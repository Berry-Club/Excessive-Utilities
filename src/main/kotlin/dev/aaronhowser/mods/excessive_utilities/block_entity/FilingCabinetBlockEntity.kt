package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
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

	fun getItemCount(): Int = storedEntries.values.sum()

	fun getMaxAmount(): Int {
		return if (blockState.isBlock(ModBlocks.FILING_CABINET)) {
			ServerConfig.CONFIG.basicFilingCabinetCapacity.get()
		} else {
			ServerConfig.CONFIG.advancedFilingCabinetCapacity.get()
		}
	}

	private val itemHandler: IItemHandler =
		object : IItemHandler {
			override fun getSlots(): Int = getMaxAmount()

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
				val amountToInsert = stack.count.coerceAtMost(getMaxAmount() - currentTotalCount)
				if (amountToInsert <= 0) {
					return stack
				}

				val stackComponents = stack.componentsPatch
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

			override fun getSlotLimit(slot: Int): Int = getMaxAmount()

			override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
				if (storedItem == null) {
					return true
				}

				return stack.item == storedItem
			}
		}

	fun getItemHandler(direction: Direction): IItemHandler = itemHandler

	fun dropAllItems() {
		val level = level ?: return
		val item = storedItem ?: return

		for ((data, count) in storedEntries) {
			val stack = recreateStack(item, data)
			stack.count = count

			Block.popResource(level, worldPosition, stack)
		}

		storedItem = null
		storedEntries.clear()
		setChanged()
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val item = storedItem ?: return
		tag.putString(ITEM_NBT, item.builtInRegistryHolder().key().location().toString())

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

		val itemString = tag.getString(ITEM_NBT)
		if (itemString.isEmpty()) return

		val itemRk = ResourceKey.create(
			Registries.ITEM,
			ResourceLocation.parse(itemString)
		)

		val item = registries.lookupOrThrow(Registries.ITEM).getOrThrow(itemRk)
		storedItem = item.value()

		val entriesList = tag.getList(ENTRIES_NBT, Tag.TAG_COMPOUND.toInt())
		for (i in 0 until entriesList.size) {
			val tag = entriesList.getCompound(i)

			val count = tag.getInt(COUNT_NBT)
			val dataTag = tag.getCompound(DATA_NBT)

			val data = DataComponentPatch.CODEC.decode(NbtOps.INSTANCE, dataTag).getOrThrow().first
			storedEntries[data] = count
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

}