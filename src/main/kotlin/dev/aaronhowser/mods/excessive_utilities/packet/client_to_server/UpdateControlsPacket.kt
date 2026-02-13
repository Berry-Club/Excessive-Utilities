package dev.aaronhowser.mods.excessive_utilities.packet.client_to_server

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.handler.key_handler.KeyHandler
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class UpdateControlsPacket(
	val isHoldingSpace: Boolean
) : AaronPacket() {

	override fun handleOnServer(context: IPayloadContext) {
		KeyHandler.setIsHoldingSpace(context.player(), isHoldingSpace)
	}

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
		return TYPE
	}

	companion object {
		val TYPE: CustomPacketPayload.Type<UpdateControlsPacket> =
			CustomPacketPayload.Type(ExcessiveUtilities.modResource("update_controls_packet"))

		val STREAM_CODEC: StreamCodec<ByteBuf, UpdateControlsPacket> =
			ByteBufCodecs.BOOL.map(
				::UpdateControlsPacket,
				UpdateControlsPacket::isHoldingSpace
			)
	}

}