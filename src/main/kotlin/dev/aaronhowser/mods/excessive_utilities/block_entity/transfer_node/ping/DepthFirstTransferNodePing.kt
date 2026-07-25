package dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node.ping

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.random
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level

class DepthFirstTransferNodePing(
	homePos: BlockPos,
	homePlacedOnDirection: Direction
) : TransferNodePing(homePos, homePlacedOnDirection) {

	override val searchMode: SearchMode = SearchMode.DEPTH_FIRST

	private val path = mutableListOf(homePos)
	private val visitedPositions = mutableSetOf(homePos)

	override fun reset() {
		super.reset()
		path.clear()
		path.add(homePos)
		visitedPositions.clear()
		visitedPositions.add(homePos)
	}

	override fun march(level: Level) {
		val nextDirections = getMarchableDirections(level).filter { direction ->
			currentPingPos.relative(direction) !in visitedPositions
		}

		if (nextDirections.isNotEmpty()) {
			val nextDirection = nextDirections.random(level.random)
			move(nextDirection)
			path.add(currentPingPos)
			visitedPositions.add(currentPingPos)
			return
		}

		if (path.size <= 1) {
			reset()
			return
		}

		val previousPos = currentPingPos
		path.removeLast()
		val nextPos = path.last()
		val cameFrom = Direction.fromDelta(
			previousPos.x - nextPos.x,
			previousPos.y - nextPos.y,
			previousPos.z - nextPos.z
		) ?: homePlacedOnDirection

		move(nextPos, cameFrom)
	}
}