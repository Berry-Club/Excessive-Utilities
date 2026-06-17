package dev.aaronhowser.mods.excessive_utilities.actor

import dev.aaronhowser.mods.aaron.actor.LevelActor
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.status
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMessageLang
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.excessive_utilities.datamap.InversionRitualEnemyWeight
import dev.aaronhowser.mods.excessive_utilities.handler.CurseHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.*
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.event.EventHooks
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent
import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class InversionRitualActor(
	val playerUuid: UUID,
	private val center: BlockPos,
	level: Level
) : LevelActor(level) {

	constructor(player: Player, center: BlockPos) : this(player.uuid, center, player.level())

	private val area: AABB = AABB(center).inflate(1024.0)
	private val period: Int = ServerConfig.CONFIG.inversionRitualPeriod.get()
	private val spawnsPer: Int get() = ServerConfig.CONFIG.inversionRitualSpawnsPer.get()
	private val requiredKills: Int get() = ServerConfig.CONFIG.inversionRitualKillsRequired.get()

	private var tick = 0
	private var monstersKilled = 0

	fun getPlayer(): Player? {
		val level = this.level

		return if (level is ServerLevel) {
			level.server.playerList.getPlayer(playerUuid)
		} else {
			level.getPlayerByUUID(playerUuid)
		}
	}

	override fun tick() {
		if (tick == 0) firstTick()
		tick++

		if (tick % period != 0) return

		val player = getPlayer()

		if (player == null) {
			cancel()
			return
		}

		if (player.level() != level
			|| player.isDeadOrDying
			|| !area.contains(player.position())
		) {
			player.tell(ModMessageLang.INVERSION_RITUAL_TOO_FAR.toComponent())
			cancel()
			return
		}

		for (i in 0 until spawnsPer) {
			spawnMonster(player)
		}
	}

	private fun cancel() {
		markForRemoval()

		val entitiesToRemove = level
			.getEntitiesOfClass(Mob::class.java, area)
			.filter(CurseHandler::isCursed)

		for (entity in entitiesToRemove) {
			entity.discard()
		}
	}

	private fun spawnMonster(player: Player) {
		val serverLevel = level as? ServerLevel ?: return
		val monsterType = InversionRitualEnemyWeight.getRandomType(player.random) ?: return
		val mob = monsterType.create(serverLevel) ?: return

		val spawnPos = findMobSpawnLocation(
			player,
			isFlyingMob = mob is FlyingMob
		) ?: return

		mob.moveTo(
			spawnPos.x + 0.5,
			spawnPos.y.toDouble(),
			spawnPos.z + 0.5,
			player.random.nextFloat() * 360f,
			0f
		)

		if (!mob.checkSpawnObstruction(serverLevel)) return

		EventHooks.finalizeMobSpawn(
			mob,
			serverLevel,
			serverLevel.getCurrentDifficultyAt(spawnPos),
			MobSpawnType.EVENT,
			null
		)

		mob.target = player
		CurseHandler.setCursed(mob, value = true)

		if (mob is NeutralMob) {
			mob.persistentAngerTarget = player.uuid
			mob.startPersistentAngerTimer()
		}

		serverLevel.addFreshEntity(mob)
	}

	private fun findMobSpawnLocation(
		player: Player,
		isFlyingMob: Boolean,
		runningTries: Int = 0
	): BlockPos? {
		val random = player.random

		if (runningTries > 50) return null

		val spawnArea = AABB(player.blockPosition()).inflate(48.0).intersect(area)
		val x = random.nextRange(spawnArea.minX, spawnArea.maxX).roundToInt()
		val z = random.nextRange(spawnArea.minZ, spawnArea.maxZ).roundToInt()

		if (isFlyingMob) {
			val minY = max(level.minBuildHeight + 1, player.blockY + 12)
			val maxY = min(level.maxBuildHeight - 2, player.blockY + 32)
			if (minY > maxY) return null

			val pos = BlockPos(x, random.nextRange(minY, maxY), z)
			return if (canSpawnFlyingMobAt(pos)) {
				pos
			} else {
				findMobSpawnLocation(player, isFlyingMob = true, runningTries + 1)
			}
		}

		val heightmapPos = level.getHeightmapPos(
			Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
			BlockPos(x, 0, z)
		)

		val mutablePos = heightmapPos.mutable()

		while (mutablePos.y > level.minBuildHeight && canSpawnGroundMobAt(mutablePos)) {
			mutablePos.move(Direction.DOWN)
		}

		mutablePos.move(Direction.UP)

		return if (canSpawnGroundMobAt(mutablePos)) {
			mutablePos
		} else {
			findMobSpawnLocation(player, isFlyingMob = false, runningTries + 1)
		}
	}

	private fun canSpawnGroundMobAt(pos: BlockPos): Boolean {
		if (!area.contains(pos.center)) return false
		if (!level.isLoaded(pos)) return false

		if (!level.isEmptyBlock(pos)) return false
		if (!level.isEmptyBlock(pos.above())) return false
		if (!level.isEmptyBlock(pos.above(2))) return false

		val below = pos.below()
		return level.getBlockState(below).isFaceSturdy(level, below, Direction.UP)
	}

	private fun canSpawnFlyingMobAt(pos: BlockPos): Boolean {
		if (!area.contains(pos.center)) return false
		if (!level.isLoaded(pos)) return false
		return level.isEmptyBlock(pos) && level.isEmptyBlock(pos.above())
	}

	private fun firstTick() {
		val entitiesToRemove = level
			.getEntitiesOfClass(Mob::class.java, area)
			.filter { it.isEntity(ModEntityTypeTagsProvider.INVERSION_RITUAL_DESPAWN_ON_START) }

		for (entity in entitiesToRemove) {
			entity.discard()
		}

		ExcessiveUtilities.LOGGER.info("InversionRitualActor discarded ${entitiesToRemove.size} entities")
	}

	private fun handleDeath(
		victim: LivingEntity,
		killer: Player
	) {
		if (killer != getPlayer()) return

		monstersKilled++
		killer.status("$monstersKilled/$requiredKills")
	}

	companion object {
		fun isRitualActive(
			level: Level,
			pos: BlockPos
		): Boolean {
			val inversionRituals = level
				.getLevelActors()
				.filterIsInstance<InversionRitualActor>()

			val center = pos.center
			return inversionRituals.any { it.area.contains(center) }
		}

		fun handleDeath(event: LivingDeathEvent) {
			if (event.isCanceled) return

			val victim = event.entity
			val killer = event.source.entity as? Player ?: return

			if (!CurseHandler.isCursed(victim)) return

			val inversionRituals = victim
				.level()
				.getLevelActors()
				.filterIsInstance<InversionRitualActor>()

			for (ritual in inversionRituals) {
				ritual.handleDeath(victim, killer)
			}
		}
	}

}
