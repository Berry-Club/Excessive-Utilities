package dev.aaronhowser.mods.excessive_utilities.actor

import dev.aaronhowser.mods.aaron.actor.LevelActor
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.random
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMessageLang
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.excessive_utilities.handler.CursedEarthHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.entity.NeutralMob
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.event.EventHooks
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
	private var tick = 0

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

		if (tick % 20 != 0) return

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

		spawnMonster(player)
	}

	private fun cancel() {
		remove()

		val entitiesToRemove = level
			.getEntitiesOfClass(Mob::class.java, area)
			.filter(CursedEarthHandler::isCursed)

		for (entity in entitiesToRemove) {
			entity.discard()
		}
	}

	private fun spawnMonster(player: Player) {
		val serverLevel = level as? ServerLevel ?: return
		val monsterType = MONSTER_TYPES.random(player.random)
		val mob = monsterType.create(serverLevel) ?: return

		val spawnPos = findMobSpawnLocation(
			player,
			isFlyingMob = monsterType == EntityType.PHANTOM
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
		CursedEarthHandler.setCursed(mob, value = true)

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

	companion object {
		private val MONSTER_TYPES: List<EntityType<out Mob>> = listOf(
			EntityType.ZOMBIE,
			EntityType.SKELETON,
			EntityType.CREEPER,
			EntityType.ENDERMAN,
			EntityType.PHANTOM
		)

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
