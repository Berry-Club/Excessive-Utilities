package dev.aaronhowser.mods.excessive_utilities.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.ClientGridPower
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class UpdateGridPowerPacket(
	val capacity: Int,
	val usage: Int
) : AaronPacket() {

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
		return TYPE
	}

	override fun handleOnClient(context: IPayloadContext) {
		ClientGridPower.capacity = capacity
		ClientGridPower.usage = usage
	}

	companion object {
		val TYPE: CustomPacketPayload.Type<UpdateGridPowerPacket> =
			CustomPacketPayload.Type(ExcessiveUtilities.modResource("update_grid_power"))

		val STREAM_CODEC: StreamCodec<ByteBuf, UpdateGridPowerPacket> =
			StreamCodec.composite(
				ByteBufCodecs.VAR_INT, UpdateGridPowerPacket::capacity,
				ByteBufCodecs.VAR_INT, UpdateGridPowerPacket::usage,
				::UpdateGridPowerPacket
			)
	}

}