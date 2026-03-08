package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.IEnergyStorage

class EnergyTrashCanBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.ENERGY_TRASH_CAN.get(), pos, state) {

	private val energyStorage: IEnergyStorage =
		object : IEnergyStorage {
			override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int = toReceive
			override fun extractEnergy(toExtract: Int, simulate: Boolean): Int = 0
			override fun getEnergyStored(): Int = 0
			override fun getMaxEnergyStored(): Int = Int.MAX_VALUE
			override fun canExtract(): Boolean = false
			override fun canReceive(): Boolean = true
		}

	fun getEnergyStorage(direction: Direction): IEnergyStorage = energyStorage


}