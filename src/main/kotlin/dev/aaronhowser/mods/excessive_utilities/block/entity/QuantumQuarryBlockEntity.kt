package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModDimensionProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.common.util.FakePlayer
import net.neoforged.neoforge.energy.EnergyStorage
import java.lang.ref.WeakReference
import java.util.*

class QuantumQuarryBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.QUANTUM_QUARRY.get(), pos, state) {

	private val energyStorage = EnergyStorage(1_000_000)
	private val container = ImprovedSimpleContainer(this, 3)

	private var uuid: UUID? = null
	private var fakePlayer: WeakReference<FakePlayer>? = null

	private var targetChunk: ChunkPos? = null
	private var targetBlockPos: BlockPos? = null

	private fun getItemFilter() = container.getItem(ITEM_FILTER_SLOT_INDEX)
	private fun getEnchantedBook() = container.getItem(ENCHANTED_BOOK_SLOT_INDEX)
	private fun getBiomeFilter() = container.getItem(BIOME_FILTER_SLOT_INDEX)

	private fun serverTick(quarryLevel: ServerLevel, miningDimensionLevel: ServerLevel) {
		if (targetChunk == null || targetBlockPos == null) {
			targetNewChunk(miningDimensionLevel)
		}

		val targetChunk = targetChunk ?: return
		val targetBlockPos = targetBlockPos ?: return

	}

	private var progressThroughBlock = 0.0
	private var feProgress = 0.0

	fun progressMine(level: ServerLevel) {
		if (level.hasNeighborSignal(blockPos)) return
	}

	// Start at the top of the chunk's minimum corner,
	// and then mine downwards until it hits bedrock.
	// Then do +1 to the X, and repeat until at max X.
	// Go back to minX, then do +1 to Z, and repeat until at max Z.
	// Then pick a new chunk and repeat.
	private fun advanceTarget(miningDimensionLevel: ServerLevel) {

	}

	private fun targetNewChunk(miningDimensionLevel: ServerLevel) {
		val previousChunk = targetChunk
		if (previousChunk != null) {
			miningDimensionLevel.setChunkForced(previousChunk.x, previousChunk.z, false)
		}

		var nextChunkPos = ChunkPos(
			miningDimensionLevel.random.nextInt(-100_000, 100_000),
			miningDimensionLevel.random.nextInt(-100_000, 100_000)
		)

		val filteredBiome = getBiomeFilter().get(ModDataComponents.BIOME)
		if (filteredBiome != null) {
			val referencePos = nextChunkPos.getBlockAt(0, 0, 0)
			val nearestBiome = miningDimensionLevel
				.findClosestBiome3d(
					{ holder -> holder.isHolder(filteredBiome) },
					referencePos,
					20_000,
					32,
					64
				)

			if (nearestBiome != null) {
				val biomePos = nearestBiome.first
				nextChunkPos = ChunkPos(biomePos)
			}
		}

		targetChunk = nextChunkPos
		targetBlockPos = BlockPos(
			nextChunkPos.minBlockX,
			miningDimensionLevel.maxBuildHeight,
			nextChunkPos.minBlockZ
		)
	}

	override fun setRemoved() {
		super.setRemoved()
		setChunkForced(level, targetChunk, false)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putInt(STORED_ENERGY_NBT, energyStorage.energyStored)

		val chunkPos = targetChunk
		if (chunkPos != null) {
			tag.putLong(TARGET_CHUNK_POS_NBT, chunkPos.toLong())
		}

		val blockPos = targetBlockPos
		if (blockPos != null) {
			tag.putLong(TARGET_BLOCK_POS_NBT, blockPos.asLong())
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		val storedEnergyTag = tag.get(STORED_ENERGY_NBT)
		if (storedEnergyTag is IntTag) {
			energyStorage.deserializeNBT(registries, storedEnergyTag)
		}

		if (tag.contains(TARGET_CHUNK_POS_NBT)) {
			val chunkPosLong = tag.getLong(TARGET_CHUNK_POS_NBT)
			targetChunk = ChunkPos(chunkPosLong)
		}

		if (tag.contains(TARGET_BLOCK_POS_NBT)) {
			val blockPosLong = tag.getLong(TARGET_BLOCK_POS_NBT)
			targetBlockPos = BlockPos.of(blockPosLong)
		}

	}

	companion object {
		const val TARGET_CHUNK_POS_NBT = "TargetChunkPos"
		const val TARGET_BLOCK_POS_NBT = "TargetBlockPos"
		const val STORED_ENERGY_NBT = "StoredEnergy"
		const val ITEM_FILTER_SLOT_INDEX = 0
		const val ENCHANTED_BOOK_SLOT_INDEX = 1
		const val BIOME_FILTER_SLOT_INDEX = 2

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: QuantumQuarryBlockEntity
		) {
			if (level is ServerLevel) {
				val miningDimensionLevel = level.server
					.getLevel(ModDimensionProvider.QUANTUM_QUARRY_LEVEL)
					?: return

				blockEntity.serverTick(level, miningDimensionLevel)
			}
		}

		private fun setChunkForced(quarryLevel: Level?, chunkPos: ChunkPos?, forced: Boolean) {
			if (quarryLevel !is ServerLevel || chunkPos == null) return
			val miningLevel = quarryLevel.server.getLevel(ModDimensionProvider.QUANTUM_QUARRY_LEVEL) ?: return
			miningLevel.setChunkForced(chunkPos.x, chunkPos.z, forced)
		}
	}

}