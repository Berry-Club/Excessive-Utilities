package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.item.component.MagicalSnowGlobeProgressComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class MagicalSnowGlobeBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.MAGICAL_SNOW_GLOBE.get(), pos, blockState) {

	var progressComponent = MagicalSnowGlobeProgressComponent(emptyMap())

}