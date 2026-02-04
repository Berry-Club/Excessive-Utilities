package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class EnderCoreBlock : Block(Properties.ofFullCopy(Blocks.OBSIDIAN)) {

	override fun getEnchantPowerBonus(state: BlockState, level: LevelReader, pos: BlockPos): Float {
		return 10f
	}

}