package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class DragonEggMillBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpSourceBlockEntity(ModBlockEntityTypes.DRAGON_EGG_MILL.get(), pos, blockState) {

	override fun getGpGeneration(): Double {
		val stateAbove = level?.getBlockState(worldPosition.above()) ?: return 0.0

		return if (stateAbove.isBlock(ModBlockTagsProvider.VALID_FOR_DRAGON_EGG_MILL)) {
			ServerConfig.CONFIG.dragonEggMillGeneration.get()
		} else {
			0.0
		}
	}

}