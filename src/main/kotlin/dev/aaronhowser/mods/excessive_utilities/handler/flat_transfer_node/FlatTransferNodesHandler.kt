package dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.saveddata.SavedData

class FlatTransferNodesHandler : SavedData() {

	private val nodes: MutableSet<FlatTransferNode> = mutableSetOf()
	private val nodesPerChunk: MutableMap<ChunkPos, MutableSet<FlatTransferNode>> = mutableMapOf()

	fun getNodesInChunk(chunkPos: ChunkPos): Set<FlatTransferNode> {
		return nodesPerChunk[chunkPos] ?: emptySet()
	}

	fun addNode(node: FlatTransferNode) {
		nodes.add(node)

		val chunkPos = node.chunkPos
		val chunkNodes = nodesPerChunk.getOrPut(chunkPos) { mutableSetOf() }
		chunkNodes.add(node)
	}

	fun removeNode(node: FlatTransferNode) {
		nodes.remove(node)

		val chunkPos = node.chunkPos
		val chunkNodes = nodesPerChunk[chunkPos]
		if (chunkNodes != null) {
			chunkNodes.remove(node)
			if (chunkNodes.isEmpty()) {
				nodesPerChunk.remove(chunkPos)
			}
		}
	}

	fun tick(level: ServerLevel) {
		for (node in nodes) {
			node.tick(level)
		}
	}

	override fun save(tag: CompoundTag, registries: HolderLookup.Provider): CompoundTag {
		val nodesList = tag.getList(NODES_NBT, Tag.TAG_COMPOUND.toInt())

		for (node in nodes) {
			nodesList.add(node.toTag())
		}

		tag.put(NODES_NBT, nodesList)
		return tag
	}

	companion object {
		const val SAVED_DATA_NAME = "eu_flat_transfer_nodes_handler"
		const val NODES_NBT = "nodes"

		private fun load(tag: CompoundTag, provider: HolderLookup.Provider): FlatTransferNodesHandler {
			val handler = FlatTransferNodesHandler()

			val nodesList = tag.getList(NODES_NBT, Tag.TAG_COMPOUND.toInt())

			for (i in nodesList.indices) {
				val tag = nodesList.getCompound(i)
				val node = FlatTransferNode.fromTag(tag)
				handler.addNode(node)
			}

			return handler
		}

		fun get(level: ServerLevel): FlatTransferNodesHandler {
			val storage = level.dataStorage
			val factory = Factory(::FlatTransferNodesHandler, ::load)

			return storage.computeIfAbsent(factory, SAVED_DATA_NAME)
		}

	}


}