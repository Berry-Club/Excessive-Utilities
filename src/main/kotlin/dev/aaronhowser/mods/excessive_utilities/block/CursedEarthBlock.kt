package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.util.random.WeightedRandomList
import net.minecraft.world.Difficulty
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.entity.MobSpawnType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
import kotlin.jvm.optionals.getOrNull

class CursedEarthBlock : Block(Properties.ofFullCopy(Blocks.GRASS_BLOCK)) {

	override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
		if (level is ServerLevel) {
			level.scheduleTick(pos, this, ServerConfig.CONFIG.cursedEarthPeriod.get())
		}
	}

	override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
		level.scheduleTick(pos, this, ServerConfig.CONFIG.cursedEarthPeriod.get())

		handleFire(level, pos, random)
		spawnMonster(level, pos, random)
	}

	companion object {
		private fun handleFire(level: ServerLevel, pos: BlockPos, random: RandomSource) {
			var fireNearby = false
			val fireCheckArea = BlockPos.betweenClosed(
				pos.offset(-3, 0, -3),
				pos.offset(3, 2, 3)
			)

			for (firePos in fireCheckArea) {
				if (!level.isLoaded(firePos)) continue

				val stateThere = level.getBlockState(firePos)
				if (stateThere.isBlock(Blocks.FIRE)) {
					fireNearby = true
					break
				}
			}

			if (!fireNearby) return

			if (random.chance(0.1)) {
				level.setBlockAndUpdate(pos, Blocks.DIRT.defaultBlockState())
			}

			val randomNearby = BlockPos.randomInCube(random, 10, pos, 5)
			for (nearbyPos in randomNearby) {
				if (!level.isLoaded(nearbyPos)) continue

				val stateThere = level.getBlockState(nearbyPos)
				if (!stateThere.`is`(ModBlocks.CURSED_EARTH)) continue

				val posAboveThere = nearbyPos.above()
				val stateAboveThere = level.getBlockState(posAboveThere)
				if (stateAboveThere.canBeReplaced()) {
					level.setBlockAndUpdate(posAboveThere, Blocks.FIRE.defaultBlockState())
				}
			}
		}

		private fun spawnMonster(level: ServerLevel, cursedEarthPos: BlockPos, random: RandomSource) {
			if (level.difficulty == Difficulty.PEACEFUL) return
			if (!random.chance(ServerConfig.CONFIG.cursedEarthChance.get())) return

			val pos = cursedEarthPos.above()
			val lightLevel = level.getMaxLocalRawBrightness(pos.above())
			if (lightLevel > 5) return

			val nearbyEntities = level.getEntitiesOfClass(
				LivingEntity::class.java,
				AABB(pos).inflate(ServerConfig.CONFIG.cursedEarthCheckRadius.get())
			)
			if (nearbyEntities.count() > ServerConfig.CONFIG.cursedEarthMaxSpawnedMobs.get()) return

			val possibleMobs = level
				.getBiome(pos)
				.value()
				.mobSettings
				.getMobs(MobCategory.MONSTER)
				.unwrap()
				.filterNot { it.type.isEntity(ModEntityTypeTagsProvider.CURSED_EARTH_BLACKLIST) }

			if (possibleMobs.isEmpty()) return

			val newWeightedList = WeightedRandomList.create(possibleMobs)
			val randomType = newWeightedList
				.getRandom(random)
				.getOrNull()
				?.type
				?: return

			randomType.spawn(level, pos, MobSpawnType.SPAWNER)
		}
	}

}