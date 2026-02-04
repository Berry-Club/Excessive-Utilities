package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.ChandelierCarrier
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.MobCategory
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent

class ChandelierBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.CHANDELIER.get(), pos, blockState) {

	override fun onLoad() {
		super.onLoad()
		val level = this.level
		if (level is ChandelierCarrier) {
			level.getChandelierPositions().add(this.worldPosition.asLong())
		}
	}

	override fun setRemoved() {
		super.setRemoved()
		val level = this.level
		if (level is ChandelierCarrier) {
			level.getChandelierPositions().remove(this.worldPosition.asLong())
		}
	}

	override fun clearRemoved() {
		super.clearRemoved()
		val level = this.level
		if (level is ChandelierCarrier) {
			level.getChandelierPositions().add(this.worldPosition.asLong())
		}
	}


	companion object {
		fun ChandelierCarrier.getChandelierPositions(): LongOpenHashSet = this.`eu$getChandelierBlockPositions`()

		fun handleSpawnEvent(event: MobSpawnEvent.SpawnPlacementCheck) {
			if (event.result != MobSpawnEvent.SpawnPlacementCheck.Result.DEFAULT) return

			val level = event.level.level

			if (event.entityType.category == MobCategory.MONSTER) {
				val pos = event.pos
				if (shouldPreventSpawn(level, pos)) {
					event.result = MobSpawnEvent.SpawnPlacementCheck.Result.FAIL
				}
			}
		}

		fun shouldPreventSpawn(level: ServerLevel, pos: BlockPos): Boolean {
			if (level !is ChandelierCarrier) return false

			val radius = ServerConfig.CONFIG.chandelierRadius.get()
			val positions = level.getChandelierPositions()

			return positions
				.asSequence()
				.map(BlockPos::of)
				.any { it.closerThan(pos, radius.toDouble()) }
		}
	}

}