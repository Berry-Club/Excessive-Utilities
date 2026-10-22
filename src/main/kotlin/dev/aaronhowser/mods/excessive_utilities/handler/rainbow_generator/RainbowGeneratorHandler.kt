package dev.aaronhowser.mods.excessive_utilities.handler.rainbow_generator

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedDataType
import java.util.*

class RainbowGeneratorHandler : SavedData() {

	private val generatorNetworks: MutableMap<UUID, PlayerGeneratorNetwork> = mutableMapOf()

	fun getGeneratorNetwork(player: Player): PlayerGeneratorNetwork {
		return getGeneratorNetwork(player.uuid)
	}

	fun getGeneratorNetwork(uuid: UUID): PlayerGeneratorNetwork {
		return generatorNetworks.getOrPut(uuid) { PlayerGeneratorNetwork(uuid) }
	}

	fun tick(level: ServerLevel) {
		generatorNetworks.values.forEach { network -> network.tick(level) }
		generatorNetworks.entries.removeIf { (_, network) -> network.isEmpty() }
	}

	fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		return tag
	}

	companion object {
		const val SAVED_DATA_NAME = "eu_rainbow_generator_handler"

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): RainbowGeneratorHandler {
			return RainbowGeneratorHandler()
		}

		fun get(level: ServerLevel): RainbowGeneratorHandler {
			if (level != level.server.overworld()) {
				return get(level.server.overworld())
			}

			val storage = level.dataStorage
			val type = SavedDataType(
                dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities.modResource(SAVED_DATA_NAME),
                ::RainbowGeneratorHandler,
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