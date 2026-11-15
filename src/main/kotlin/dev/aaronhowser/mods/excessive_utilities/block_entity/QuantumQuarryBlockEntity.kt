package dev.aaronhowser.mods.excessive_utilities.block_entity

import com.mojang.authlib.GameProfile
import dev.aaronhowser.mods.aaron.container.ContainerContainer
import dev.aaronhowser.mods.aaron.container.ExtractOnlyResourceHandler
import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.MachineWithBuffer
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBiomeTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.ItemFilterItem
import dev.aaronhowser.mods.excessive_utilities.menu.quantum_quarry.QuantumQuarryMenu
import dev.aaronhowser.mods.excessive_utilities.registry.*
import dev.aaronhowser.mods.excessive_utilities.worldgen.quantum_quarry.QuantumQuarryBiomeSource
import dev.aaronhowser.mods.excessive_utilities.worldgen.quantum_quarry.QuantumQuarryDimension
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Holder
import net.minecraft.core.UUIDUtil
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.resources.ResourceKey
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.enchantment.EnchantmentHelper
import net.minecraft.world.item.enchantment.Enchantments
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.common.util.FakePlayerFactory
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import java.lang.ref.WeakReference
import java.util.*

class QuantumQuarryBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.QUANTUM_QUARRY.get(), pos, state), ContainerContainer, MachineWithBuffer, MenuProvider {

	private val energyStorage = SimpleEnergyHandler(1_000_000)
	fun getEnergyCapability(direction: Direction?): EnergyHandler = energyStorage

	private val bufferContainer: ImprovedSimpleContainer = ImprovedSimpleContainer(this, 27)
	private val bufferItemHandler: ResourceHandler<ItemResource> = ExtractOnlyResourceHandler(bufferContainer)
	private val upgradesContainer: ImprovedSimpleContainer = QuantumQuarryUpgradeContainer()

	override fun getContainers(): List<Container> = listOf(bufferContainer, upgradesContainer)

	override fun clearContent() {
		super.clearContent()
		energyStorage.extractEnergy(energyStorage.amountAsInt, false)
	}

	private var fakePlayer: WeakReference<FakePlayer>? = null
	private var fakePlayerUuid = UUID.randomUUID()

	private var targetChunk: ChunkPos? = null
	private var targetBlockPos: BlockPos? = null
	private var targetDimension: QuantumQuarryDimension? = null
	private var targetBiomeKey: ResourceKey<Biome>? = null

	private var activeTicketChunk: ChunkPos? = null
	private var activeTicketDimension: QuantumQuarryDimension? = null

	private var biomeFilterChanged = false
	private var loadingAdditionalData = false
	private var hasLegacyForcedTarget = false

	private var amountBlocksBroken: Int = 0

	private fun getItemFilter(): ItemStack = upgradesContainer.getItem(ITEM_FILTER_SLOT_INDEX)
	private fun getEnchantedBook(): ItemStack = upgradesContainer.getItem(ENCHANTED_BOOK_SLOT_INDEX)
	private fun getBiomeFilter(): ItemStack = upgradesContainer.getItem(BIOME_FILTER_SLOT_INDEX)

	private val actuatorPositions: List<BlockPos> = Direction.entries.map { direction -> this.blockPos.relative(direction) }

	private fun serverTick(quarryLevel: ServerLevel) {
		if (hasLegacyForcedTarget) {
			removeLegacyForcedTarget(quarryLevel)
		}

		if (!hasActuators()) return

		if (fakePlayer?.get() == null) {
			initFakePlayer()
		}

		pushOutItems(quarryLevel, bufferContainer, bufferItemHandler)
		if (!bufferContainer.isEmpty) return

		val hasSignal = actuatorPositions.any { pos -> quarryLevel.hasNeighborSignal(pos) }
		if (hasSignal) return

		if (biomeFilterChanged) {
			biomeFilterChanged = false
			discardTarget(quarryLevel)
		}

		if (targetChunk == null || targetBlockPos == null || targetDimension == null || targetBiomeKey == null) {
			targetNewChunk(quarryLevel)
		}

		val miningDimensionLevel = activateTarget(quarryLevel) ?: return
		progressMine(miningDimensionLevel)
	}

	private fun hasActuators(): Boolean {
		val level = level ?: return false
		for (pos in actuatorPositions) {
			val stateThere = level.getBlockState(pos)
			if (!stateThere.isBlock(ModBlocks.QUANTUM_QUARRY_ACTUATOR)) return false
		}

		return true
	}

	override fun collectReceivingItemHandlers(level: ServerLevel): Collection<ResourceHandler<ItemResource>> {
		val adjacentItemHandlers = mutableSetOf<ResourceHandler<ItemResource>>()

		for (actuatorPos in actuatorPositions) {
			for (direction in Direction.entries) {
				val adjacentPos = actuatorPos.relative(direction)
				if (adjacentPos == blockPos) continue

				val itemHandler = level.getCapability(Capabilities.Item.BLOCK, adjacentPos, direction.opposite)
				if (itemHandler != null) adjacentItemHandlers.add(itemHandler)
			}
		}

		return adjacentItemHandlers
	}

	private var progressThroughBlock = 0.0
	private var feProgress = 0.0
	private var targetScansRemainingThisTick = 0

	private fun progressMine(miningDimensionLevel: ServerLevel) {
		val fePerBlock = ServerConfig.CONFIG.quantumQuarryFePerBlock.get()
		if (energyStorage.amountAsInt < fePerBlock) return

		val blocksPerTick = ServerConfig.CONFIG.quantumQuarryBlocksPerTick.get()
		progressThroughBlock += blocksPerTick

		val enchantedBook = getEnchantedBook()
		if (enchantedBook.isNotEmpty()) {
			val storedEnchants = EnchantmentHelper.getEnchantmentsForCrafting(enchantedBook)
			val enchantmentLookup = miningDimensionLevel.registryAccess().lookupOrThrow(Registries.ENCHANTMENT)
			val efficiencyLevel = storedEnchants.getLevel(enchantmentLookup.getOrThrow(Enchantments.EFFICIENCY))
			if (efficiencyLevel > 0) {
				progressThroughBlock *= 1.0 + (efficiencyLevel * 0.25)
			}
		}

		targetScansRemainingThisTick = 4096

		while (progressThroughBlock >= 1.0) {
			val target = getMineableTarget(miningDimensionLevel) ?: return

			val drops = gatherDrops(miningDimensionLevel, target)
			for (drop in drops) {
				bufferContainer.addItem(drop)
			}

			progressThroughBlock -= 1.0
			amountBlocksBroken++

			feProgress += fePerBlock
			val feToExtract = Mth.floor(feProgress)
			if (feToExtract > 0) {
				energyStorage.extractEnergy(feToExtract, false)
				feProgress -= feToExtract
			}

			advanceTarget(miningDimensionLevel)
		}
	}

	private fun gatherDrops(miningDimensionLevel: ServerLevel, target: BlockPos): List<ItemStack> {
		val targetState = miningDimensionLevel.getBlockState(target)

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

		val enchantments = EnchantmentHelper.getEnchantmentsForCrafting(getEnchantedBook())
		EnchantmentHelper.setEnchantments(tool, enchantments)

		val lootParams = LootParams.Builder(miningDimensionLevel)
			.withParameter(LootContextParams.ORIGIN, target.center)
			.withParameter(LootContextParams.TOOL, tool)
			.withParameter(LootContextParams.BLOCK_STATE, targetState)
			.withOptionalParameter(LootContextParams.THIS_ENTITY, fakePlayer?.get())
			.withOptionalParameter(LootContextParams.BLOCK_ENTITY, miningDimensionLevel.getBlockEntity(target))

		val unfilteredDrops = targetState.getDrops(lootParams)

		val itemFilter = getItemFilter()
		if (itemFilter.isEmpty) return unfilteredDrops

		return ItemFilterItem.filterItems(itemFilter, unfilteredDrops)
	}

	private fun canQuarryMineBlock(miningDimensionLevel: ServerLevel, blockPos: BlockPos): Boolean {
		val state = miningDimensionLevel.getBlockState(blockPos)
		if (state.isAir) return false
		val unbreakable = state.getDestroySpeed(miningDimensionLevel, blockPos) < 0
		return !unbreakable
	}

	private fun getMineableTarget(miningDimensionLevel: ServerLevel): BlockPos? {
		val target = targetBlockPos ?: return null
		if (canQuarryMineBlock(miningDimensionLevel, target)) return target

		if (!advanceTarget(miningDimensionLevel, includeCurrent = true)) return null
		return targetBlockPos
	}

	// Start at the top of the chunk's minimum corner,
	// and then mine downwards until it hits bedrock.
	// Then do +1 to the X, and repeat until at max X.
	// Go back to minX, then do +1 to Z, and repeat until at max Z.
	// Then pick a new chunk and repeat.
	private fun advanceTarget(
		miningDimensionLevel: ServerLevel,
		includeCurrent: Boolean = false
	): Boolean {
		val currentChunk = targetChunk ?: return false
		val currentTarget = targetBlockPos ?: return false

		val xBounds = currentChunk.minBlockX..currentChunk.maxBlockX
		val zBounds = currentChunk.minBlockZ..currentChunk.maxBlockZ
		val topY = miningDimensionLevel.maxY

		fun moveNextPos(currentPos: BlockPos.MutableBlockPos): Boolean {
			when {
				currentPos.y > miningDimensionLevel.minY -> {
					currentPos.move(Direction.DOWN)
				}

				currentPos.x < currentChunk.maxBlockX -> {
					currentPos.set(currentPos.x + 1, topY, currentPos.z)
				}

				currentPos.z < currentChunk.maxBlockZ -> {
					currentPos.set(currentChunk.minBlockX, topY, currentPos.z + 1)
				}

				else -> {
					return false
				}
			}

			return true
		}

		val nextPos = currentTarget.mutable()
		if (!includeCurrent && !moveNextPos(nextPos)) {
			targetBlockPos = null
			return false
		}

		while (targetScansRemainingThisTick > 0) {
			targetScansRemainingThisTick--

			if (nextPos.x !in xBounds || nextPos.z !in zBounds) {
				break
			}

			if (canQuarryMineBlock(miningDimensionLevel, nextPos)) {
				targetBlockPos = nextPos
				return true
			}

			if (!moveNextPos(nextPos)) {
				targetBlockPos = null
				return false
			}
		}

		targetBlockPos = nextPos
		return false
	}

	private fun targetNewChunk(quarryLevel: ServerLevel) {
		discardTarget(quarryLevel)

		val filteredBiome = getBiomeFilter().get(ModDataComponents.BIOME)
		val miningDimension = if (filteredBiome == null) {
			getDimensionForLevel(quarryLevel)
		} else {
			getDimensionForBiome(filteredBiome)
		}

		val targetBiome = filteredBiome ?: chooseRandomTargetBiome(quarryLevel, miningDimension) ?: return
		val biomeKey = targetBiome.key ?: return
		val miningDimensionLevel = quarryLevel.server.getLevel(miningDimension.levelKey) ?: return

		var attempts = 0
		while (attempts < 100) {
			val nextChunkPos = ChunkPos(
				miningDimensionLevel.random.nextInt(-MAX_TARGET_COORDINATE, MAX_TARGET_COORDINATE),
				miningDimensionLevel.random.nextInt(-MAX_TARGET_COORDINATE, MAX_TARGET_COORDINATE)
			)

			if (QuantumQuarryBiomeSource.claim(miningDimension, nextChunkPos, fakePlayerUuid, targetBiome)) {
				targetChunk = nextChunkPos
				targetBlockPos = BlockPos(
					nextChunkPos.minBlockX,
					miningDimensionLevel.maxY,
					nextChunkPos.minBlockZ
				)
				targetDimension = miningDimension
				targetBiomeKey = biomeKey
				addTargetTicket(miningDimensionLevel, miningDimension, nextChunkPos)
				setChanged()
				return
			}

			attempts++
		}
	}

	private fun chooseRandomTargetBiome(
		quarryLevel: ServerLevel,
		dimension: QuantumQuarryDimension
	): Holder<Biome>? {
		val biomeRegistry = quarryLevel.registryAccess().lookupOrThrow(Registries.BIOME)
		val choices = mutableListOf<Holder<Biome>>()
		val biomeTag = biomeRegistry.get(dimension.biomeTag).orElse(null) ?: return null

		for (biome in biomeTag) {
			if (biome.isHolder(ModBiomeTagsProvider.BIOME_MARKER_BLACKLIST)) continue
			if (biome.isHolder(ModBiomeTagsProvider.QUANTUM_QUARRY_RANDOM_BLACKLIST)) continue

			choices.add(biome)
		}

		if (choices.isEmpty()) return null
		return choices[quarryLevel.random.nextInt(choices.size)]
	}

	private fun getDimensionForLevel(level: ServerLevel): QuantumQuarryDimension {
		return when (level.dimension()) {
			Level.NETHER -> QuantumQuarryDimension.NETHER
			Level.END -> QuantumQuarryDimension.END
			else -> QuantumQuarryDimension.OVERWORLD
		}
	}

	private fun getDimensionForBiome(biome: Holder<Biome>): QuantumQuarryDimension {
		return when {
			biome.isHolder(QuantumQuarryDimension.NETHER.biomeTag) -> QuantumQuarryDimension.NETHER
			biome.isHolder(QuantumQuarryDimension.END.biomeTag)
					|| biome.isHolder(Biomes.THE_VOID) -> QuantumQuarryDimension.END

			else -> QuantumQuarryDimension.OVERWORLD
		}
	}

	private fun activateTarget(quarryLevel: ServerLevel): ServerLevel? {
		val chunkPos = targetChunk ?: return null
		val dimension = targetDimension ?: return null
		val biomeKey = targetBiomeKey ?: return null
		val miningDimensionLevel = quarryLevel.server.getLevel(dimension.levelKey) ?: return null

		if (activeTicketChunk == chunkPos && activeTicketDimension == dimension) {
			return miningDimensionLevel
		}

		releaseTargetTicket(quarryLevel)

		val biomeRegistry = quarryLevel.registryAccess().lookupOrThrow(Registries.BIOME)
		val biome = biomeRegistry.get(biomeKey).orElse(null) ?: return null
		if (!QuantumQuarryBiomeSource.claim(dimension, chunkPos, fakePlayerUuid, biome)) {
			discardTarget(quarryLevel)
			return null
		}

		addTargetTicket(miningDimensionLevel, dimension, chunkPos)
		return miningDimensionLevel
	}

	private fun addTargetTicket(
		miningDimensionLevel: ServerLevel,
		dimension: QuantumQuarryDimension,
		chunkPos: ChunkPos
	) {
		miningDimensionLevel.chunkSource.addTicketWithRadius(
			ModTicketTypes.QUANTUM_QUARRY.get(),
			chunkPos,
			QUARRY_TICKET_RADIUS
		)

		activeTicketChunk = chunkPos
		activeTicketDimension = dimension
	}

	private fun releaseTargetTicket(quarryLevel: ServerLevel) {
		val chunkPos = activeTicketChunk ?: return
		val dimension = activeTicketDimension ?: return
		val miningDimensionLevel = quarryLevel.server.getLevel(dimension.levelKey)

		miningDimensionLevel?.chunkSource?.removeTicketWithRadius(
			ModTicketTypes.QUANTUM_QUARRY.get(),
			chunkPos,
			QUARRY_TICKET_RADIUS
		)

		QuantumQuarryBiomeSource.release(dimension, chunkPos, fakePlayerUuid)
		activeTicketChunk = null
		activeTicketDimension = null
	}

	private fun discardTarget(quarryLevel: ServerLevel) {
		releaseTargetTicket(quarryLevel)

		targetChunk = null
		targetBlockPos = null
		targetDimension = null
		targetBiomeKey = null
		setChanged()
	}

	private fun removeLegacyForcedTarget(quarryLevel: ServerLevel) {
		hasLegacyForcedTarget = false

		val chunkPos = targetChunk
		val miningDimensionLevel = quarryLevel.server.getLevel(QuantumQuarryDimension.OVERWORLD.levelKey)
		if (chunkPos != null && miningDimensionLevel != null) {
			miningDimensionLevel.setChunkForced(chunkPos.x, chunkPos.z, false)
		}

		discardTarget(quarryLevel)
	}

	private fun initFakePlayer() {
		val level = level as? ServerLevel ?: return

		val gameProfile = GameProfile(fakePlayerUuid, "EU_QuantumQuarry")
		val fakePlayer = FakePlayerFactory.get(level, gameProfile)

		fakePlayer.isSilent = true
		fakePlayer.setPos(blockPos.bottomCenter)
		fakePlayer.setOnGround(true)

		this.fakePlayer = WeakReference(fakePlayer)
		setChanged()
	}

	override fun setRemoved() {
		val quarryLevel = level as? ServerLevel
		if (quarryLevel != null) {
			releaseTargetTicket(quarryLevel)
		}

		super.setRemoved()
	}

	private val containerData: ContainerData = QuantumQuarryContainerData()

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)

		tag.saveEnergy(STORED_ENERGY_NBT, energyStorage)

		tag.child(BUFFER_CONTAINER_NBT).saveItems(bufferContainer)

		tag.child(UPGRADE_CONTAINER_NBT).saveItems(upgradesContainer)

		val chunkPos = targetChunk
		if (chunkPos != null) {
			tag.putLong(TARGET_CHUNK_POS_NBT, chunkPos.pack())
		}

		val blockPos = targetBlockPos
		if (blockPos != null) {
			tag.putLong(TARGET_BLOCK_POS_NBT, blockPos.asLong())
		}

		val dimension = targetDimension
		if (dimension != null) {
			tag.putString(TARGET_DIMENSION_NBT, dimension.serializedName)
		}

		val biomeKey = targetBiomeKey
		if (biomeKey != null) {
			tag.putString(TARGET_BIOME_NBT, biomeKey.identifier().toString())
		}

		tag.putInt(AMOUNT_BLOCKS_BROKEN_NBT, amountBlocksBroken)
		tag.store(FAKE_PLAYER_UUID_NBT, UUIDUtil.CODEC, fakePlayerUuid)
		tag.putBoolean(TARGET_USES_REGION_TICKET_NBT, !hasLegacyForcedTarget)
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)
		loadingAdditionalData = true

		tag.loadEnergy(STORED_ENERGY_NBT, energyStorage)

		tag.child(BUFFER_CONTAINER_NBT).ifPresent { it.loadItems(bufferContainer) }

		tag.child(UPGRADE_CONTAINER_NBT).ifPresent { it.loadItems(upgradesContainer) }

		if (tag.getLong(TARGET_CHUNK_POS_NBT).isPresent) {
			val chunkPosLong = tag.getLongOr(TARGET_CHUNK_POS_NBT, 0L)
			targetChunk = ChunkPos.unpack(chunkPosLong)
		}

		if (tag.getLong(TARGET_BLOCK_POS_NBT).isPresent) {
			val blockPosLong = tag.getLongOr(TARGET_BLOCK_POS_NBT, 0L)
			targetBlockPos = BlockPos.of(blockPosLong)
		}

		if (tag.getString(TARGET_DIMENSION_NBT).isPresent) {
			targetDimension = QuantumQuarryDimension.fromSerializedName(tag.getStringOr(TARGET_DIMENSION_NBT, ""))
		}

		if (tag.getString(TARGET_BIOME_NBT).isPresent) {
			val biomeId = Identifier.tryParse(tag.getStringOr(TARGET_BIOME_NBT, ""))
			if (biomeId != null) {
				targetBiomeKey = ResourceKey.create(Registries.BIOME, biomeId)
			}
		}

		amountBlocksBroken = tag.getIntOr(AMOUNT_BLOCKS_BROKEN_NBT, 0)

		tag.read(FAKE_PLAYER_UUID_NBT, UUIDUtil.CODEC).ifPresent { fakePlayerUuid = it }

		hasLegacyForcedTarget = targetChunk != null && !tag.getBooleanOr(TARGET_USES_REGION_TICKET_NBT, false)
		activeTicketChunk = null
		activeTicketDimension = null
		biomeFilterChanged = false
		loadingAdditionalData = false
	}

	override fun getDisplayName(): Component = blockState.block.name

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return QuantumQuarryMenu(containerId, playerInventory, upgradesContainer, containerData)
	}

	companion object {
		const val TARGET_CHUNK_POS_NBT = "TargetChunkPos"
		const val TARGET_BLOCK_POS_NBT = "TargetBlockPos"
		const val TARGET_DIMENSION_NBT = "TargetDimension"
		const val TARGET_BIOME_NBT = "TargetBiome"
		const val STORED_ENERGY_NBT = "StoredEnergy"
		const val BUFFER_CONTAINER_NBT = "Buffer"
		const val UPGRADE_CONTAINER_NBT = "Upgrades"
		const val AMOUNT_BLOCKS_BROKEN_NBT = "AmountBlocksBroken"
		const val FAKE_PLAYER_UUID_NBT = "FakePlayerUuid"
		const val TARGET_USES_REGION_TICKET_NBT = "TargetUsesRegionTicket"

		const val UPGRADE_CONTAINER_SIZE = 3
		const val ITEM_FILTER_SLOT_INDEX = 0
		const val ENCHANTED_BOOK_SLOT_INDEX = 1
		const val BIOME_FILTER_SLOT_INDEX = 2

		const val CONTAINER_DATA_SIZE = 7
		const val CURRENT_ENERGY_DATA_INDEX = 0
		const val TARGET_X_DATA_INDEX = 1
		const val TARGET_Y_DATA_INDEX = 2
		const val TARGET_Z_DATA_INDEX = 3
		const val PROGRESS_PERCENT_DATA_INDEX = 4
		const val AMOUNT_BLOCKS_BROKEN_DATA_INDEX = 5
		const val BIOME_ID_DATA_INDEX = 6

		const val MAX_TARGET_COORDINATE = 100_000
		const val QUARRY_TICKET_RADIUS = 2

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: QuantumQuarryBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			}
		}
	}

	private inner class QuantumQuarryContainerData : ContainerData {

		override fun get(index: Int): Int {
			return when (index) {
				CURRENT_ENERGY_DATA_INDEX -> energyStorage.amountAsInt
				TARGET_X_DATA_INDEX -> targetBlockPos?.x ?: 0
				TARGET_Y_DATA_INDEX -> targetBlockPos?.y ?: 0
				TARGET_Z_DATA_INDEX -> targetBlockPos?.z ?: 0
				PROGRESS_PERCENT_DATA_INDEX -> Mth.floor(progressThroughBlock * 100)
				AMOUNT_BLOCKS_BROKEN_DATA_INDEX -> amountBlocksBroken
				BIOME_ID_DATA_INDEX -> getTargetBiomeId()
				else -> 0
			}
		}

		override fun set(index: Int, value: Int) {
			// No need to implement since the client doesn't write to these
		}

		override fun getCount(): Int = CONTAINER_DATA_SIZE

		private fun getTargetBiomeId(): Int {
			val level = level
			val biomeKey = targetBiomeKey
			if (biomeKey == null || level !is ServerLevel) return 0

			val registry = level.registryAccess().lookupOrThrow(Registries.BIOME)
			val biome = registry.get(biomeKey).orElse(null) ?: return 0
			return registry.getId(biome.value())
		}

	}

	private inner class QuantumQuarryUpgradeContainer : ImprovedSimpleContainer(this, UPGRADE_CONTAINER_SIZE) {

		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			return when (slot) {
				ITEM_FILTER_SLOT_INDEX -> stack.isItem(ModItems.ITEM_FILTER)
				ENCHANTED_BOOK_SLOT_INDEX -> stack.isItem(Items.ENCHANTED_BOOK)
				BIOME_FILTER_SLOT_INDEX -> stack.isItem(ModItems.BIOME_MARKER)
				else -> false
			}
		}

		override fun setItem(index: Int, stack: ItemStack) {
			val previousBiome = if (index == BIOME_FILTER_SLOT_INDEX) {
				getItem(index).get(ModDataComponents.BIOME)
			} else {
				null
			}

			super.setItem(index, stack)

			if (index != BIOME_FILTER_SLOT_INDEX || loadingAdditionalData) return

			val currentBiome = stack.get(ModDataComponents.BIOME)
			if (previousBiome != currentBiome) {
				biomeFilterChanged = true
				setChanged()
			}
		}

	}

}