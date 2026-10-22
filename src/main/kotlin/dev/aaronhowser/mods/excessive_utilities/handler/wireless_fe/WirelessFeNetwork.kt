package dev.aaronhowser.mods.excessive_utilities.handler.wireless_fe

import dev.aaronhowser.mods.excessive_utilities.block_entity.WirelessFeBatteryBlockEntity
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext
import java.util.*

class WirelessFeNetwork(
	val ownerUuid: UUID
) {

	private val batteries: MutableSet<WirelessFeBatteryBlockEntity> = mutableSetOf()

	fun addBattery(battery: WirelessFeBatteryBlockEntity) = batteries.add(battery)
	fun removeBattery(battery: WirelessFeBatteryBlockEntity) = batteries.remove(battery)

    val energyStorage: EnergyHandler = NetworkEnergyStorage()

    private inner class NetworkEnergyStorage : EnergyHandler {
        override fun getAmountAsLong(): Long = batteries.sumOf { it.energyStorage.amountAsLong }
        override fun getCapacityAsLong(): Long = batteries.sumOf { it.energyStorage.capacityAsLong }
        override fun insert(amount: Int, transaction: TransactionContext): Int {
            require(amount >= 0)
            return 0
        }
        override fun extract(amount: Int, transaction: TransactionContext): Int {
            require(amount >= 0)
            var extracted = 0
            for (battery in batteries) {
                if (extracted == amount) break
                extracted += battery.energyStorage.extract(amount - extracted, transaction)
            }
            return extracted
        }
    }
}
