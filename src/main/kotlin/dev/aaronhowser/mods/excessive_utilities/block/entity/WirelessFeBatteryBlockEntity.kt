package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState

class WirelessFeBatteryBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.WIRELESS_FE_BATTERY.get(), pos, blockState) {

	override fun onLoad() {
		super.onLoad()
		val level = this.level
	}

	override fun setRemoved() {
		super.setRemoved()
		val level = this.level
	}

	override fun clearRemoved() {
		super.clearRemoved()
		val level = this.level
	}

}