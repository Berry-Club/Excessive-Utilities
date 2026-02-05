package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import java.util.*

class GPGrid(
	val gridId: UUID
) {

	private val gpProducers: MutableSet<GridPowerContribution> = mutableSetOf()
	private val gpConsumers: MutableSet<GridPowerContribution> = mutableSetOf()

	fun getCapacity(): Int = gpProducers.sumOf(GridPowerContribution::getAmount)
	fun getUsage(): Int = gpConsumers.sumOf(GridPowerContribution::getAmount)
	fun isOverloaded(): Boolean = getUsage() > getCapacity()

	fun addProducer(producer: GridPowerContribution) = gpProducers.add(producer)
	fun removeProducer(producer: GridPowerContribution) = gpProducers.remove(producer)

	fun addConsumer(consumer: GridPowerContribution) = gpConsumers.add(consumer)
	fun removeConsumer(consumer: GridPowerContribution) = gpConsumers.remove(consumer)

	fun isEmpty() = gpProducers.isEmpty() && gpConsumers.isEmpty()

	fun tick() {
		gpProducers.removeIf { !it.isStillValid() }
		gpConsumers.removeIf { !it.isStillValid() }
	}

}