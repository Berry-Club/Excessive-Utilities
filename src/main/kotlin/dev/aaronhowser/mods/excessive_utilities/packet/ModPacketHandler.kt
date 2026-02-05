package dev.aaronhowser.mods.excessive_utilities.packet

import dev.aaronhowser.mods.aaron.packet.AaronPacketRegistrar
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

object ModPacketHandler : AaronPacketRegistrar {

	fun registerPayloads(event: RegisterPayloadHandlersEvent) {
		val registrar = event.registrar("1")
	}

}