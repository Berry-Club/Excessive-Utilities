package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.state.BlockState

class ManualMillBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpSourceBlockEntity(ModBlockEntityTypes.MANUAL_MILL.get(), pos, blockState) {

	override fun getGp(): Int {
		val stateBelow = level?.getBlockState(worldPosition.below()) ?: return 0

		return if (stateBelow.isBlock(BlockTags.FIRE)) {
			ServerConfig.CONFIG.fireMillGeneration.get()
		} else {
			0
		}
	}

}