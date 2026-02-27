package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModDimensionProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
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

	private var uuid: UUID? = null
	private var fakePlayer: WeakReference<FakePlayer>? = null

	private var targetChunk: ChunkPos? = null
	private var targetBlockPos: BlockPos? = null

	private fun serverTick(quarryLevel: ServerLevel, miningDimensionLevel: ServerLevel) {

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
	}

}