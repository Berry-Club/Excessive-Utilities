package dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node.ping

import dev.aaronhowser.mods.excessive_utilities.block.TransferNodeBlock
import dev.aaronhowser.mods.excessive_utilities.block.TransferPipeBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level

sealed class TransferNodePing(
	protected val homePos: BlockPos,
	protected val homePlacedOnDirection: Direction
) {

	abstract val searchMode: SearchMode

	var currentPingPos: BlockPos = homePos
		protected set

	protected var cameFromDirection: Direction = homePlacedOnDirection

	open fun reset() {
		currentPingPos = homePos
		cameFromDirection = homePlacedOnDirection
	}

	abstract fun march(level: Level)

	protected fun getMarchableDirections(level: Level): List<Direction> {
		return getNextDirections(level).filter { direction ->
			canMarchTo(level, currentPingPos.relative(direction))
		}
	}

	protected fun move(direction: Direction) {
		currentPingPos = currentPingPos.relative(direction)
		cameFromDirection = direction.opposite
	}

	protected fun move(pos: BlockPos, cameFrom: Direction) {
		currentPingPos = pos
		cameFromDirection = cameFrom
	}

	private fun canMarchTo(level: Level, pos: BlockPos): Boolean {
		val block = level.getBlockState(pos).block
		return block is TransferPipeBlock || block is TransferNodeBlock
	}

	/** @return A list of directions that Transfer Pipes are allowed to search from, or that the Ping can march to */
	fun getNextDirections(level: Level): List<Direction> {
		val stateAtPingPos = level.getBlockState(currentPingPos)
		val block = stateAtPingPos.block

		val directions = Direction.entries.toMutableList()
		directions.remove(cameFromDirection)

		when (block) {
			is TransferPipeBlock -> {
				directions.removeIf { direction ->
					val property = TransferPipeBlock.CONNECTIONS[direction.ordinal]
					!stateAtPingPos.getValue(property).allowsTravel
				}
			}

			is TransferNodeBlock -> {
				val placedOnDirection = stateAtPingPos.getValue(TransferNodeBlock.PLACED_ON)
				directions.remove(placedOnDirection)
			}

			else -> {
				reset()
				return emptyList()
			}
		}

		return directions
	}

	enum class SearchMode {
		RANDOM,
		DEPTH_FIRST,
		BREADTH_FIRST;

		fun create(homePos: BlockPos, homePlacedOnDirection: Direction): TransferNodePing {
			return when (this) {
				RANDOM -> RandomTransferNodePing(homePos, homePlacedOnDirection)
				DEPTH_FIRST -> DepthFirstTransferNodePing(homePos, homePlacedOnDirection)
				BREADTH_FIRST -> BreadthFirstTransferNodePing(homePos, homePlacedOnDirection)
			}
		}
	}

}