package dev.aaronhowser.mods.excessive_utilities.actor

import dev.aaronhowser.mods.aaron.actor.LevelActor
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.furtherThan
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import java.util.*

class InversionRitualActor(
	val playerUuid: UUID,
	level: Level
) : LevelActor(level) {

	fun getPlayer(): Player? = level.getPlayerByUUID(playerUuid)

	override fun tick() {
		val player = getPlayer()

		if (player == null
			|| player.level() != level
			|| player.blockPosition().furtherThan(BlockPos.ZERO.atY(player.y.toInt()), 1024)
		) {
			remove()
			return
		}

	}

}