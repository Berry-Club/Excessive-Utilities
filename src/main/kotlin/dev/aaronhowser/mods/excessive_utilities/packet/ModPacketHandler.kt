package dev.aaronhowser.mods.excessive_utilities.packet

import dev.aaronhowser.mods.aaron.packet.AaronPacketRegistrar
import dev.aaronhowser.mods.excessive_utilities.packet.server_to_client.UpdateChunkFlatTransferNodesPacket
import dev.aaronhowser.mods.excessive_utilities.packet.server_to_client.UpdateGridPowerPacket
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

object ModPacketHandler : AaronPacketRegistrar {

	fun registerPayloads(event: RegisterPayloadHandlersEvent) {
		val registrar = event.registrar("1")

		toClient(
			registrar,
			UpdateGridPowerPacket.TYPE,
			UpdateGridPowerPacket.STREAM_CODEC
		)

		toClient(
			registrar,
			UpdateChunkFlatTransferNodesPacket.TYPE,
			UpdateChunkFlatTransferNodesPacket.STREAM_CODEC
		)

	}

}