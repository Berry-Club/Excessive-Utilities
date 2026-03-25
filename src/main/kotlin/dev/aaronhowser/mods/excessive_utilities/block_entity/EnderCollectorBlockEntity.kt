package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class EnderCollectorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.ENDER_COLLECTOR.get(), pos, blockState) {

	var radius: Double = 0.5
		private set(value) {
			field = value
			setChanged()
		}

	fun cycleRadius(reverse: Boolean) {
		if (reverse) {
			val new = radius - 0.5
			radius = if (new < MIN_RADIUS) MAX_RADIUS else new
		} else {
			val new = radius + 0.5
			radius = if (new > MAX_RADIUS) MIN_RADIUS else new
		}
	}

	private fun serverTick(level: ServerLevel) {

	}

	companion object {
		const val MIN_RADIUS = 0.5
		const val MAX_RADIUS = 4.0

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: EnderCollectorBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			}
		}
	}

}