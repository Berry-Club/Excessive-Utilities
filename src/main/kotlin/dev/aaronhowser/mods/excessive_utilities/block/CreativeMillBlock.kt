package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.base.GpSourceBlock
import dev.aaronhowser.mods.excessive_utilities.block.entity.CreativeMillBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.DragonEggMillBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

class CreativeMillBlock : GpSourceBlock(Properties.ofFullCopy(Blocks.BEDROCK)) {

	override fun getBlockEntityType(): BlockEntityType<out GpSourceBlockEntity> {
		return ModBlockEntityTypes.CREATIVE_MILL.get()
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return CreativeMillBlockEntity(pos, state)
	}

}