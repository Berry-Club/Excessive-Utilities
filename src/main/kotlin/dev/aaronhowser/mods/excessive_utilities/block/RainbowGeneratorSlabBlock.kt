package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape

class RainbowGeneratorSlabBlock(
	val isTop: Boolean
) : Block(Properties.ofFullCopy(Blocks.STONE_SLAB)) {

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return if (isTop) TOP_SHAPE else BOTTOM_SHAPE
	}

	companion object {
		val TOP_SHAPE: VoxelShape = box(0.0, 8.0, 0.0, 16.0, 16.0, 16.0)
		val BOTTOM_SHAPE: VoxelShape = box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0)
	}

}