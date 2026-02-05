package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class CreativeMillBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpSourceBlockEntity(ModBlockEntityTypes.CREATIVE_MILL.get(), pos, blockState) {

	override fun getGp(): Int = ServerConfig.CONFIG.creativeMillGeneration.get()

}