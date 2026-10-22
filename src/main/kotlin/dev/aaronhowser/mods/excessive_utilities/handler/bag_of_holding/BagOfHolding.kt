package dev.aaronhowser.mods.excessive_utilities.handler.bag_of_holding

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.item.BagOfHoldingItem
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.ContainerHelper
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack
import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.NbtOps
import java.util.*

class BagOfHolding(
	val bagUUID: UUID,
	private val onChanged: () -> Unit
) {

	var isActive = true
		private set

	val container = BagOfHoldingContainer()

	fun takeItems(): List<ItemStack> {
		isActive = false
		val items = container.items.map(ItemStack::copy)
		container.clearContent()
		return items
	}

	fun isEmpty(): Boolean = container.items.all(ItemStack::isEmpty)

	fun toTag(registries: HolderLookup.Provider): CompoundTag {
		val tag = CompoundTag()

		tag.store(UUID_TAG, UUIDUtil.CODEC, bagUUID)
		tag.store("Items", ItemStack.OPTIONAL_CODEC.listOf(), registries.createSerializationContext(NbtOps.INSTANCE), container.items)

		return tag
	}

	companion object {
		const val UUID_TAG = "BagUUID"
		const val SLOT_COUNT = 9 * 6

		fun fromTag(
			tag: CompoundTag,
			registries: HolderLookup.Provider,
			onChanged: () -> Unit
		): BagOfHolding? {
			if (!tag.read(UUID_TAG, UUIDUtil.CODEC).isPresent) return null

			val bagUUID = tag.read(UUID_TAG, UUIDUtil.CODEC).orElseThrow()
			val bag = BagOfHolding(bagUUID, onChanged)

			val items = tag.read("Items", ItemStack.OPTIONAL_CODEC.listOf(), registries.createSerializationContext(NbtOps.INSTANCE)).orElseThrow()
            require(items.size == SLOT_COUNT) { "Invalid bag inventory size: ${items.size}" }
            items.forEachIndexed { slot, stack -> bag.container.setItem(slot, stack) }

			return bag
		}
	}

	inner class BagOfHoldingContainer : SimpleContainer(SLOT_COUNT) {
		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			return isActive && stack.item !is BagOfHoldingItem
		}

		override fun setChanged() {
			if (isActive) {
				onChanged()
			}
		}
	}

}