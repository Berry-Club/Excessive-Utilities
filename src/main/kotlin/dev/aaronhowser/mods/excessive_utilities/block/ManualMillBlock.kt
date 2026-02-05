package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.base.GpSourceBlock
import dev.aaronhowser.mods.excessive_utilities.block.entity.ManualMillBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class ManualMillBlock : GpSourceBlock(Properties.ofFullCopy(Blocks.STONE)) {

	override fun getBlockEntityType(): BlockEntityType<out GpSourceBlockEntity> {
		return ModBlockEntityTypes.MANUAL_MILL.get()
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return ManualMillBlockEntity(pos, state)
	}

}