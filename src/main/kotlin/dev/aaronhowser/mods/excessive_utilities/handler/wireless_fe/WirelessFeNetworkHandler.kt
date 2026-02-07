package dev.aaronhowser.mods.excessive_utilities.handler.wireless_fe

import net.minecraft.core.HolderLookup
import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtUtils
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class WirelessFeNetworkHandler : SavedData() {

	private val networks: MutableMap<UUID, WirelessFeNetwork> = mutableMapOf()

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		val listTag = tag.getList(NETWORK_IDS_NBT, Tag.TAG_INT_ARRAY.toInt())

		for (gridId in networks.keys) {
			val uuidTag = NbtUtils.createUUID(gridId)
			listTag.add(uuidTag)
		}

		tag.put(NETWORK_IDS_NBT, listTag)
		return tag
	}

	companion object {
		const val NETWORK_IDS_NBT = "NetworkIDs"
		const val SAVED_DATA_NAME = "eu_fe_network_handler"

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): WirelessFeNetworkHandler {
			val handler = WirelessFeNetworkHandler()

			val listTag = tag.getList(NETWORK_IDS_NBT, Tag.TAG_INT_ARRAY.toInt())
			for (i in listTag.indices) {
				val intArray = listTag.getIntArray(i)
				val uuid = UUIDUtil.uuidFromIntArray(intArray)
				handler.networks[uuid] = WirelessFeNetwork(uuid)
			}

			return handler
		}

		fun get(level: ServerLevel): WirelessFeNetworkHandler {
			if (level != level.server.overworld()) {
				return get(level.server.overworld())
			}

			val storage = level.dataStorage
			val factory = Factory(::WirelessFeNetworkHandler, ::load)
			return storage.computeIfAbsent(factory, SAVED_DATA_NAME)
		}

	}

}