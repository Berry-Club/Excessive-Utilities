package dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node.ping

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level

class DepthFirstTransferNodePing(
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