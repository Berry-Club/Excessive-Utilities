package dev.aaronhowser.mods.excessive_utilities.handler.bag_of_holding

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedDataType
import java.util.*

class BagOfHoldingHandler : SavedData() {

	private val bags: MutableMap<UUID, BagOfHolding> = mutableMapOf()

	fun getOrCreateBag(uuid: UUID): BagOfHolding {
		val existingBag = bags[uuid]
		if (existingBag != null) return existingBag

		val bag = BagOfHolding(uuid, ::setDirty)
		bags[uuid] = bag
		setDirty()
		return bag
	}

	fun removeBag(uuid: UUID): List<ItemStack> {
		val bag = bags.remove(uuid) ?: return emptyList()
		val items = bag.takeItems()
		setDirty()
		return items
	}

	fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		val bagList = ListTag()

		for (bag in bags.values) {
			if (bag.isEmpty()) continue

			val bagTag = bag.toTag(registries)
			bagList.add(bagTag)
		}

		tag.put(BAG_LIST_TAG, bagList)
		return tag
	}

	companion object {
		const val SAVED_DATA_NAME = "eu_bag_of_holding_handler"
		const val BAG_LIST_TAG = "Bags"

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): BagOfHoldingHandler {
			val handler = BagOfHoldingHandler()

			val bagList = tag.getList(BAG_LIST_TAG).orElseThrow()
			for (i in bagList.indices) {
				val bagTag = bagList.getCompound(i).orElseThrow()
				val bag = BagOfHolding.fromTag(bagTag, provider, handler::setDirty) ?: continue
				handler.bags[bag.bagUUID] = bag
			}

			return handler
		}

		fun get(level: ServerLevel): BagOfHoldingHandler {
			if (level != level.server.overworld()) {
				return get(level.server.overworld())
			}

			val storage = level.dataStorage
			val type = SavedDataType(
                dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities.modResource(SAVED_DATA_NAME),
                ::BagOfHoldingHandler,
                CompoundTag.CODEC.xmap(
                    { tag -> load(tag, level.registryAccess()) },
                    { data -> data.save(CompoundTag(), level.registryAccess()) }
                ),
                DataFixTypes.SAVED_DATA_COMMAND_STORAGE
            )
            return storage.computeIfAbsent(type)
		}

	}

}