package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.world.level.block.state.BlockBehaviour.Properties

import dev.aaronhowser.mods.excessive_utilities.block.base.GpDrainBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node.FluidTransferNodeBlockEntity
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
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.minecraft.util.RandomSource
import net.minecraft.world.level.LevelReader
import net.minecraft.world.level.ScheduledTickAccess
import net.neoforged.neoforge.capabilities.Capabilities


class TransferNodeBlock(
	val type: Type,
	val isRetrieval: Boolean,
	properties: Properties
) : GpDrainBlock(
	properties
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
		level: LevelReader,
		 ticks: ScheduledTickAccess,
		pos: BlockPos,
		 directionToNeighbour: Direction,
		 neighbourPos: BlockPos,
		 neighbourState: BlockState,
		 random: RandomSource
	): BlockState {
		if (level is Level) {
			return updateConnections(level, pos, state)
		}

		return state
	}

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		val key = getShapeKey(state)

		return shapeCache.computeIfAbsent(key) { buildShape(state) }
	}

	override fun useWithoutItem(state: BlockState, level: Level, pos: BlockPos, player: Player, hitResult: BlockHitResult): InteractionResult {
		val be = level.getBlockEntity(pos)

		if (be is FluidTransferNodeBlockEntity) {
			player.openMenu(be) { it.writeBlockPos(pos) }
			return InteractionResult.SUCCESS
		}

		if (be is MenuProvider) {
			player.openMenu(be)
			return InteractionResult.SUCCESS
		}

		return InteractionResult.PASS
	}

	override fun getBlockEntityType(): BlockEntityType<out GpDrainBlockEntity> {
		return when (type) {
			Type.ITEM -> ModBlockEntityTypes.ITEM_TRANSFER_NODE.get()
			Type.FLUID -> ModBlockEntityTypes.FLUID_TRANSFER_NODE.get()
			Type.ENERGY -> ModBlockEntityTypes.ENERGY_TRANSFER_NODE.get()
		}
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
				val hasItemHandler = level.getCapability(Capabilities.Item.BLOCK, neighborPos, direction.opposite) != null
				if (hasItemHandler) return true
			}

			Type.FLUID -> {
				val hasFluidHandler = level.getCapability(Capabilities.Fluid.BLOCK, neighborPos, direction.opposite) != null
				if (hasFluidHandler) return true
			}

			Type.ENERGY -> {
				val hasEnergyHandler = level.getCapability(Capabilities.Energy.BLOCK, neighborPos, direction.opposite) != null
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
		val PLACED_ON: EnumProperty<Direction> = EnumProperty.create("placed_on", Direction::class.java)
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

		private val shapeCache: MutableMap<Long, VoxelShape> = mutableMapOf()

		private fun buildShape(state: BlockState): VoxelShape {
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

		private fun getShapeKey(state: BlockState): Long {
			val placedOnOrdinal = state.getValue(PLACED_ON).ordinal.toLong()
			var result = placedOnOrdinal

			for (dir in Direction.entries) {
				val property = CONNECTIONS.getOrNull(dir.ordinal) ?: continue
				val isConnected = state.getValue(property)
				if (!isConnected) continue

				val bitPosition = dir.ordinal + 8
				result = result or (1L shl bitPosition)
			}

			return result
		}
	}

	enum class Type {
		ITEM, FLUID, ENERGY
	}

}













