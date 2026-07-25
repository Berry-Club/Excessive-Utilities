package dev.aaronhowser.mods.excessive_utilities.block_entity.base

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

	protected fun move(searchStep: SearchStep) {
		currentPingPos = searchStep.pos
		cameFromDirection = searchStep.cameFromDirection
	}

	private fun canMarchTo(level: Level, pos: BlockPos): Boolean {
		val block = level.getBlockState(pos).block
		return block is TransferPipeBlock || block is TransferNodeBlock
	}

	protected fun directionFromTo(from: BlockPos, to: BlockPos): Direction {
		return Direction.fromDelta(to.x - from.x, to.y - from.y, to.z - from.z)
			?: homePlacedOnDirection
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

	protected data class SearchStep(
		val pos: BlockPos,
		val cameFromDirection: Direction
	)
}

private class RandomTransferNodePing(
	homePos: BlockPos,
	homePlacedOnDirection: Direction
) : TransferNodePing(homePos, homePlacedOnDirection) {

	override val searchMode: SearchMode = SearchMode.RANDOM

	override fun march(level: Level) {
		val nextDirections = getMarchableDirections(level)
		if (nextDirections.isEmpty()) {
			reset()
			return
		}

		move(nextDirections[level.random.nextInt(nextDirections.size)])
	}
}

private class DepthFirstTransferNodePing(
	homePos: BlockPos,
	homePlacedOnDirection: Direction
) : TransferNodePing(homePos, homePlacedOnDirection) {

	override val searchMode: SearchMode = SearchMode.DEPTH_FIRST

	private val path: MutableList<BlockPos> = mutableListOf(homePos)
	private val visited: MutableSet<BlockPos> = mutableSetOf(homePos)

	override fun reset() {
		super.reset()
		path.clear()
		path.add(homePos)
		visited.clear()
		visited.add(homePos)
	}

	override fun march(level: Level) {
		val nextDirections = getMarchableDirections(level).filter { direction ->
			currentPingPos.relative(direction) !in visited
		}

		if (nextDirections.isNotEmpty()) {
			val nextDirection = nextDirections[level.random.nextInt(nextDirections.size)]
			move(nextDirection)
			path.add(currentPingPos)
			visited.add(currentPingPos)
			return
		}

		if (path.size <= 1) {
			reset()
			return
		}

		val previousPos = path.removeLast()
		val nextPos = path.last()
		move(SearchStep(nextPos, directionFromTo(nextPos, previousPos)))
	}
}

private class BreadthFirstTransferNodePing(
	homePos: BlockPos,
	homePlacedOnDirection: Direction
) : TransferNodePing(homePos, homePlacedOnDirection) {

	override val searchMode: SearchMode = SearchMode.BREADTH_FIRST

	private val queue: ArrayDeque<SearchStep> = ArrayDeque()
	private val visited: MutableSet<BlockPos> = mutableSetOf(homePos)

	override fun reset() {
		super.reset()
		queue.clear()
		visited.clear()
		visited.add(homePos)
	}

	override fun march(level: Level) {
		val nextDirections = getMarchableDirections(level).toMutableList()

		while (nextDirections.isNotEmpty()) {
			val directionIndex = level.random.nextInt(nextDirections.size)
			val direction = nextDirections.removeAt(directionIndex)
			val nextPos = currentPingPos.relative(direction)
			if (nextPos in visited) continue

			queue.addLast(SearchStep(nextPos, direction.opposite))
			visited.add(nextPos)
		}

		if (queue.isEmpty()) {
			reset()
			return
		}

		move(queue.removeFirst())
	}
}