package dev.aaronhowser.mods.excessive_utilities.actor

import dev.aaronhowser.mods.aaron.actor.LevelActor
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMessageLang
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModEntityTypeTagsProvider
import net.minecraft.core.BlockPos
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import java.util.*

class InversionRitualActor(
	val playerUuid: UUID,
	private val center: BlockPos,
	level: Level
) : LevelActor(level) {

	constructor(player: Player, center: BlockPos) : this(player.uuid, center, player.level())

	private val area: AABB = AABB(center).inflate(1024.0)
	private var tick = 0

	fun getPlayer(): Player? = level.getPlayerByUUID(playerUuid)

	override fun tick() {
		if (tick % 20 != 0) return
		if (tick == 0) firstTick()
		tick++

		val player = getPlayer()

		if (player == null) {
			remove()
			return
		}

		if (player.level() != level || player.isDeadOrDying) {
			player.tell(ModMessageLang.INVERSION_RITUAL_LEFT_END.toComponent())
			remove()
			return
		}

		if (area.contains(player.position())) {
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

	private fun firstTick() {
		val entitiesToRemove = level
			.getEntitiesOfClass(Mob::class.java, area)
			.filter { it.isEntity(ModEntityTypeTagsProvider.INVERSION_RITUAL_DESPAWN_ON_START) }

	}

	companion object {
		fun isRitualActive(
			level: Level,
			pos: BlockPos
		): Boolean {
			val inversionRituals = level.getLevelActors().filterIsInstance<InversionRitualActor>()
			val center = pos.center
			return inversionRituals.any { it.area.contains(center) }
		}
	}

}