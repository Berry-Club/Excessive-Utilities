package dev.aaronhowser.mods.excessive_utilities.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node.ClientFlatTransferNodes
import dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node.FlatTransferNode
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.level.ChunkPos
import net.neoforged.neoforge.network.handling.IPayloadContext

class UpdateChunkFlatTransferNodesPacket(
	val chunkPos: ChunkPos,
	val nodes: List<FlatTransferNode>
) : AaronPacket() {

	constructor(chunkLong: Long, nodes: List<FlatTransferNode>) : this(ChunkPos(chunkLong), nodes)

	val chunkLong = chunkPos.toLong()

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> {
		return TYPE
	}

	override fun handleOnClient(context: IPayloadContext) {
		ClientFlatTransferNodes.setNodesForChunk(chunkPos, nodes)
	}

	companion object {
		val TYPE: CustomPacketPayload.Type<UpdateChunkFlatTransferNodesPacket> =
			CustomPacketPayload.Type(ExcessiveUtilities.modResource("update_flat_transfer_nodes"))

		val STREAM_CODEC: StreamCodec<ByteBuf, UpdateChunkFlatTransferNodesPacket> =
			StreamCodec.composite(
				ByteBufCodecs.VAR_LONG, UpdateChunkFlatTransferNodesPacket::chunkLong,
				FlatTransferNode.STREAM_CODEC.apply(ByteBufCodecs.list()), UpdateChunkFlatTransferNodesPacket::nodes,
				::UpdateChunkFlatTransferNodesPacket
			)
	}


}