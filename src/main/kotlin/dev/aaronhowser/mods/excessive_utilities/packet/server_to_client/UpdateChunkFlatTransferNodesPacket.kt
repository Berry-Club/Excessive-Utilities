package dev.aaronhowser.mods.excessive_utilities.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node.FlatTransferNode
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.level.ChunkPos

class UpdateChunkFlatTransferNodesPacket(
	val chunkPos: ChunkPos,
	val nodes: List<FlatTransferNode>
) : AaronPacket() {

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
		TODO("Not yet implemented")
	}

}