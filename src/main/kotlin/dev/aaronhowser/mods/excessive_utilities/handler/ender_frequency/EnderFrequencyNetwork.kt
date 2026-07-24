package dev.aaronhowser.mods.excessive_utilities.handler.ender_frequency

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isTrue
import dev.aaronhowser.mods.excessive_utilities.block.TransferNodeBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.item.component.EnderFrequencyComponent
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceKey
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.saveddata.SavedData

class EnderFrequencyNetwork : SavedData() {

	private val receivers: MutableMap<NetworkKey, LinkedHashSet<Endpoint>> = mutableMapOf()
	private val roundRobinCursors: MutableMap<NetworkKey, Int> = mutableMapOf()

	fun registerReceiver(node: TransferNodeBlockEntity, frequency: EnderFrequencyComponent) {
		val level = node.level as? ServerLevel ?: return
		val key = NetworkKey(frequency, node.nodeType)
		receivers.getOrPut(key, ::linkedSetOf)
			.add(
				Endpoint(
					level.dimension(),
					node.blockPos
				)
			)
	}

	fun unregisterReceiver(node: TransferNodeBlockEntity, frequency: EnderFrequencyComponent) {
		val level = node.level as? ServerLevel ?: return
		val key = NetworkKey(frequency, node.nodeType)

		val endpoints = receivers[key] ?: return
		endpoints.remove(Endpoint(level.dimension(), node.blockPos))

		if (endpoints.isEmpty()) {
			receivers.remove(key)
			roundRobinCursors.remove(key)
		}
	}

	fun visitReceivers(
		server: MinecraftServer,
		frequency: EnderFrequencyComponent,
		nodeType: TransferNodeBlock.Type,
		visitor: (TransferNodeBlockEntity) -> Boolean
	): Boolean {
		val key = NetworkKey(frequency, nodeType)
		val endpoints = receivers[key] ?: return false
		if (endpoints.isEmpty()) return false

		val snapshot = endpoints.toList()
		val start = (roundRobinCursors[key] ?: 0).mod(snapshot.size)
		var didTransfer = false

		for (offset in snapshot.indices) {
			val index = (start + offset) % snapshot.size
			val endpoint = snapshot[index]
			val level = server.getLevel(endpoint.dimension)

			val node = if (level?.isLoaded(endpoint.pos).isTrue()) {
				level.getBlockEntity(endpoint.pos) as? TransferNodeBlockEntity
			} else {
				null
			}

			if (node == null || !node.isReceiverFor(frequency, nodeType)) {
				endpoints.remove(endpoint)
				continue
			}

			if (visitor(node)) {
				roundRobinCursors[key] = (index + 1) % snapshot.size
				didTransfer = true
			}
		}

		return didTransfer
	}

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag = tag

	data class NetworkKey(
		val frequency: EnderFrequencyComponent,
		val nodeType: TransferNodeBlock.Type
	)

	data class Endpoint(
		val dimension: ResourceKey<Level>,
		val pos: BlockPos
	)

	companion object {
		private const val SAVED_DATA_NAME = "eu_ender_frequency_network"

		private fun load(tag: CompoundTag, registries: HolderLookup.Provider) = EnderFrequencyNetwork()

		fun get(server: MinecraftServer): EnderFrequencyNetwork {
			val storage = server.overworld().dataStorage
			return storage.computeIfAbsent(Factory(::EnderFrequencyNetwork, ::load), SAVED_DATA_NAME)
		}
	}
}