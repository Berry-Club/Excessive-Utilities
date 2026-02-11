package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class EnderQuarryBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.ENDER_QUARRY.get(), pos, state) {

	var minPos: BlockPos? = null
		private set(value) {
			field = value
			setChanged()
		}

	var maxPos: BlockPos? = null
		private set(value) {
			field = value
			setChanged()
		}

	var targetPos: BlockPos? = null
		private set(value) {
			field = value
			setChanged()
		}

	private fun trySetBoundaries(level: ServerLevel) {
		val horizontals = Direction.Plane.HORIZONTAL

		for (dir in horizontals) {
			val posThere = worldPosition.relative(dir)
			val stateThere = level.getBlockState(posThere)

			if (stateThere.isBlock(BlockTags.FENCES) && trySetBoundariesFromFences(level, posThere)) {
				return
			}

			if (stateThere.isBlock(ModBlocks.ENDER_MARKER) && trySetBoundariesFromMarkers(level, posThere)) {
				return
			}
		}
	}

	private fun trySetBoundariesFromFences(level: ServerLevel, fencePos: BlockPos): Boolean {
		val firstFenceState = level.getBlockState(fencePos)
		if (!firstFenceState.isBlock(BlockTags.FENCES)) return false

		fun isValidFence(state: BlockState): Boolean {
			val hasProperties = state.hasProperty(FenceBlock.NORTH)
					&& state.hasProperty(FenceBlock.EAST)
					&& state.hasProperty(FenceBlock.SOUTH)
					&& state.hasProperty(FenceBlock.WEST)

			if (!hasProperties) return false

			var connections = 0
			if (state.getValue(FenceBlock.NORTH)) connections++
			if (state.getValue(FenceBlock.EAST)) connections++
			if (state.getValue(FenceBlock.SOUTH)) connections++
			if (state.getValue(FenceBlock.WEST)) connections++

			return connections == 2
		}

		val dirToProperty = mapOf(
			Direction.NORTH to FenceBlock.NORTH,
			Direction.EAST to FenceBlock.EAST,
			Direction.SOUTH to FenceBlock.SOUTH,
			Direction.WEST to FenceBlock.WEST
		)

		val firstDirection = dirToProperty
			.entries
			.firstOrNull { (dir, property) -> firstFenceState.getValue(property) }
			?.key
			?: return false

		var currentDirection = firstDirection
		val corners = mutableListOf(fencePos)

		val currentPos = fencePos.mutable()
		var iterations = 0

		val maxFences = ServerConfig.CONFIG.enderQuarryFencePerimeterLimit.get()

		while (iterations < maxFences) {
			iterations++

			currentPos.move(currentDirection)
			val currentState = level.getBlockState(currentPos)

			if (!isValidFence(currentState)) return false

			val property = dirToProperty[currentDirection] ?: return false
			val canKeepGoing = currentState.getValue(property)

			if (!canKeepGoing) {
				val nextDirection = dirToProperty
					.entries
					.firstOrNull { (dir, property) ->
						dir != currentDirection.opposite && currentState.getValue(property)
					}
					?.key
					?: return false

				currentDirection = nextDirection
				corners.add(currentPos.immutable())
			}

			if (currentPos == fencePos) break
		}

		if (corners.size < 4) return false

		val minX = corners.minOf(BlockPos::getX)
		val minZ = corners.minOf(BlockPos::getZ)
		val maxX = corners.maxOf(BlockPos::getX)
		val maxZ = corners.maxOf(BlockPos::getZ)

		if (minX == maxX || minZ == maxZ) return false

		val y = blockPos.y

		minPos = BlockPos(minX, y, minZ)
		maxPos = BlockPos(maxX, y, maxZ)

		targetPos = minPos
		return true
	}

	private fun trySetBoundariesFromMarkers(level: ServerLevel, markerPos: BlockPos): Boolean {
		val firstMarkerState = level.getBlockState(markerPos)
		if (!firstMarkerState.isBlock(ModBlocks.ENDER_MARKER)) return false

		val markers = mutableSetOf(markerPos)

		val searchDistance = ServerConfig.CONFIG.enderQuarryMarkerSearchDistance.get()

		val horizontals = Direction.Plane.HORIZONTAL
		for (dir in horizontals) {
			val checkPos = markerPos.relative(dir).mutable()
			var locationsChecked = 0

			while (locationsChecked < searchDistance) {
				locationsChecked++

				val checkState = level.getBlockState(checkPos)
				if (checkState.isBlock(ModBlocks.ENDER_MARKER)) {
					markers.add(checkPos.immutable())
					break
				}
			}
		}

		if (markers.size < 3) return false

		val minX = markers.minOf(BlockPos::getX)
		val minZ = markers.minOf(BlockPos::getZ)
		val maxX = markers.maxOf(BlockPos::getX)
		val maxZ = markers.maxOf(BlockPos::getZ)

		if (minX == maxX || minZ == maxZ) return false

		val y = blockPos.y

		minPos = BlockPos(minX, y, minZ)
		maxPos = BlockPos(maxX, y, maxZ)

		targetPos = minPos
		return true
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		val min = minPos
		val max = maxPos

		if (min != null && max != null) {
			tag.putLong(MIN_POS_NBT, min.asLong())
			tag.putLong(MAX_POS_NBT, max.asLong())
		}

		val target = targetPos
		if (target != null) {
			tag.putLong(TARGET_POS_NBT, target.asLong())
		}

	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		if (tag.contains(MIN_POS_NBT) && tag.contains(MAX_POS_NBT)) {
			minPos = BlockPos.of(tag.getLong(MIN_POS_NBT))
			maxPos = BlockPos.of(tag.getLong(MAX_POS_NBT))
		}

		if (tag.contains(TARGET_POS_NBT)) {
			targetPos = BlockPos.of(tag.getLong(TARGET_POS_NBT))
		}
	}

	companion object {
		const val MIN_POS_NBT = "MinPos"
		const val MAX_POS_NBT = "MaxPos"
		const val TARGET_POS_NBT = "TargetPos"
	}

}