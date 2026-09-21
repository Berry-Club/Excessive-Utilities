package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.handler.key_handler.KeyHandler
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack

class ChickenWingRingGridPowerContribution(
	ringStack: ItemStack,
	player: ServerPlayer
) : HeldRingGridPowerContribution(ringStack, player) {

	override fun getAmount(): Double {
		if (player.hasInfiniteMaterials()) return 0.0
		if (!KeyHandler.isHoldingSpace(player)) return 0.0
		if (player.onGround() || player.isPassenger) return 0.0
		if (player.deltaMovement.y >= 0) return 0.0

		return ServerConfig.CONFIG.chickenWingRingGpCost.get()
	}

}