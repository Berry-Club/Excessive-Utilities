package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.AaronExtensions.isServerSide
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class GpPanelBlockEntity(
	pos: BlockPos,
	blockState: BlockState,
	val isDay: Boolean
) : BlockEntity(if (isDay) null else null, pos, blockState) {

	fun tick() {

	}

	companion object {
		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: GpPanelBlockEntity
		) {
			if (level.isServerSide) {
				blockEntity.tick()
			}
		}
	}

}