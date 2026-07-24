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
	private var depthFirstEnabled: Boolean = false

	fun reset() {
		currentPingPos = homePos
		cameFromDirection = homePlacedOnDirection
		depthFirstPath.clear()
		depthFirstPath.add(homePos)
		depthFirstVisited.clear()
		depthFirstVisited.add(homePos)
	}

	fun march(level: Level, depthFirst: Boolean) {
		if (depthFirst != depthFirstEnabled) {
			reset()
			depthFirstEnabled = depthFirst
		}

		if (depthFirst) {
			marchDepthFirst(level)
		} else {
			marchRandomly(level)
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

}