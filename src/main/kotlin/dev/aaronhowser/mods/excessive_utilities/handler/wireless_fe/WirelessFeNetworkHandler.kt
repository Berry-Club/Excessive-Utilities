package dev.aaronhowser.mods.excessive_utilities.handler.wireless_fe

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedDataType
import java.util.*

class WirelessFeNetworkHandler : SavedData() {

	private val networks: MutableMap<UUID, WirelessFeNetwork> = mutableMapOf()

	fun getNetwork(playerUuid: UUID): WirelessFeNetwork {
		return networks.getOrPut(playerUuid) { WirelessFeNetwork(playerUuid) }
	}

	fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
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
			val type = SavedDataType(
                dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities.modResource(SAVED_DATA_NAME),
                ::WirelessFeNetworkHandler,
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