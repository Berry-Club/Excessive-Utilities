package dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node.ping

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.Level

class RandomTransferNodePing(
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