package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.handler.wireless_fe.WirelessFeNetworkHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.EnergyStorage
import java.util.*

class WirelessFeBatteryBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.WIRELESS_FE_BATTERY.get(), pos, blockState) {

	val energyStorage = EnergyStorage(10)

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

}