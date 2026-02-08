package dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node

import net.minecraft.world.level.ChunkPos

object ClientFlatTransferNodes {

	private val nodesPerChunk: MutableMap<ChunkPos, List<FlatTransferNode>> = mutableMapOf()

	fun setNodesForChunk(chunkPos: ChunkPos, nodes: List<FlatTransferNode>) {
		nodesPerChunk[chunkPos] = nodes
	}

	fun getNodesInChunk(chunkPos: ChunkPos): List<FlatTransferNode> {
		return nodesPerChunk[chunkPos] ?: emptyList()
	}

}