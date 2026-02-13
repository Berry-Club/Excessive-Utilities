package dev.aaronhowser.mods.excessive_utilities.handler.key_handler

import net.minecraft.world.entity.player.Player
import java.util.*
import kotlin.collections.set

/*
* Copied mostly from this:
* https://github.com/BlakeBr0/IronJetpacks/blob/1.21/src/main/java/com/blakebr0/ironjetpacks/client/handler/InputHandler.java
*/
object KeyHandler {

	private val HOLDING_SPACE: WeakHashMap<Player, Boolean> = WeakHashMap()

	fun isHoldingSpace(player: Player): Boolean {
		return HOLDING_SPACE.getOrDefault(player, false)
	}

	fun setIsHoldingSpace(player: Player, isHolding: Boolean) {
		HOLDING_SPACE[player] = isHolding
	}

	fun remove(player: Player) {
		HOLDING_SPACE.remove(player)
	}

}