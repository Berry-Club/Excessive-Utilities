package dev.aaronhowser.mods.excessive_utilities.actor

import dev.aaronhowser.mods.aaron.actor.LevelActor
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.furtherThan
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMessageLang
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import java.util.*

class InversionRitualActor(
	val playerUuid: UUID,
	private val center: BlockPos,
	level: Level
) : LevelActor(level) {

	constructor(player: Player, center: BlockPos) : this(player.uuid, center, player.level())

	fun getPlayer(): Player? = level.getPlayerByUUID(playerUuid)

	private var tick = 0

	override fun tick() {
		tick++
		if (tick % 20 != 0) return

		val player = getPlayer()

		if (player == null) {
			remove()
			return
		}

		if (player.level() != level) {
			player.tell(ModMessageLang.INVERSION_RITUAL_LEFT_END.toComponent())
			remove()
			return
		}

		if (player.blockPosition().furtherThan(center, 1024)) {
			player.tell(ModMessageLang.INVERSION_RITUAL_TOO_FAR.toComponent())
			remove()
			return
		}

		if (tick / 20 == 10) {
			player.tell("It's been 10 seconds!")
			remove()
			return
		}
	}

}