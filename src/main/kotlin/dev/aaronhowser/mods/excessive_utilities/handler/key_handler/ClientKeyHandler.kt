package dev.aaronhowser.mods.excessive_utilities.handler.key_handler

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.excessive_utilities.packet.client_to_server.UpdateControlsPacket
import net.minecraft.client.player.LocalPlayer

object ClientKeyHandler {

	private var wasSpaceHeld: Boolean = false

	fun updateControls() {
		val player = AaronClientUtil.localPlayer as? LocalPlayer ?: return
		val isSpaceHeld = player.input.jumping

		if (isSpaceHeld != wasSpaceHeld) {
			wasSpaceHeld = isSpaceHeld

			KeyHandler.setIsHoldingSpace(player, isSpaceHeld)

			val packet = UpdateControlsPacket(isHoldingSpace = isSpaceHeld)
			packet.messageServer()
		}
	}

}