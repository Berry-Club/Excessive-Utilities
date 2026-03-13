package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block_entity.PeacefulTableBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class PeacefulTableBlock : Block(Properties.ofFullCopy(Blocks.CRAFTING_TABLE)), EntityBlock {

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return SHAPE
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return PeacefulTableBlockEntity(pos, state)
	}

	override fun <T : BlockEntity> getTicker(
		level: Level,
		state: BlockState,
		blockEntityType: BlockEntityType<T>
	): BlockEntityTicker<T>? {
		return BaseEntityBlock.createTickerHelper(
			blockEntityType,
			ModBlockEntityTypes.PEACEFUL_TABLE.get(),
			PeacefulTableBlockEntity::tick
		)
	}

	companion object {
		val SHAPE: VoxelShape =
			Shapes.or(
				box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0),
				box(1.0, 0.0, 1.0, 5.0, 12.0, 5.0),
				box(11.0, 0.0, 1.0, 15.0, 12.0, 5.0),
				box(1.0, 0.0, 11.0, 5.0, 12.0, 15.0),
				box(11.0, 0.0, 11.0, 15.0, 12.0, 15.0)
			)
	}

}