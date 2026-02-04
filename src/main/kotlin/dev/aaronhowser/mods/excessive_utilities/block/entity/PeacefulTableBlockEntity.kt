package dev.aaronhowser.mods.excessive_utilities.block.entity

import com.mojang.datafixers.util.Either
import dev.aaronhowser.mods.aaron.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Difficulty
import net.minecraft.world.DifficultyInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.neoforged.neoforge.common.CommonHooks
import net.neoforged.neoforge.common.extensions.IOwnedSpawner
import net.neoforged.neoforge.event.EventHooks
import kotlin.jvm.optionals.getOrNull

class PeacefulTableBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.PEACEFUL_TABLE.get(), pos, blockState), IOwnedSpawner {

	private fun tick() {
		val level = this.level as? ServerLevel ?: return
		val pos = this.blockPos

		val possibleMobTypes = level.getBiome(pos).value().mobSettings.getMobs(MobCategory.MONSTER)

		val entityType = possibleMobTypes
			.getRandom(level.random)
			.getOrNull()
			?.type
			?: return

		val mob = entityType.create(level) as? Mob ?: return
		mob.moveTo(blockPos.bottomCenter)

		EventHooks.finalizeMobSpawnSpawner(
			mob,
			level,
			DifficultyInstance(
				Difficulty.HARD,
				level.dayTime,
				level.getChunkAt(pos).inhabitedTime,
				level.moonBrightness
			),
			MobSpawnType.SPAWNER,
			null,
			this,
			true
		)

		val damageSource = level.damageSources().genericKill()

		mob.captureDrops(mutableListOf())

		val lootTable = level.server.reloadableRegistries().getLootTable(mob.lootTable)
		val lootParamsBuilder = LootParams.Builder(level)
			.withParameter(LootContextParams.THIS_ENTITY, mob)
			.withParameter(LootContextParams.ORIGIN, mob.position())
			.withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
			.withOptionalParameter(LootContextParams.ATTACKING_ENTITY, damageSource.entity)
			.withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.directEntity)

		val lootParams = lootParamsBuilder.create(LootContextParamSets.ENTITY)
		lootTable.getRandomItems(lootParams, mob.lootTableSeed, mob::spawnAtLocation)

		val drops = mob.captureDrops(null) ?: return
		if (!CommonHooks.onLivingDrops(mob, damageSource, drops, true)) {
			for (drop in drops) {
				level.addFreshEntity(drop)
			}
		}

	}

	override fun getOwner(): Either<BlockEntity, Entity> {
		return Either.left(this)
	}

	companion object {
		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: PeacefulTableBlockEntity
		) {
			if (level.isServerSide) {
				blockEntity.tick()
			}
		}
	}

}