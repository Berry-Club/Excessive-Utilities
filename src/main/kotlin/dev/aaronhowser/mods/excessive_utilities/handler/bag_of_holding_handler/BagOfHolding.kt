package dev.aaronhowser.mods.excessive_utilities.handler.bag_of_holding_handler

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.ContainerHelper
import net.minecraft.world.SimpleContainer
import java.util.*

class BagOfHolding(
	val bagUUID: UUID
) {

	val container = SimpleContainer(9 * 6)

	fun toTag(registries: HolderLookup.Provider): CompoundTag {
		val tag = CompoundTag()

		tag.putUUID(UUID_TAG, bagUUID)
		ContainerHelper.saveAllItems(tag, container.items, registries)

		return tag
	}

	companion object {
		const val UUID_TAG = "BagUUID"

		fun fromTag(tag: CompoundTag, registries: HolderLookup.Provider): BagOfHolding {
			val bagUUID = tag.getUUID(UUID_TAG)
			val bag = BagOfHolding(bagUUID)

			ContainerHelper.loadAllItems(tag, bag.container.items, registries)

			return bag
		}
	}

}