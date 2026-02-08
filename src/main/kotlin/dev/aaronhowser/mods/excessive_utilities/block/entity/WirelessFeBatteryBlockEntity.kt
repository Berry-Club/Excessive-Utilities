package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getUuidOrNull
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.putUuidIfNotNull
import dev.aaronhowser.mods.excessive_utilities.handler.wireless_fe.WirelessFeNetworkHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage
import java.util.*

class WirelessFeBatteryBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.WIRELESS_FE_BATTERY.get(), pos, blockState) {

	val energyStorage = EnergyStorage(15_000)
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

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		tag.putUuidIfNotNull(OWNER_UUID_NBT, ownerUuid)
		tag.put(STORED_ENERGY_NBT, energyStorage.serializeNBT(registries))
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		ownerUuid = tag.getUuidOrNull(OWNER_UUID_NBT)
		val storedEnergyTag = tag.get(STORED_ENERGY_NBT)
		if (storedEnergyTag is IntTag) {
			energyStorage.deserializeNBT(registries, storedEnergyTag)
		}
	}

	fun getEnergyCapability(direction: Direction?): IEnergyStorage = energyStorage

	companion object {
		const val OWNER_UUID_NBT = "OwnerUUID"
		const val STORED_ENERGY_NBT = "StoredEnergy"
	}

}