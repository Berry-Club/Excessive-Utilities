package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class MagnumTorchBlock : Block(Properties.ofFullCopy(Blocks.OAK_LOG)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
		TODO("Not yet implemented")
	}

}