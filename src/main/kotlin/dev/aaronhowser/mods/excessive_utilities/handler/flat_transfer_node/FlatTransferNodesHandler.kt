package dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.saveddata.SavedData

class FlatTransferNodesHandler : SavedData() {

	private val nodes: MutableSet<FlatTransferNode> = mutableSetOf()
	private val nodesPerChunk: MutableMap<ChunkPos, MutableSet<FlatTransferNode>> = mutableMapOf()

	fun getNodesInChunk(chunkPos: ChunkPos): Set<FlatTransferNode> {
		return nodesPerChunk.get(chunkPos)?.toSet() ?: emptySet()
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
		TODO("Not yet implemented")
	}


}