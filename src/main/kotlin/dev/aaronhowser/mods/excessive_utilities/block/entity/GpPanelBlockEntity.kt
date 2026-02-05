package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerContribution
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.state.BlockState

class GpPanelBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpSourceBlockEntity(ModBlockEntityTypes.GP_PANEL.get(), pos, blockState) {

	var isDay: Boolean = true

	override val gpGeneration: GridPowerContribution =
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

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		tag.putBoolean(IS_DAY_NBT, isDay)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)
		isDay = tag.getBoolean(IS_DAY_NBT)
	}

	companion object {
		const val IS_DAY_NBT = "IsDay"
	}

}