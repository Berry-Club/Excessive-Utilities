package dev.aaronhowser.mods.excessive_utilities.block.base

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.ContainerContainer
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState

abstract class SimpleContainerBlock(properties: Properties) : Block(properties) {

	override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, movedByPiston: Boolean) {
		if (!state.isBlock(newState.block)) {
			val be = level.getBlockEntity(pos)
			if (be is ContainerContainer) {
				be.dropContents(level, pos)
			}
		}

		super.onRemove(state, level, pos, newState, movedByPiston)
	}

}