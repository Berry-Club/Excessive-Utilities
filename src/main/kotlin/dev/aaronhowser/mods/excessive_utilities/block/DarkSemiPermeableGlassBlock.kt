package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState

class DarkSemiPermeableGlassBlock(
	isSolidForMobsOnly: Boolean,
	properties: Properties = Properties.ofFullCopy(Blocks.GLASS)
) : SemiPermeableGlassBlock(isSolidForMobsOnly, properties) {

	override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
		return false
	}

	override fun getLightBlock(state: BlockState, level: BlockGetter, pos: BlockPos): Int {
		return level.maxLightLevel
	}

}