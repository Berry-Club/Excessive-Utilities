package dev.aaronhowser.mods.excessive_utilities.block_entity

import com.mojang.authlib.GameProfile
import dev.aaronhowser.mods.aaron.block_entity.SyncingBlockEntity
import dev.aaronhowser.mods.aaron.container.ContainerContainer
import dev.aaronhowser.mods.aaron.container.ExtractOnlyInvWrapper
import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveEnergy
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.EnderQuarryUpgradeType
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.LongArrayTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.tags.BlockTags
import net.minecraft.util.Mth
import net.minecraft.util.StringRepresentable
import net.minecraft.world.ContainerHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.FenceBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.common.util.FakePlayerFactory
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.ItemHandlerHelper
import java.lang.ref.WeakReference
import java.util.*

//TODO:
// Block States for inactive, active, and finished
// Make sure it works while the markers are unloaded, and make it load the chunk the target is in
class EnderQuarryBlockEntity(
	pos: BlockPos,
	state: BlockState
) : SyncingBlockEntity(ModBlockEntityTypes.ENDER_QUARRY.get(), pos, state), ContainerContainer {

	override val syncImmediately: Boolean = true

	private val energyStorage: EnergyStorage = EnergyStorage(1_000_000)
	private val bufferContainer: ImprovedSimpleContainer = ImprovedSimpleContainer(this, 27)
	private val bufferItemHandler: ExtractOnlyInvWrapper = ExtractOnlyInvWrapper(bufferContainer)

	override fun getContainers(): List<ImprovedSimpleContainer> = listOf(bufferContainer)

	private var fakePlayer: WeakReference<FakePlayer>? = null

	private val upgradePositions: MutableSet<BlockPos> = mutableSetOf()
	fun addUpgrade(upgradeBe: EnderQuarryUpgradeBlockEntity): Boolean {
		if (upgradeBe.blockPos in upgradePositions) return true

		val currentUpgrades = getUpgrades()
		val newUpgradeType = upgradeBe.upgradeType
		for (upgrade in currentUpgrades) {
			if (upgrade == newUpgradeType) return false
			if (upgrade in newUpgradeType.getIncompatibleUpgrades()) return false
		}

		upgradePositions.add(upgradeBe.blockPos)
		setChanged()
		return true
	}

	var boundaryType: BoundaryType? = null
		private set

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

	fun getUpgrades(): Set<EnderQuarryUpgradeType> {
		val level = level ?: return emptySet()
		val upgrades = mutableSetOf<EnderQuarryUpgradeType>()

		val iterator = upgradePositions.iterator()
		while (iterator.hasNext()) {
			val pos = iterator.next()

			val be = level.getBlockEntity(pos)
			if (be is EnderQuarryUpgradeBlockEntity && be.getQuarryPos() == blockPos) {
				upgrades.add(be.upgradeType)
			} else {
				iterator.remove()
				setChanged()
			}
		}

		return upgrades
	}

	private fun serverTick(level: ServerLevel) {
		if (fakePlayer?.get() == null) {
			initFakePlayer()
		}

		pushOutItems(level)
		if (!bufferContainer.isEmpty) return

		progressMine(level)
	}

	private fun pushOutItems(level: ServerLevel) {
		if (bufferContainer.isEmpty) return

		val adjacentItemHandlers = Direction.entries
			.mapNotNull { level.getCapability(Capabilities.ItemHandler.BLOCK, blockPos.relative(it), it.opposite) }

		for (destinationHandler in adjacentItemHandlers) {
			if (bufferContainer.isEmpty) break

			for (slot in 0 until bufferItemHandler.slots) {
				val stack = bufferItemHandler.getStackInSlot(slot)
				if (stack.isEmpty) continue

				val simRemainder = ItemHandlerHelper.insertItemStacked(destinationHandler, stack, true)
				val amountAccepted = stack.count - simRemainder.count
				if (amountAccepted > 0) {
					val extracted = bufferItemHandler.extractItem(slot, amountAccepted, false)
					ItemHandlerHelper.insertItemStacked(destinationHandler, extracted, false)
				}
			}
		}
	}

	private var progressThroughBlock = 0.0
	private var feProgress = 0.0

	private fun progressMine(level: ServerLevel) {
		if (level.hasNeighborSignal(blockPos)) return

		confirmTarget(level) ?: return

		var fePerBlock = ServerConfig.CONFIG.enderQuarryFePerBlock.get()
		for (upgrade in getUpgrades()) {
			fePerBlock *= upgrade.feMultiplierGetter.asDouble
		}

		if (energyStorage.energyStored < fePerBlock) return

		var blocksPerTick = ServerConfig.CONFIG.enderQuarryBlocksPerTick.get()
		when {
			EnderQuarryUpgradeType.SPEED_ONE in getUpgrades() -> blocksPerTick *= ServerConfig.CONFIG.eqSpeedOneSpeedMultiplier.get()
			EnderQuarryUpgradeType.SPEED_TWO in getUpgrades() -> blocksPerTick *= ServerConfig.CONFIG.eqSpeedTwoSpeedMultiplier.get()
			EnderQuarryUpgradeType.SPEED_THREE in getUpgrades() -> blocksPerTick *= ServerConfig.CONFIG.eqSpeedThreeSpeedMultiplier.get()
		}

		progressThroughBlock += blocksPerTick

		while (progressThroughBlock > 1.0) {
			progressThroughBlock -= 1.0

			val target = targetPos ?: break
			actuallyMineBlock(level, target)
			advanceTargetPos(level)

			feProgress += fePerBlock
			val feToExtract = Mth.floor(feProgress)
			if (feToExtract > 0) {
				energyStorage.extractEnergy(feToExtract, false)
				feProgress -= feToExtract
			}
		}
	}

	private fun confirmTarget(level: ServerLevel): BlockPos? {
		if (targetPos == null) {
			if (!checkBoundaries(level)) {
				trySetBoundaries(level)
			}

			advanceTargetPos(level)
		}

		return targetPos
	}

	private fun actuallyMineBlock(level: ServerLevel, target: BlockPos) {
		val drops = gatherDrops(level, target)
		for (drop in drops) {
			bufferContainer.addItem(drop)
		}

		val hasWorldHoleUpgrade = getUpgrades().contains(EnderQuarryUpgradeType.WORLD_HOLE)

		if (hasWorldHoleUpgrade) {
			level.removeBlock(target, false)
		} else {
			level.setBlock(target, Blocks.DIRT.defaultBlockState(), Block.UPDATE_ALL)
		}
	}

	private fun gatherDrops(level: ServerLevel, target: BlockPos): List<ItemStack> {
		val targetState = level.getBlockState(target)

		var tool = Items.NETHERITE_PICKAXE.defaultInstance

		if (targetState.requiresCorrectToolForDrops()) {
			val shovel = Items.NETHERITE_SHOVEL.defaultInstance
			if (shovel.isCorrectToolForDrops(targetState)) {
				tool = shovel
			} else {
				val axe = Items.NETHERITE_AXE.defaultInstance
				if (axe.isCorrectToolForDrops(targetState)) {
					tool = axe
				}
			}
		}

		val upgrades = getUpgrades()
		val enchantmentRegistry = level.registryAccess().registryOrThrow(Registries.ENCHANTMENT)

		if (EnderQuarryUpgradeType.SILK_TOUCH in upgrades) {
			tool.enchant(enchantmentRegistry.getHolderOrThrow(Enchantments.SILK_TOUCH), 1)
		} else {
			val fortuneLevel = when {
				EnderQuarryUpgradeType.FORTUNE_THREE in upgrades -> 3
				EnderQuarryUpgradeType.FORTUNE_TWO in upgrades -> 2
				EnderQuarryUpgradeType.FORTUNE_ONE in upgrades -> 1
				else -> 0
			}

			if (fortuneLevel > 0) {
				tool.enchant(enchantmentRegistry.getHolderOrThrow(Enchantments.FORTUNE), fortuneLevel)
			}
		}

		val lootParams = LootParams.Builder(level)
			.withParameter(LootContextParams.ORIGIN, target.center)
			.withParameter(LootContextParams.TOOL, tool)
			.withParameter(LootContextParams.BLOCK_STATE, targetState)
			.withOptionalParameter(LootContextParams.THIS_ENTITY, fakePlayer?.get())
			.withOptionalParameter(LootContextParams.BLOCK_ENTITY, level.getBlockEntity(target))

		return targetState.getDrops(lootParams)
	}

	/**
	 * @return true if the Quarry should try to mine the block, false if it should skip it and move on to the next one
	 */
	private fun canQuarryMineBlock(level: ServerLevel, target: BlockPos): Boolean {
		val skipCobble = !getUpgrades().contains(EnderQuarryUpgradeType.WORLD_HOLE)
		val state = level.getBlockState(target)

		if (state.isAir
			|| state.hasBlockEntity()
			|| (skipCobble && state.isBlock(Blocks.COBBLESTONE))
			|| state.isBlock(ModBlockTagsProvider.ENDER_QUARRY_BLACKLIST)
		) return false

		val unbreakable = state.getDestroySpeed(level, target) < 0
		if (unbreakable) return false

		val fakePlayer = fakePlayer?.get() ?: return false
		return level.mayInteract(fakePlayer, target)
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
		advanceTargetPos(level)
		return true
	}

	private fun trySetBoundariesFromMarkers(level: ServerLevel, markerPos: BlockPos): Boolean {
		val firstMarkerState = level.getBlockState(markerPos)
		if (!firstMarkerState.isBlock(ModBlocks.ENDER_MARKER)) return false

		val markers = mutableSetOf(markerPos)
		val searchDistance = ServerConfig.CONFIG.enderQuarryMarkerSearchDistance.get()

		// Skip this one, because that's where the quarry is
		val directionToQuarry = Direction.fromDelta(blockPos.x - markerPos.x, 0, blockPos.z - markerPos.z) ?: return false
		val directions = Direction.Plane.HORIZONTAL.toMutableSet()
		directions.remove(directionToQuarry)

		val checkPos = markerPos.mutable()
		for (i in 0 until searchDistance) {
			for (dir in directions) {
				checkPos.set(markerPos).move(dir, i)

				val checkState = level.getBlockState(checkPos)
				if (checkState.isBlock(ModBlocks.ENDER_MARKER)) {
					markers.add(checkPos.immutable())
					directions.remove(dir)
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

		val gameProfile = GameProfile(UUID.randomUUID(), "EU_EnderQuarry")
		val fakePlayer = FakePlayerFactory.get(level, gameProfile)

		fakePlayer.isSilent = true
		fakePlayer.setPos(blockPos.bottomCenter)
		fakePlayer.setOnGround(true)

		this.fakePlayer = WeakReference(fakePlayer)
		setChanged()
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		ContainerHelper.saveAllItems(tag, bufferContainer.items, registries)

		tag.saveEnergy(STORED_ENERGY_NBT, energyStorage, registries)

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

		val upgradePositionLongs = upgradePositions.map(BlockPos::asLong).toLongArray()
		tag.put(UPGRADE_POSITIONS_NBT, LongArrayTag(upgradePositionLongs))
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		ContainerHelper.loadAllItems(tag, bufferContainer.items, registries)

		tag.saveEnergy(STORED_ENERGY_NBT, energyStorage, registries)

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

		val upgradePositionsTag = tag.getLongArray(UPGRADE_POSITIONS_NBT)
		upgradePositions.clear()
		for (posLong in upgradePositionsTag) {
			upgradePositions.add(BlockPos.of(posLong))
		}

	}

	companion object {
		const val MIN_BOUNDARY_NBT = "MinBoundary"
		const val MAX_BOUNDARY_NBT = "MaxBoundary"
		const val TARGET_POS_NBT = "TargetPos"
		const val STORED_ENERGY_NBT = "StoredEnergy"
		const val BOUNDARY_TYPE_NBT = "BoundaryType"
		const val UPGRADE_POSITIONS_NBT = "UpgradePositions"

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

		fun getEnergyCapability(quarry: EnderQuarryBlockEntity, direction: Direction?): IEnergyStorage {
			return quarry.energyStorage
		}
	}

	enum class BoundaryType(val id: String) : StringRepresentable {
		FENCE("fence"),
		MARKER("marker")
		;

		override fun getSerializedName(): String = id
	}

}