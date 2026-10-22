package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.block_entity.FilingCabinetBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
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
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.state.BlockBehaviour

class FilingCabinetBlock(properties: BlockBehaviour.Properties) : Block(properties), EntityBlock {

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
		return FilingCabinetBlockEntity(pos, state)
	}

	override fun affectNeighborsAfterRemoval(state: BlockState, level: ServerLevel, pos: BlockPos, movedByPiston: Boolean) {
		if (true) {
			val be = level.getBlockEntity(pos)
			if (be is FilingCabinetBlockEntity) {
				be.dropAllItems()
			}
		}

		super.affectNeighborsAfterRemoval(state, level, pos, movedByPiston)
	}

	override fun useWithoutItem(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hitResult: BlockHitResult
	): InteractionResult {
		if (level.isServerSide) {
			val blockEntity = level.getBlockEntity(pos)

			if (blockEntity is FilingCabinetBlockEntity) {
				val count = blockEntity.getItemCount()
				player.sendSystemMessage(Component.literal(count.toString()))
			}
		}

		return InteractionResult.SUCCESS
	}

	companion object {
		val FACING: EnumProperty<Direction> = BlockStateProperties.HORIZONTAL_FACING
	}

}












