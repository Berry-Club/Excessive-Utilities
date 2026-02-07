package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.util.UUID

class WirelessFeBatteryBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.WIRELESS_FE_BATTERY.get(), pos, blockState) {

	var ownerUuid: UUID? = null

	override fun onLoad() {
		super.onLoad()
		val level = this.level
		val owner = ownerUuid
		if (level != null && owner != null) {
			WirelessFeBatteryHandler.addBattery(level, owner, this)
		}
	}

	override fun setRemoved() {
		super.setRemoved()
		val level = this.level
		val owner = ownerUuid
		if (level != null && owner != null) {
			WirelessFeBatteryHandler.removeBattery(level, owner, this)
		}
	}

	override fun clearRemoved() {
		super.clearRemoved()
		val level = this.level
		val owner = ownerUuid
		if (level != null && owner != null) {
			WirelessFeBatteryHandler.addBattery(level, owner, this)
		}
	}

}