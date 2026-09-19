package dev.aaronhowser.mods.excessive_utilities.packet.server_to_client

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.neoforged.neoforge.network.handling.IPayloadContext

class TransferNodeParticlesPacket(
	val position: BlockPos
) : AaronPacket() {

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
		return TYPE
	}

	override fun handleOnClient(context: IPayloadContext) {
		val level = Minecraft.getInstance().level ?: return
		val particleCount = ClientConfig.CONFIG.transferNodeParticleCount.get()

		for (particleIndex in 0 until particleCount) {
			val x = position.x + level.random.nextDouble()
			val y = position.y + level.random.nextDouble()
			val z = position.z + level.random.nextDouble()

			level.addParticle(
				DustParticleOptions.REDSTONE,
				x, y, z,
				0.0, 0.0, 0.0
			)
		}
	}

	companion object {
		val TYPE: CustomPacketPayload.Type<TransferNodeParticlesPacket> =
			makeType(ExcessiveUtilities.MOD_ID, "transfer_node_particles")

		val STREAM_CODEC: StreamCodec<ByteBuf, TransferNodeParticlesPacket> =
			BlockPos.STREAM_CODEC.map(::TransferNodeParticlesPacket, TransferNodeParticlesPacket::position)
	}

}