package dev.aaronhowser.mods.excessive_utilities.block.base.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getUuidOrNull
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage
import java.util.*

abstract class CompressibleFeGeneratorBlockEntity(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(type, pos, blockState) {

	abstract val tier: Int

	var ownerUuid: UUID? = null

	protected val energyStorage = EnergyStorage(10_000)

	protected var fePerTick: Int = 0
		set(value) {
			if (field == value) return
			field = value
			setChanged()
		}

	protected var burnTimeRemaining: Int = 0
		set(value) {
			if (field == value) return
			field = value
			setChanged()
		}

	protected open fun serverTick(level: ServerLevel) {
		for (i in 0 until tier) generatorTick(level)
	}

	protected open fun generatorTick(level: ServerLevel) {
		if (burnTimeRemaining <= 0) {
			fePerTick = 0
			tryStartBurning(level)

			if (burnTimeRemaining <= 0) return
		}

		generateEnergy(level)
	}

	protected abstract fun tryStartBurning(level: ServerLevel)

	protected open fun generateEnergy(level: ServerLevel) {
		val remainingCapacity = energyStorage.maxEnergyStored - energyStorage.energyStored
		if (remainingCapacity <= 0) return

		val energyToGenerate = fePerTick.coerceAtMost(remainingCapacity)
		energyStorage.receiveEnergy(energyToGenerate, false)

		burnTimeRemaining--
		setChanged()
	}

	protected open fun clientTick(level: Level) {}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putInt(BURN_TIME_REMAINING_NBT, burnTimeRemaining)
		tag.putInt(FE_PER_TICK_NBT, fePerTick)
		tag.put(STORED_ENERGY_NBT, energyStorage.serializeNBT(registries))

		val uuid = ownerUuid
		if (uuid != null) {
			tag.putUUID(OWNER_UUID_NBT, uuid)
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		burnTimeRemaining = tag.getInt(BURN_TIME_REMAINING_NBT)
		fePerTick = tag.getInt(FE_PER_TICK_NBT)
		ownerUuid = tag.getUuidOrNull(OWNER_UUID_NBT)

		val storedEnergyTag = tag.get(STORED_ENERGY_NBT)
		if (storedEnergyTag is IntTag) {
			energyStorage.deserializeNBT(registries, storedEnergyTag)
		}
	}

	companion object {
		const val OWNER_UUID_NBT = "OwnerUUID"
		const val BURN_TIME_REMAINING_NBT = "BurnTimeRemaining"
		const val FE_PER_TICK_NBT = "FePerTick"
		const val STORED_ENERGY_NBT = "StoredEnergy"

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: CompressibleFeGeneratorBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			} else {
				blockEntity.clientTick(level)
			}
		}

		fun getEnergyCapability(transmitter: CompressibleFeGeneratorBlockEntity, direction: Direction?): IEnergyStorage? {
			return transmitter.energyStorage
		}
	}

}