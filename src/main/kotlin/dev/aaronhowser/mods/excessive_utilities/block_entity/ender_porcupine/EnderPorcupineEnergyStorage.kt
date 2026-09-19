package dev.aaronhowser.mods.excessive_utilities.block_entity.ender_porcupine

import net.minecraft.core.Direction
import net.neoforged.neoforge.energy.IEnergyStorage

class EnderPorcupineEnergyStorage(
	private val porcupine: EnderPorcupineBlockEntity,
	private val direction: Direction?
) : IEnergyStorage {

	private fun getTarget(): IEnergyStorage? = porcupine.findTargetEnergyHandler(direction)

	override fun receiveEnergy(maxReceive: Int, simulate: Boolean): Int {
		return getTarget()?.receiveEnergy(maxReceive, simulate) ?: 0
	}

	override fun extractEnergy(maxExtract: Int, simulate: Boolean): Int {
		return getTarget()?.extractEnergy(maxExtract, simulate) ?: 0
	}

	override fun getEnergyStored(): Int = getTarget()?.energyStored ?: 0

	override fun getMaxEnergyStored(): Int = getTarget()?.maxEnergyStored ?: 0

	override fun canExtract(): Boolean = getTarget()?.canExtract() ?: false

	override fun canReceive(): Boolean = getTarget()?.canReceive() ?: false

}