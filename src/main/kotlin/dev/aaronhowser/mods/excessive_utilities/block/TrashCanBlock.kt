package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.excessive_utilities.block.entity.EnergyTrashCanBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.FluidTrashCanBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.TrashCanBlockEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
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
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

class TrashCanBlock(
	val type: Type
) : Block(Properties.ofFullCopy(Blocks.STONE)), EntityBlock {

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

	override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity? {
		if (type == Type.CHEST) {
			val be = TrashCanBlockEntity(pos, state)
			be.isChest = true
			return be
		}

		if (type == Type.ITEM) {
			return TrashCanBlockEntity(pos, state)
		}

		if (type == Type.FLUID) {
			return FluidTrashCanBlockEntity(pos, state)
		}

		if (type == Type.ENERGY) {
			return EnergyTrashCanBlockEntity(pos, state)
		}

		return null
	}

	override fun useWithoutItem(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hitResult: BlockHitResult
	): InteractionResult {
		val blockEntity = level.getBlockEntity(pos)

		if (blockEntity is MenuProvider) {
			player.openMenu(blockEntity)
			return InteractionResult.sidedSuccess(level.isClientSide)
		}

		return InteractionResult.PASS
	}

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return if (type == Type.CHEST) CHEST_SHAPE else REGULAR_SHAPE
	}

	companion object {
		val FACING: DirectionProperty = BlockStateProperties.HORIZONTAL_FACING

		val REGULAR_SHAPE: VoxelShape = Shapes.or(
			box(2.0, 0.0, 2.0, 14.0, 10.0, 14.0),
			box(1.0, 10.0, 1.0, 15.0, 14.0, 15.0)
		)

		val CHEST_SHAPE: VoxelShape = Shapes.or(
			box(2.0, 0.0, 2.0, 14.0, 10.0, 14.0),
			box(1.0, 10.0, 1.0, 15.0, 14.0, 15.0)
		)
	}

	enum class Type(
		val id: String
	) : StringRepresentable {
		ITEM("ITEM"),
		CHEST("CHEST"),
		FLUID("FLUID"),
		ENERGY("ENERGY")
		;

		override fun getSerializedName(): String = id
	}

}