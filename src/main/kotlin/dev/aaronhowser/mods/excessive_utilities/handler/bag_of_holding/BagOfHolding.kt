package dev.aaronhowser.mods.excessive_utilities.handler.bag_of_holding

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.item.BagOfHoldingItem
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.ContainerHelper
import net.minecraft.world.SimpleContainer
import net.minecraft.world.item.ItemStack
import java.util.*

class BagOfHolding(
	val bagUUID: UUID,
	private val onChanged: () -> Unit
) {

	var isActive = true
		private set

	val container = object : SimpleContainer(SLOT_COUNT) {
		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			return isActive && stack.item !is BagOfHoldingItem
		}

		override fun setChanged() {
			if (isActive) {
				onChanged()
			}
		}
	}

	fun takeItems(): List<ItemStack> {
		isActive = false
		val items = container.items.map(ItemStack::copy)
		container.clearContent()
		return items
	}

	fun isEmpty(): Boolean = container.items.all(ItemStack::isEmpty)

	fun toTag(registries: HolderLookup.Provider): CompoundTag {
		val tag = CompoundTag()

		tag.putUUID(UUID_TAG, bagUUID)
		tag.saveItems(container, registries)

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
			if (!tag.hasUUID(UUID_TAG)) return null

			val bagUUID = tag.getUUID(UUID_TAG)
			val bag = BagOfHolding(bagUUID, onChanged)

			ContainerHelper.loadAllItems(tag, bag.container.items, registries)

			return bag
		}
	}

}