package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveEnergy
import dev.aaronhowser.mods.excessive_utilities.handler.wireless_fe.WirelessFeNetworkHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.UUIDUtil
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import java.util.*

class WirelessFeBatteryBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.WIRELESS_FE_BATTERY.get(), pos, blockState) {

	val energyStorage = SimpleEnergyHandler(15_000)
	var ownerUuid: UUID? = null

	override fun onLoad() {
		super.onLoad()
		val level = this.level
		val owner = ownerUuid
		if (level is ServerLevel && owner != null) {
			WirelessFeNetworkHandler.get(level).getNetwork(owner).addBattery(this)
		}
	}

	override fun setRemoved() {
		super.setRemoved()
		val level = this.level
		val owner = ownerUuid
		if (level is ServerLevel && owner != null) {
			WirelessFeNetworkHandler.get(level).getNetwork(owner).removeBattery(this)
		}
	}

	override fun clearRemoved() {
		super.clearRemoved()
		val level = this.level
		val owner = ownerUuid
		if (level is ServerLevel && owner != null) {
			WirelessFeNetworkHandler.get(level).getNetwork(owner).addBattery(this)
		}
	}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)
		if (ownerUuid != null) tag.putIntArray(OWNER_UUID_NBT, UUIDUtil.uuidToIntArray(ownerUuid!!))
		tag.saveEnergy(STORED_ENERGY_NBT, energyStorage)
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)

		val ownerInts = tag.getIntArray(OWNER_UUID_NBT).orElse(IntArray(0))
		ownerUuid = if (ownerInts.size == 4) UUIDUtil.uuidFromIntArray(ownerInts) else null
		tag.loadEnergy(STORED_ENERGY_NBT, energyStorage)
	}

	fun getEnergyCapability(direction: Direction?): EnergyHandler = energyStorage

	companion object {
		const val OWNER_UUID_NBT = "OwnerUUID"
		const val STORED_ENERGY_NBT = "StoredEnergy"
	}

}
