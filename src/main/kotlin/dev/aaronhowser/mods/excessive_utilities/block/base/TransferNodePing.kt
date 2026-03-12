package dev.aaronhowser.mods.excessive_utilities.block.base

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

	fun reset() {
		currentPingPos = homePos
		cameFromDirection = homePlacedOnDirection
	}

	//TODO: Remember forks and if there's nowhere for it to go, backtrack to the last fork and try a different path
	/** @return `true` if the ping advanced properly, `false` if it had nowhere to march to */
	fun march(level: Level): Boolean {
		val nextDirections = getNextDirections(level).toMutableList()

		nextDirections.removeIf { dir ->
			val nextPos = currentPingPos.relative(dir)
			val blockThere = level.getBlockState(nextPos).block

			blockThere !is TransferPipeBlock && blockThere !is TransferNodeBlock
		}

		if (nextDirections.isEmpty()) return false

		val nextIndex = level.random.nextInt(nextDirections.size)
		val nextDirection = nextDirections[nextIndex]

		currentPingPos = currentPingPos.relative(nextDirection)
		cameFromDirection = nextDirection.opposite

		return true
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
					stateAtPingPos.getValue(property).allowsTravel
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