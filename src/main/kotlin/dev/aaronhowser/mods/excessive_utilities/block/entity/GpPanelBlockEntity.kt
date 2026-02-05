package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.block.state.BlockState

class GpPanelBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpSourceBlockEntity(ModBlockEntityTypes.GP_PANEL.get(), pos, blockState) {

	var requiresDay: Boolean = true

	override fun getGpGeneration(): Double {
		val level = level ?: return 0.0

		val amount = if (requiresDay) {
			ServerConfig.CONFIG.solarPanelGeneration.get()
		} else {
			ServerConfig.CONFIG.lunarPanelGeneration.get()
		}

		val canSeeSky = level.canSeeSky(worldPosition.above())
		val isDayTime = level.isDay

		return if (canSeeSky && requiresDay == isDayTime) amount else 0.0
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		tag.putBoolean(REQUIRES_DAY_NBT, requiresDay)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)
		requiresDay = tag.getBoolean(REQUIRES_DAY_NBT)
	}

	companion object {
		const val REQUIRES_DAY_NBT = "RequiresDay"
	}

}