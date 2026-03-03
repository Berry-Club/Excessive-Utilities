package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModEntityTypeTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
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
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.IntegerProperty
import net.minecraft.world.phys.AABB
import kotlin.jvm.optionals.getOrNull

class CursedEarthBlock : Block(Properties.ofFullCopy(Blocks.GRASS_BLOCK)) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(DISTANCE, 7)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(DISTANCE)
	}

	override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
		if (level is ServerLevel) {
			level.scheduleTick(pos, this, 1)
		}

		val distance = state.getValue(DISTANCE)
		if (distance <= 1) return

		for (dx in -1..1) for (dy in -1..1) for (dz in -1..1) {
			if (!level.random.chance(0.5)) continue

			val neighborPos = pos.offset(dx, dy, dz)
			if (!level.isEmptyBlock(neighborPos.above())) continue

			val neighborState = level.getBlockState(neighborPos)

			if (neighborState.isBlock(this)) {
				val neighborDistance = neighborState.getValue(DISTANCE)
				if (neighborDistance >= distance) continue
			} else if (!neighborState.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)) {
				continue
			}

			val newState = defaultBlockState().setValue(DISTANCE, distance - 1)
			level.setBlockAndUpdate(neighborPos, newState)
		}
	}

	override fun tick(state: BlockState, level: ServerLevel, pos: BlockPos, random: RandomSource) {
		level.scheduleTick(pos, this, 1)

		handleFire(level, pos, random)
		spawnMonster(level, pos, random)
	}

	companion object {
		val DISTANCE: IntegerProperty = BlockStateProperties.DISTANCE

		private fun handleFire(level: ServerLevel, pos: BlockPos, random: RandomSource) {
			val stateAbove = level.getBlockState(pos.above())
			if (!stateAbove.isBlock(BlockTags.FIRE)) return

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
					level.setBlockAndUpdate(posAboveThere, stateAbove)
				}
			}
		}

		private fun spawnMonster(level: ServerLevel, cursedEarthPos: BlockPos, random: RandomSource) {
			if (level.difficulty == Difficulty.PEACEFUL) return
			if (!random.chance(ServerConfig.CONFIG.cursedEarthChance.get())) return

			val pos = cursedEarthPos.above()

			val nearbyEntities = level.getEntitiesOfClass(
				LivingEntity::class.java,
				AABB(pos).inflate(8.0)
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