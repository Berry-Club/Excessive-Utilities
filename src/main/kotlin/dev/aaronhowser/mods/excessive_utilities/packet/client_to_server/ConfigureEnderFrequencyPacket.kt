package dev.aaronhowser.mods.excessive_utilities.packet.client_to_server

import dev.aaronhowser.mods.aaron.packet.AaronPacket
import dev.aaronhowser.mods.aaron.serialization.AaronExtraStreamCodecs
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.EnderFrequencyItem
import dev.aaronhowser.mods.excessive_utilities.item.component.EnderFrequencyComponent
import dev.aaronhowser.mods.excessive_utilities.menu.ender_frequency.EnderFrequencyMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.world.InteractionHand
import net.neoforged.neoforge.network.handling.IPayloadContext

class ConfigureEnderFrequencyPacket(
	val hand: InteractionHand,
	val name: String,
	val isPrivate: Boolean
) : AaronPacket() {

	override fun handleOnServer(context: IPayloadContext) {
		val player = context.player()
		val menu = player.containerMenu
		if (menu !is EnderFrequencyMenu || menu.hand != hand) return

		val stack = player.getItemInHand(hand)
		if (stack.item !is EnderFrequencyItem) return

		val frequency = EnderFrequencyComponent.create(name, isPrivate, player.uuid) ?: return
		stack.set(ModDataComponents.ENDER_FREQUENCY, frequency)
	}

	override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> = TYPE

	companion object {
		val TYPE: CustomPacketPayload.Type<ConfigureEnderFrequencyPacket> =
			makeType(ExcessiveUtilities.MOD_ID, "configure_ender_frequency")

		val STREAM_CODEC: StreamCodec<ByteBuf, ConfigureEnderFrequencyPacket> =
			StreamCodec.composite(
				AaronExtraStreamCodecs.enumStreamCodec(InteractionHand::class.java), ConfigureEnderFrequencyPacket::hand,
				ByteBufCodecs.stringUtf8(EnderFrequencyComponent.MAX_NAME_LENGTH), ConfigureEnderFrequencyPacket::name,
				ByteBufCodecs.BOOL, ConfigureEnderFrequencyPacket::isPrivate,
				::ConfigureEnderFrequencyPacket
			)
	}
}