package dev.aaronhowser.mods.excessive_utilities.handler.bag_of_holding_handler

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class BagOfHoldingHandler : SavedData() {

	private val bags: MutableMap<UUID, BagOfHolding> = mutableMapOf()

	fun getBag(uuid: UUID): BagOfHolding {
		return bags.getOrPut(uuid) { BagOfHolding(uuid) }
	}

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		val bagList = tag.getList(BAG_LIST_TAG, Tag.TAG_COMPOUND.toInt())

		for (bag in bags.values) {
			val bagTag = bag.toTag(registries)
			bagList.add(bagTag)
		}

		return tag
	}

	companion object {
		const val SAVED_DATA_NAME = "eu_bag_of_holding_handler"
		const val BAG_LIST_TAG = "Bags"

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): BagOfHoldingHandler {
			val handler = BagOfHoldingHandler()

			val bagList = tag.getList(BAG_LIST_TAG, Tag.TAG_COMPOUND.toInt())
			for (i in bagList.indices) {
				val bagTag = bagList.getCompound(i)
				val bag = BagOfHolding.fromTag(bagTag, provider)
				handler.bags[bag.bagUUID] = bag
			}

			return handler
		}

		fun get(level: ServerLevel): BagOfHoldingHandler {
			val storage = level.dataStorage
			val factory = Factory(::BagOfHoldingHandler, ::load)

			return storage.computeIfAbsent(factory, SAVED_DATA_NAME)
		}

	}

}