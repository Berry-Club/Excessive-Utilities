package dev.aaronhowser.mods.excessive_utilities.block_entity.base

import dev.aaronhowser.mods.excessive_utilities.block.TransferNodeBlock
import dev.aaronhowser.mods.excessive_utilities.block.TransferPipeBlock
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level

// https://github.com/Leclowndu93150/Extra-Utilities/blob/master/src/main/java/com/leclowndu93150/extrautils2/blockentity/transfer/TransferNodePing.java
class TransferNodePing(
	private val homePos: BlockPos,
	private val homePlacedOnDirection: Direction
) {

	var currentPingPos: BlockPos = homePos
		private set

	private var cameFromDirection: Direction = homePlacedOnDirection
	private val depthFirstPath: MutableList<BlockPos> = mutableListOf(homePos)
	private val depthFirstVisited: MutableSet<BlockPos> = mutableSetOf(homePos)
	private val breadthFirstQueue: ArrayDeque<SearchStep> = ArrayDeque()
	private val breadthFirstVisited: MutableSet<BlockPos> = mutableSetOf(homePos)
	private var searchMode: SearchMode = SearchMode.RANDOM

	fun reset() {
		currentPingPos = homePos
		cameFromDirection = homePlacedOnDirection
		depthFirstPath.clear()
		depthFirstPath.add(homePos)
		depthFirstVisited.clear()
		depthFirstVisited.add(homePos)
		breadthFirstQueue.clear()
		breadthFirstVisited.clear()
		breadthFirstVisited.add(homePos)
	}

	fun march(level: Level, depthFirst: Boolean, breadthFirst: Boolean) {
		val newSearchMode = when {
			breadthFirst -> SearchMode.BREADTH_FIRST
			depthFirst -> SearchMode.DEPTH_FIRST
			else -> SearchMode.RANDOM
		}

		if (newSearchMode != searchMode) {
			reset()
			searchMode = newSearchMode
		}

		when (searchMode) {
			SearchMode.RANDOM -> marchRandomly(level)
			SearchMode.DEPTH_FIRST -> marchDepthFirst(level)
			SearchMode.BREADTH_FIRST -> marchBreadthFirst(level)
		}
	}

	private fun marchRandomly(level: Level) {
		val nextDirections = getNextDirections(level).toMutableList()

		nextDirections.removeIf { !canMarchTo(level, currentPingPos.relative(it)) }

		if (nextDirections.isEmpty()) {
			reset()
			return
		}

		val nextIndex = level.random.nextInt(nextDirections.size)
		val nextDirection = nextDirections[nextIndex]

		currentPingPos = currentPingPos.relative(nextDirection)
		cameFromDirection = nextDirection.opposite
	}

	private fun marchDepthFirst(level: Level) {
		val nextDirections = getNextDirections(level).toMutableList()
		nextDirections.removeIf {
			val nextPos = currentPingPos.relative(it)
			nextPos in depthFirstVisited || !canMarchTo(level, nextPos)
		}

		if (nextDirections.isNotEmpty()) {
			val nextDirection = nextDirections[level.random.nextInt(nextDirections.size)]
			currentPingPos = currentPingPos.relative(nextDirection)
			cameFromDirection = nextDirection.opposite
			depthFirstPath.add(currentPingPos)
			depthFirstVisited.add(currentPingPos)
			return
		}

		if (depthFirstPath.size <= 1) {
			reset()
			return
		}

		val previousPos = depthFirstPath.removeLast()
		currentPingPos = depthFirstPath.last()
		cameFromDirection = directionFromTo(currentPingPos, previousPos)
	}

	private fun marchBreadthFirst(level: Level) {
		val nextDirections = getNextDirections(level)
		for (direction in nextDirections) {
			val nextPos = currentPingPos.relative(direction)
			if (nextPos in breadthFirstVisited || !canMarchTo(level, nextPos)) continue

			breadthFirstQueue.addLast(SearchStep(nextPos, direction.opposite))
			breadthFirstVisited.add(nextPos)
		}

		if (breadthFirstQueue.isEmpty()) {
			reset()
			return
		}

		val nextStep = breadthFirstQueue.removeFirst()
		currentPingPos = nextStep.pos
		cameFromDirection = nextStep.cameFromDirection
	}

	private fun canMarchTo(level: Level, pos: BlockPos): Boolean {
		val block = level.getBlockState(pos).block
		return block is TransferPipeBlock || block is TransferNodeBlock
	}

	private fun directionFromTo(from: BlockPos, to: BlockPos): Direction {
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
				directions.removeIf { dir ->
					val property = TransferPipeBlock.CONNECTIONS[dir.ordinal]
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

	private data class SearchStep(
		val pos: BlockPos,
		val cameFromDirection: Direction
	)

	private enum class SearchMode {
		RANDOM,
		DEPTH_FIRST,
		BREADTH_FIRST
	}

}