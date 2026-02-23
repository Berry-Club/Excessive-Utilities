package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

class CreativeEnergySourceBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.CREATIVE_ENERGY_SOURCE.get(), pos, blockState) {

	val energyStorage: IEnergyStorage =
		object : IEnergyStorage {
			override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int = 0
			override fun extractEnergy(toExtract: Int, simulate: Boolean): Int = toExtract
			override fun getEnergyStored(): Int = Int.MAX_VALUE
			override fun getMaxEnergyStored(): Int = Int.MAX_VALUE
			override fun canExtract(): Boolean = true
			override fun canReceive(): Boolean = false
		}

	private fun serverTick(level: ServerLevel) {

	}

	companion object {
		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: CreativeEnergySourceBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			}
		}
	}

}