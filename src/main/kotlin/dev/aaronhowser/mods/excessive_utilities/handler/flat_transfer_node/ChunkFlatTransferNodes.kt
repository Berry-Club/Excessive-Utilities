package dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node

import com.mojang.serialization.Codec
import net.minecraft.server.level.ServerLevel

class ChunkFlatTransferNodes(
	val nodes: List<FlatTransferNode>
) {

	constructor() : this(emptyList())

	fun tick(level: ServerLevel) {
		for (node in nodes) {
			node.tick(level)
		}
	}

	companion object {
		val CODEC: Codec<ChunkFlatTransferNodes> =
			FlatTransferNode.CODEC.listOf()
				.xmap(::ChunkFlatTransferNodes, ChunkFlatTransferNodes::nodes)

		fun tickLevel(level: ServerLevel) {
			val chunks = level.chunkSource.
				.chunkMap


		}
	}

}