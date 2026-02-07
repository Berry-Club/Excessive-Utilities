package dev.aaronhowser.mods.excessive_utilities.handler.wireless_fe

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData
import java.util.*

class WirelessFeNetworkHandler : SavedData() {

	private val networks: MutableMap<UUID, WirelessFeNetwork> = mutableMapOf()

	fun getNetwork(playerUuid: UUID): WirelessFeNetwork {
		return networks.getOrPut(playerUuid) { WirelessFeNetwork(playerUuid) }
	}

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		return tag
	}

	companion object {
		const val SAVED_DATA_NAME = "eu_fe_network_handler"

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): WirelessFeNetworkHandler {
			return WirelessFeNetworkHandler()
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