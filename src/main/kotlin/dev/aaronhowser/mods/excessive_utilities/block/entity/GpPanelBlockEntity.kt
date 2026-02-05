package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerContribution
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class GpPanelBlockEntity(
	pos: BlockPos,
	blockState: BlockState,
	val isDay: Boolean
) : BlockEntity(if (isDay) null else null, pos, blockState) {

	private val gpGeneration: GridPowerContribution =
		object : GridPowerContribution {
			override fun getAmount(): Int {
				val level = level ?: return 0

				val amount = if (isDay) {
					ServerConfig.CONFIG.solarPanelGeneration.get()
				} else {
					ServerConfig.CONFIG.lunarPanelGeneration.get()
				}

				val canSeeSky = level.canSeeSky(worldPosition.above())
				val isDayTime = level.isDay

				return if (canSeeSky && isDay == isDayTime) amount else 0
			}

			override fun isStillValid(): Boolean = !this@GpPanelBlockEntity.isRemoved
		}

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