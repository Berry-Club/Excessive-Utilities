package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.block.entity.SlightlyLargerChestBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.Containers
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.EntityBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.BlockHitResult

class SlightlyLargerChestBlock : Block(Properties.ofFullCopy(Blocks.CHEST)), EntityBlock {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(FACING, Direction.NORTH)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(FACING)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState? {
		return defaultBlockState()
			.setValue(FACING, context.horizontalDirection.opposite)
	}

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
		return SlightlyLargerChestBlockEntity(pos, state)
	}

	override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
		val be = level.getBlockEntity(pos)
		if (be is MenuProvider) {
			player.openMenu(be)
			return InteractionResult.sidedSuccess(level.isClientSide)
		} else {
			return InteractionResult.PASS
		}
	}

	override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, movedByPiston: Boolean) {
		if (!state.isBlock(newState.block)) {
			val be = level.getBlockEntity(pos)
			if (be is SlightlyLargerChestBlockEntity) {
				Containers.dropContents(level, pos, be.container)
			}
		}
		super.onRemove(state, level, pos, newState, movedByPiston)
	}

	companion object {
		val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING
	}

}