package dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node

import com.mojang.serialization.Codec

class ChunkFlatTransferNodes(
	val nodes: List<FlatTransferNode>
) {

	constructor() : this(emptyList())

	companion object {
		val CODEC: Codec<ChunkFlatTransferNodes> =
			FlatTransferNode.CODEC.listOf()
				.xmap(::ChunkFlatTransferNodes, ChunkFlatTransferNodes::nodes)
	}

}