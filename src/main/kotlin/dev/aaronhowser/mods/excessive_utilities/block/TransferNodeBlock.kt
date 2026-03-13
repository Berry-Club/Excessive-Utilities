package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.util.ContainerContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.GpDrainBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.capabilities.Capabilities


class TransferNodeBlock(
	val type: Type,
	val isRetrieval: Boolean
) : GpDrainBlock(
	Properties.of()
		.strength(1.5f, 6f)
		.requiresCorrectToolForDrops()
		.noOcclusion()
) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(PLACED_ON, Direction.DOWN)
				.setValue(NORTH, false)
				.setValue(EAST, false)
				.setValue(SOUTH, false)
				.setValue(WEST, false)
				.setValue(UP, false)
				.setValue(DOWN, false)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(PLACED_ON, NORTH, EAST, SOUTH, WEST, UP, DOWN)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
		var state = defaultBlockState().setValue(PLACED_ON, context.clickedFace.opposite)
		state = updateConnections(context.level, context.clickedPos, state)
		return state
	}

	override fun updateShape(
		state: BlockState,
		direction: Direction,
		neighborState: BlockState,
		level: LevelAccessor,
		pos: BlockPos,
		neighborPos: BlockPos
	): BlockState {
		if (level is Level) {
			return updateConnections(level, pos, state)
		}

		return state
	}

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		val placedOn = state.getValue(PLACED_ON)

		val nodeShape = NODE_SHAPES[placedOn.ordinal]
		val pipeShape = TransferPipeBlock.ARM_SHAPES[placedOn.ordinal] ?: Shapes.empty()

		var shape = Shapes.or(nodeShape, pipeShape, TransferPipeBlock.CENTER_SHAPE)

		for (dir in Direction.entries) {
			val ordinal = dir.ordinal
			val property = CONNECTIONS[ordinal]
			if (state.getValue(property)) {
				val pipeShape = TransferPipeBlock.ARM_SHAPES[ordinal] ?: continue
				shape = Shapes.or(shape, pipeShape)
			}
		}

		return shape
	}

	override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
		val be = level.getBlockEntity(pos)

		if (be is MenuProvider) {
			player.openMenu(be)
			return InteractionResult.sidedSuccess(level.isClientSide)
		}

		return InteractionResult.PASS
	}

	override fun getBlockEntityType(): BlockEntityType<out GpDrainBlockEntity> {
		return when (type) {
			Type.ITEM -> ModBlockEntityTypes.ITEM_TRANSFER_NODE.get()
			else -> error("NYI")
		}
	}

	override fun onRemove(state: BlockState, level: Level, pos: BlockPos, newState: BlockState, movedByPiston: Boolean) {
		if (!state.isBlock(newState.block)) {
			val be = level.getBlockEntity(pos)
			if (be is ContainerContainer) {
				be.dropContents(level, pos)
			}
		}

		super.onRemove(state, level, pos, newState, movedByPiston)
	}

	override fun setPlacedBy(level: Level, pos: BlockPos, state: BlockState, placer: LivingEntity?, stack: ItemStack) {
		val blockEntity = level.getBlockEntity(pos)
		if (blockEntity is TransferNodeBlockEntity && placer != null) {
			blockEntity.ownerUuid = placer.uuid
			blockEntity.isRetrieval = isRetrieval
		}
	}

	fun canConnectTo(level: Level, pipePos: BlockPos, direction: Direction): Boolean {
		val neighborPos = pipePos.relative(direction)

		val neighborBlock = level.getBlockState(neighborPos).block
		if (neighborBlock is TransferPipeBlock) return true
		if (neighborBlock is TransferNodeBlock) return true

		when (type) {
			Type.ITEM -> {
				val hasItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, neighborPos, direction.opposite) != null
				if (hasItemHandler) return true
			}

			Type.FLUID -> {
				val hasFluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, direction.opposite) != null
				if (hasFluidHandler) return true
			}

			Type.ENERGY -> {
				val hasEnergyHandler = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, direction.opposite) != null
				return hasEnergyHandler
			}
		}

		return false
	}

	private fun updateConnections(level: Level, pipePos: BlockPos, oldState: BlockState): BlockState {
		var state = oldState

		val placedOn = state.getValue(PLACED_ON)
		for (dir in Direction.entries) {
			val shouldConnect = dir != placedOn && canConnectTo(level, pipePos, dir)
			val property = CONNECTIONS[dir.ordinal]
			state = state.setValue(property, shouldConnect)
		}

		return state
	}

	companion object {
		val PLACED_ON: DirectionProperty = DirectionProperty.create("placed_on", Direction.entries)
		val NORTH: BooleanProperty = BlockStateProperties.NORTH
		val SOUTH: BooleanProperty = BlockStateProperties.SOUTH
		val EAST: BooleanProperty = BlockStateProperties.EAST
		val WEST: BooleanProperty = BlockStateProperties.WEST
		val UP: BooleanProperty = BlockStateProperties.UP
		val DOWN: BooleanProperty = BlockStateProperties.DOWN

		// Same order as the Direction enum, so we can use the ordinal as an index
		val CONNECTIONS: Array<BooleanProperty> = arrayOf(DOWN, UP, NORTH, SOUTH, WEST, EAST)

		val NODE_SHAPES =
			arrayOf(
				box(1.0, 0.0, 1.0, 15.0, 3.0, 15.0), // DOWN
				box(1.0, 13.0, 1.0, 15.0, 16.0, 15.0), // UP
				box(1.0, 1.0, 0.0, 15.0, 15.0, 3.0), // NORTH
				box(1.0, 1.0, 13.0, 15.0, 15.0, 16.0), // SOUTH
				box(0.0, 1.0, 1.0, 3.0, 15.0, 15.0), // WEST
				box(13.0, 1.0, 1.0, 16.0, 15.0, 15.0) // EAST
			)

	}

	enum class Type {
		ITEM, FLUID, ENERGY
	}

}