package dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node.ping

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level

class BreadthFirstTransferNodePing(
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