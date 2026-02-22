package dev.aaronhowser.mods.excessive_utilities.block.entity

import com.mojang.authlib.GameProfile
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.Mth
import net.minecraft.util.StringRepresentable
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.common.util.FakePlayerFactory
import net.neoforged.neoforge.energy.EnergyStorage
import java.lang.ref.WeakReference
import java.util.*

//TODO:
// Block States for inactive, active, and finished
class EnderQuarryBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.ENDER_QUARRY.get(), pos, state) {

	private val energyStorage = EnergyStorage(1_000_000)

	private var uuid: UUID? = null
	private var fakePlayer: WeakReference<FakePlayer>? = null

	/**
	 * The exclusive minimum position of the mining area.
	 */
	var minBoundary: BlockPos? = null
		private set

	/**
	 * The exclusive maximum position of the mining area.
	 */
	var maxBoundary: BlockPos? = null
		private set

	/**
	 * The current target position that the Quarry is trying to mine.
	 * It will always stay less than the max boundary and more than the min boundary.
	 */
	var targetPos: BlockPos? = null
		private set(value) {
			if (field == value) return

			field = value
			setChanged()
		}

	var boundaryType: BoundaryType? = null
		private set

	private fun serverTick(level: ServerLevel) {
		if (fakePlayer?.get() == null) {
			initFakePlayer()
		}

		progressMine(level)
	}

	var progressThroughBlock = 0.0
	var feProgress = 0.0

	fun progressMine(level: ServerLevel) {
		var target = targetPos

		if (target == null) {
			if (!checkBoundaries(level)) {
				trySetBoundaries(level)
			}

			advanceTargetPos(level)
			target = targetPos
		}

		if (target == null) return

		val fePerBlock = ServerConfig.CONFIG.enderQuarryFePerBlock.get()
		if (energyStorage.energyStored < fePerBlock) return

		val blocksPerTick = ServerConfig.CONFIG.enderQuarryBlocksPerTick.get()
		progressThroughBlock += blocksPerTick

		while (progressThroughBlock >= 1.0) {
			progressThroughBlock -= 1.0
			mineBlock(level, target)
			advanceTargetPos(level)

			feProgress += fePerBlock
			val feToExtract = Mth.floor(feProgress)
			if (feToExtract > 0) {
				energyStorage.extractEnergy(feToExtract, false)
				feProgress -= feToExtract
			}

			if (energyStorage.energyStored < fePerBlock) {
				progressThroughBlock = 0.0
				break
			}
		}
	}

	private fun mineBlock(level: ServerLevel, target: BlockPos) {
		level.setBlock(target, Blocks.COBBLESTONE.defaultBlockState(), Block.UPDATE_ALL)
	}

	/**
	 * @return true if the Quarry should try to mine the block, false if it should skip it and move on to the next one
	 */
	private fun canQuarryMineBlock(level: ServerLevel, target: BlockPos): Boolean {
		val state = level.getBlockState(target)
		if (state.isAir
			|| state.hasBlockEntity()
			|| state.isBlock(Blocks.COBBLESTONE)
			|| state.isBlock(ModBlockTagsProvider.ENDER_QUARRY_BLACKLIST)
		) return false

		val unbreakable = state.getDestroySpeed(level, target) < 0
		if (unbreakable) return false

		return true
	}

	// Start at minBoundary +1 to X and Z
	// Go +X until maxBoundary - 1
	// +Z, go back to minBoundary + 1 X
	// Repeat until maxBoundary - 1 Z, then +X until maxBoundary - 1 X
	// Then go down one Y and repeat the whole process until minBuildHeight is reached
	private fun advanceTargetPos(level: ServerLevel) {
		val currentTarget = targetPos ?: return
		val min = minBoundary ?: return
		val max = maxBoundary ?: return

		fun getNextPos(currentPos: BlockPos): BlockPos? {
			return when {
				currentPos.x < max.x - 1 ->
					currentPos.offset(1, 0, 0)

				currentPos.z < max.z - 1 ->
					BlockPos(min.x + 1, currentPos.y, currentPos.z + 1)

				currentPos.y > level.minBuildHeight + 1 ->
					BlockPos(min.x + 1, currentPos.y - 1, min.z + 1)

				else -> null
			}
		}

		var nextPos = getNextPos(currentTarget)

		while (nextPos != null) {
			if (canQuarryMineBlock(level, nextPos)) {
				targetPos = nextPos
				return
			}

			nextPos = getNextPos(nextPos)
		}

		targetPos = null
	}

	fun checkBoundaries(level: ServerLevel): Boolean {
		val min = minBoundary ?: return false
		val max = maxBoundary ?: return false
		val type = boundaryType ?: return false

		val y = blockPos.y

		if (type == BoundaryType.FENCE) {
			val mutablePos = BlockPos.MutableBlockPos()

			for (x in min.x..max.x) {
				mutablePos.set(x, y, min.z)
				val minState = level.getBlockState(mutablePos)
				if (!minState.isBlock(BlockTags.FENCES)) return false

				mutablePos.set(x, y, max.z)
				val maxState = level.getBlockState(mutablePos)
				if (!maxState.isBlock(BlockTags.FENCES)) return false
			}

			for (z in min.z..max.z) {
				mutablePos.set(min.x, y, z)
				val minState = level.getBlockState(mutablePos)
				if (!minState.isBlock(BlockTags.FENCES)) return false

				mutablePos.set(max.x, y, z)
				val maxState = level.getBlockState(mutablePos)
				if (!maxState.isBlock(BlockTags.FENCES)) return false
			}
		} else if (type == BoundaryType.MARKER) {
			val corners = listOf(
				BlockPos(min.x, y, min.z),
				BlockPos(min.x, y, max.z),
				BlockPos(max.x, y, min.z),
				BlockPos(max.x, y, max.z)
			)

			var markers = 0

			for (corner in corners) {
				val cornerState = level.getBlockState(corner)
				if (cornerState.isBlock(ModBlocks.ENDER_MARKER)) {
					markers++
				}
			}

			if (markers < 3) return false
		}

		return true
	}

	fun trySetBoundaries(level: ServerLevel) {
		val horizontals = Direction.Plane.HORIZONTAL

		for (dir in horizontals) {
			val posThere = worldPosition.relative(dir)
			val stateThere = level.getBlockState(posThere)

			if (stateThere.isBlock(BlockTags.FENCES) && trySetBoundariesFromFences(level, posThere)) {
				boundaryType = BoundaryType.FENCE
				setChanged()
				return
			}

			if (stateThere.isBlock(ModBlocks.ENDER_MARKER) && trySetBoundariesFromMarkers(level, posThere)) {
				boundaryType = BoundaryType.MARKER
				setChanged()
				return
			}
		}

		minBoundary = null
		maxBoundary = null
		targetPos = null
		boundaryType = null

		setChanged()
	}

	private fun trySetBoundariesFromFences(level: ServerLevel, fencePos: BlockPos): Boolean {
		val firstFenceState = level.getBlockState(fencePos)
		if (!firstFenceState.isBlock(BlockTags.FENCES)) return false

		fun isValidFence(level: Level, checkedPos: BlockPos): Boolean {
			val state = level.getBlockState(checkedPos)
			if (!state.isBlock(BlockTags.FENCES)) return false

			val hasProperties = state.hasProperty(FenceBlock.NORTH)
					&& state.hasProperty(FenceBlock.EAST)
					&& state.hasProperty(FenceBlock.SOUTH)
					&& state.hasProperty(FenceBlock.WEST)

			if (!hasProperties) return false

			val adjacentFences = Direction.Plane.HORIZONTAL
				.count { dir ->
					val stateThere = level.getBlockState(checkedPos.relative(dir))
					stateThere.isBlock(BlockTags.FENCES)
				}

			return adjacentFences == 2

		}

		val dirToProperty = mapOf(
			Direction.NORTH to FenceBlock.NORTH,
			Direction.EAST to FenceBlock.EAST,
			Direction.SOUTH to FenceBlock.SOUTH,
			Direction.WEST to FenceBlock.WEST
		)

		val firstDirection = dirToProperty
			.entries
			.firstOrNull { (_, property) -> firstFenceState.getValue(property) }
			?.key
			?: return false

		var currentDirection = firstDirection
		var amountTurns = 0

		val corners = mutableListOf(fencePos)

		val currentPos = fencePos.mutable()
		var iterations = 0

		val maxFences = ServerConfig.CONFIG.enderQuarryFencePerimeterLimit.get()

		while (iterations < maxFences) {
			iterations++

			currentPos.move(currentDirection)
			val currentState = level.getBlockState(currentPos)

			if (!isValidFence(level, currentPos)) return false

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
				amountTurns++

				if (amountTurns > 4) return false

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
		if (minX + 1 == maxX || minZ + 1 == maxZ) return false

		val y = blockPos.y

		val min = BlockPos(minX, y, minZ)
		val max = BlockPos(maxX, y, maxZ)

		minBoundary = min
		maxBoundary = max

		targetPos = min.offset(1, 0, 1)
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
				checkPos.move(dir)

				val checkState = level.getBlockState(checkPos)
				if (checkState.isBlock(ModBlocks.ENDER_MARKER)) {
					markers.add(checkPos.immutable())
					break
				}
			}

			if (markers.size == 3) break
		}

		if (markers.size < 3) return false

		val minX = markers.minOf(BlockPos::getX)
		val minZ = markers.minOf(BlockPos::getZ)
		val maxX = markers.maxOf(BlockPos::getX)
		val maxZ = markers.maxOf(BlockPos::getZ)

		if (minX == maxX || minZ == maxZ) return false
		if (minX + 1 == maxX || minZ + 1 == maxZ) return false

		val y = blockPos.y

		val min = BlockPos(minX, y, minZ)
		val max = BlockPos(maxX, y, maxZ)

		minBoundary = min
		maxBoundary = max

		targetPos = min.offset(1, 0, 1)
		return true
	}

	private fun initFakePlayer() {
		val level = level as? ServerLevel ?: return

		if (this.uuid == null) {
			this.uuid = UUID.randomUUID()
			setChanged()
		}

		val gameProfile = GameProfile(this.uuid, "EU_EnderQuarry")
		val fakePlayer = FakePlayerFactory.get(level, gameProfile)

		fakePlayer.isSilent = true
		fakePlayer.setOnGround(true)

		this.fakePlayer = WeakReference(fakePlayer)
		setChanged()
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putInt(STORED_ENERGY_NBT, energyStorage.energyStored)

		val min = minBoundary
		val max = maxBoundary

		if (min != null && max != null) {
			tag.putLong(MIN_BOUNDARY_NBT, min.asLong())
			tag.putLong(MAX_BOUNDARY_NBT, max.asLong())
		}

		val target = targetPos
		if (target != null) {
			tag.putLong(TARGET_POS_NBT, target.asLong())
		}

		val bType = boundaryType
		if (bType != null) {
			tag.putString(BOUNDARY_TYPE_NBT, bType.id)
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		val storedEnergyTag = tag.get(STORED_ENERGY_NBT)
		if (storedEnergyTag is IntTag) {
			energyStorage.deserializeNBT(registries, storedEnergyTag)
		}

		if (tag.contains(MIN_BOUNDARY_NBT) && tag.contains(MAX_BOUNDARY_NBT)) {
			minBoundary = BlockPos.of(tag.getLong(MIN_BOUNDARY_NBT))
			maxBoundary = BlockPos.of(tag.getLong(MAX_BOUNDARY_NBT))
		}

		if (tag.contains(TARGET_POS_NBT)) {
			targetPos = BlockPos.of(tag.getLong(TARGET_POS_NBT))
		}

		if (tag.contains(BOUNDARY_TYPE_NBT)) {
			val bTypeString = tag.getString(BOUNDARY_TYPE_NBT)
			boundaryType = BoundaryType.entries.firstOrNull { it.id == bTypeString }
		}
	}

	// Syncs with client
	override fun getUpdateTag(pRegistries: HolderLookup.Provider): CompoundTag = saveWithoutMetadata(pRegistries)
	override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

	override fun setChanged() {
		super.setChanged()
		level?.sendBlockUpdated(blockPos, blockState, blockState, Block.UPDATE_ALL_IMMEDIATE)
	}

	companion object {
		const val MIN_BOUNDARY_NBT = "MinBoundary"
		const val MAX_BOUNDARY_NBT = "MaxBoundary"
		const val TARGET_POS_NBT = "TargetPos"
		const val STORED_ENERGY_NBT = "StoredEnergy"
		const val BOUNDARY_TYPE_NBT = "BoundaryType"

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: EnderQuarryBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			}
		}
	}

	enum class BoundaryType(val id: String) : StringRepresentable {
		FENCE("fence"),
		MARKER("marker")
		;

		override fun getSerializedName(): String = id
	}

}