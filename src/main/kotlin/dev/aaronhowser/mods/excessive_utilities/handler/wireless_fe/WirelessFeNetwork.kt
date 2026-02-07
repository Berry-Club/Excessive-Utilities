package dev.aaronhowser.mods.excessive_utilities.handler.wireless_fe

import dev.aaronhowser.mods.excessive_utilities.block.entity.WirelessFeBatteryBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.WirelessFeTransmitterBlockEntity
import net.neoforged.neoforge.energy.IEnergyStorage
import java.util.*

class WirelessFeNetwork(
	val ownerUuid: UUID
) {

	private val batteries: MutableSet<WirelessFeBatteryBlockEntity> = mutableSetOf()
	private val transmitters: MutableSet<WirelessFeTransmitterBlockEntity> = mutableSetOf()

	fun addBattery(battery: WirelessFeBatteryBlockEntity) = batteries.add(battery)
	fun removeBattery(battery: WirelessFeBatteryBlockEntity) = batteries.remove(battery)

	fun addTransmitter(transmitter: WirelessFeTransmitterBlockEntity) = transmitters.add(transmitter)
	fun removeTransmitter(transmitter: WirelessFeTransmitterBlockEntity) = transmitters.remove(transmitter)

	private val energyStorage: IEnergyStorage =
		object : IEnergyStorage {

			override fun receiveEnergy(toReceive: Int, simulate: Boolean): Int = 0
			override fun canReceive(): Boolean = false

			override fun getEnergyStored(): Int = batteries.sumOf { it.energyStorage.energyStored }
			override fun getMaxEnergyStored(): Int = batteries.sumOf { it.energyStorage.maxEnergyStored }
			override fun canExtract(): Boolean = batteries.any { it.energyStorage.canExtract() }

			override fun extractEnergy(toExtract: Int, simulate: Boolean): Int {
				var amountExtracted = 0

				for (battery in batteries) {
					if (amountExtracted >= toExtract) break

					val extracted = battery.energyStorage.extractEnergy(toExtract - amountExtracted, simulate)
					amountExtracted += extracted
				}

				return amountExtracted
			}
		}

}