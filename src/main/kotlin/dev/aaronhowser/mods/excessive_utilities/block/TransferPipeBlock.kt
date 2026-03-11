package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.util.StringRepresentable
import net.minecraft.world.InteractionHand
import net.minecraft.world.ItemInteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.LevelAccessor
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.EnumProperty
import net.minecraft.world.phys.BlockHitResult
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.Tags

class TransferPipeBlock : Block(Properties.of().strength(0.5f).noOcclusion()) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(NORTH, ConnectionType.NONE)
				.setValue(EAST, ConnectionType.NONE)
				.setValue(SOUTH, ConnectionType.NONE)
				.setValue(WEST, ConnectionType.NONE)
				.setValue(UP, ConnectionType.NONE)
				.setValue(DOWN, ConnectionType.NONE)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN)
	}

	override fun getStateForPlacement(context: BlockPlaceContext): BlockState {
		return updateConnections(context.level, context.clickedPos, defaultBlockState())
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

	override fun useItemOn(
		stack: ItemStack,
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hand: InteractionHand,
		hitResult: BlockHitResult
	): ItemInteractionResult {
		if (!stack.isItem(Tags.Items.TOOLS_WRENCH)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION

		if (level.isServerSide) {
			toggleBlocked(level, pos, hitResult.direction)
		}

		return ItemInteractionResult.sidedSuccess(level.isClientSide)
	}

	companion object {
		val NORTH: EnumProperty<ConnectionType> = connectedProperty("north")
		val EAST: EnumProperty<ConnectionType> = connectedProperty("east")
		val SOUTH: EnumProperty<ConnectionType> = connectedProperty("south")
		val WEST: EnumProperty<ConnectionType> = connectedProperty("west")
		val UP: EnumProperty<ConnectionType> = connectedProperty("up")
		val DOWN: EnumProperty<ConnectionType> = connectedProperty("down")

		private val CONNECTIONS: Array<EnumProperty<ConnectionType>> = arrayOf(DOWN, UP, NORTH, SOUTH, WEST, EAST)

		private fun connectedProperty(name: String): EnumProperty<ConnectionType> {
			return EnumProperty.create(name, ConnectionType::class.java, *ConnectionType.entries.toTypedArray())
		}

		private fun updateConnections(level: Level, pos: BlockPos, oldState: BlockState): BlockState {
			var state = oldState

			for (dir in Direction.entries) {
				val ordinal = dir.ordinal

				val property = CONNECTIONS[ordinal]
				val isBlocked = state.getValue(property) == ConnectionType.BLOCKED
				if (isBlocked) continue

				state = if (canConnectTo(level, pos, dir)) {
					state.setValue(property, ConnectionType.CONNECTED)
				} else {
					state.setValue(property, ConnectionType.NONE)
				}
			}

			return state
		}

		private fun canConnectTo(level: Level, pipePos: BlockPos, direction: Direction): Boolean {
			val neighborPos = pipePos.relative(direction)

			val neighborBlock = level.getBlockState(neighborPos).block
			if (neighborBlock is TransferPipeBlock) return true
			if (neighborBlock is TransferNodeBlock) return true

			val hasItemHandler = level.getCapability(Capabilities.ItemHandler.BLOCK, neighborPos, direction.opposite) != null
			if (hasItemHandler) return true

			val hasFluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, neighborPos, direction.opposite) != null
			if (hasFluidHandler) return true

			val hasEnergyHandler = level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, direction.opposite) != null
			return hasEnergyHandler
		}

		fun toggleBlocked(level: Level, pipePos: BlockPos, direction: Direction) {
			var state = level.getBlockState(pipePos)
			if (!state.isBlock(ModBlocks.TRANSFER_PIPE)) return

			val property = CONNECTIONS[direction.ordinal]
			val currentConnection = state.getValue(property)

			state = when (currentConnection) {
				ConnectionType.NONE -> return
				ConnectionType.BLOCKED -> state.setValue(property, ConnectionType.NONE)
				ConnectionType.CONNECTED -> state.setValue(property, ConnectionType.BLOCKED)
			}

			val newState = updateConnections(level, pipePos, state)
			level.setBlockAndUpdate(pipePos, newState)
		}
	}

	enum class ConnectionType(
		val id: String,
		val allowsTravel: Boolean
	) : StringRepresentable {
		NONE("none", allowsTravel = false),
		BLOCKED("blocked", allowsTravel = false),
		CONNECTED("connected", allowsTravel = true)
		;

		override fun getSerializedName(): String = id
	}

}