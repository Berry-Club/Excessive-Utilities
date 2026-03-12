package dev.aaronhowser.mods.excessive_utilities.block.base

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction

// https://github.com/Leclowndu93150/Extra-Utilities/blob/master/src/main/java/com/leclowndu93150/extrautils2/blockentity/transfer/TransferNodePing.java
class TransferNodePing(
	private val homePos: BlockPos,
	private val homePlacedOnDirection: Direction
) {

	var currentPingPos: BlockPos = homePos
		private set

	var movingInDirection: Direction = homePlacedOnDirection.opposite
		private set

	private val forkPositions: MutableList<BlockPos> = mutableListOf()

	fun reset() {
		currentPingPos = homePos
		movingInDirection = homePlacedOnDirection.opposite
		forkPositions.clear()
	}

}