package dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node.ping

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.random
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level

class BreadthFirstTransferNodePing(
	homePos: BlockPos,
	homePlacedOnDirection: Direction
) : TransferNodePing(homePos, homePlacedOnDirection) {

	override val searchMode: SearchMode = SearchMode.BREADTH_FIRST

	private val positionsToVisit = ArrayDeque<PositionToVisit>()
	private val visitedPositions = mutableSetOf(homePos)

	override fun reset() {
		super.reset()
		positionsToVisit.clear()
		visitedPositions.clear()
		visitedPositions.add(homePos)
	}

	override fun march(level: Level) {
		val directions = getMarchableDirections(level).toMutableList()

		while (directions.isNotEmpty()) {
			val direction = directions.random(level.random)
			directions.remove(direction)
			val nextPos = currentPingPos.relative(direction)
			if (nextPos in visitedPositions) continue

			positionsToVisit.addLast(PositionToVisit(nextPos, direction.opposite))
			visitedPositions.add(nextPos)
		}

		if (positionsToVisit.isEmpty()) {
			reset()
			return
		}

		val nextPosition = positionsToVisit.removeFirst()
		move(nextPosition.pos, nextPosition.cameFrom)
	}

	private data class PositionToVisit(
		val pos: BlockPos,
		val cameFrom: Direction
	)
}