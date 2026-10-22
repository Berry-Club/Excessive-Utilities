package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour.Properties
import net.minecraft.world.level.block.state.BlockState

class EnderCoreBlock(properties: Properties) : Block(properties) {

	override fun getEnchantPowerBonus(state: BlockState, level: BlockGetter, pos: BlockPos): Float {
		return 10f
	}

}










