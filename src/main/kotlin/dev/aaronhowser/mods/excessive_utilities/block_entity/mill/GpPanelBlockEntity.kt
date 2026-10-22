package dev.aaronhowser.mods.excessive_utilities.block_entity.mill

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
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
		val isDay = level.gameTime % 24000L < 12000L
		val isProducing = canSeeSky && requiresDay == isDay

		if (!isProducing) return 0.0
		return if (level.isRaining) amount * 0.95 else amount
	}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)
		tag.putBoolean(REQUIRES_DAY_NBT, requiresDay)
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)
		requiresDay = tag.getBooleanOr(REQUIRES_DAY_NBT, false)
	}

	companion object {
		const val REQUIRES_DAY_NBT = "RequiresDay"
	}

}
