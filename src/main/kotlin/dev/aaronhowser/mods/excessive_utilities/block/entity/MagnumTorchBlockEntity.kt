package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.MagnumTorchCarrier
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import it.unimi.dsi.fastutil.longs.LongOpenHashSet
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.monster.Enemy
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent

class MagnumTorchBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.MAGNUM_TORCH.get(), pos, blockState) {

	override fun onLoad() {
		super.onLoad()
		val level = this.level
		if (level is MagnumTorchCarrier) {
			level.getMagnumTorchPositions().add(this.worldPosition.asLong())
		}
	}

	override fun setRemoved() {
		super.setRemoved()
		val level = this.level
		if (level is MagnumTorchCarrier) {
			level.getMagnumTorchPositions().remove(this.worldPosition.asLong())
		}
	}

	override fun clearRemoved() {
		super.clearRemoved()
		val level = this.level
		if (level is MagnumTorchCarrier) {
			level.getMagnumTorchPositions().add(this.worldPosition.asLong())
		}
	}


	companion object {
		fun MagnumTorchCarrier.getMagnumTorchPositions(): LongOpenHashSet = this.`eu$getMagnumTorchBlockPositions`()

		fun handleSpawnEvent(event: MobSpawnEvent.SpawnPlacementCheck) {
			val level = event.level.level
			val entity = event.entityType.create(level)

			if (entity is Enemy) {
				val pos = event.pos
				if (shouldPreventSpawn(level, pos)) {
					event.result = MobSpawnEvent.SpawnPlacementCheck.Result.FAIL
				}
			}
		}

		fun shouldPreventSpawn(level: ServerLevel, pos: BlockPos): Boolean {
			if (level !is MagnumTorchCarrier) return false

			val radius = 64
			val positions = level.getMagnumTorchPositions()

			return positions
				.asSequence()
				.map(BlockPos::of)
				.any { it.closerThan(pos, radius.toDouble()) }
		}
	}

}