package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import java.util.*

class PlayerGrid(
	val playerUuid: UUID
) {

	private val gpProducers: MutableSet<GridPowerContribution> = mutableSetOf()
	private val gpConsumers: MutableSet<GridPowerContribution> = mutableSetOf()

	fun getProducers(): Set<GridPowerContribution> = gpProducers.toSet()
	fun getConsumers(): Set<GridPowerContribution> = gpConsumers.toSet()

	fun getCapacity(): Double = gpProducers.sumOf { it.getAmount() }
	fun getUsage(): Double = gpConsumers.sumOf { it.getAmount() }

	fun addProducer(producer: GridPowerContribution) = gpProducers.add(producer)

	fun addConsumer(consumer: GridPowerContribution) = gpConsumers.add(consumer)

	fun isEmpty() = gpProducers.isEmpty() && gpConsumers.isEmpty()

	fun tick() {
		gpProducers.removeIf { !it.isStillValid() }
		gpConsumers.removeIf { !it.isStillValid() }
	}
}