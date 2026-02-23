package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.base.EnderQuarryUpgrade
import dev.aaronhowser.mods.excessive_utilities.block.entity.EnderQuarryUpgradeBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class EnderQuarryUpgradeBlock(
	val type: EnderQuarryUpgrade
) : Block(Properties.ofFullCopy(Blocks.OBSIDIAN)), EntityBlock {

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		val blockEntity = EnderQuarryUpgradeBlockEntity(pos, state)
		blockEntity.upgrade = type
		return blockEntity
	}

}