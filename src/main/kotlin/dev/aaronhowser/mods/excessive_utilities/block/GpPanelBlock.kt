package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.base.GpSourceBlock
import dev.aaronhowser.mods.excessive_utilities.block.entity.GpPanelBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GpSourceBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class GpPanelBlock(
	val isDay: Boolean,
	beType: BlockEntityType<out GpSourceBlockEntity>
) : GpSourceBlock(beType, Properties.ofFullCopy(Blocks.DAYLIGHT_DETECTOR)) {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return GpPanelBlockEntity(pos, state, isDay)
	}

}