package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import dev.aaronhowser.mods.excessive_utilities.packet.server_to_client.UpdateGridPowerPacket
import net.minecraft.server.level.ServerLevel
import java.util.*

class GPGrid(
	val ownerUuid: UUID
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

	private var capacityLastTick = 0
	private var usageLastTick = 0

	fun updateClient(level: ServerLevel) {
		val capacityNow = getCapacity()
		val usageNow = getUsage()

		val changed = capacityNow != capacityLastTick || usageNow != usageLastTick
		if (changed) {
			val player = level.server.playerList.getPlayer(ownerUuid)
			if (player != null) {
				val packet = UpdateGridPowerPacket(capacityNow, usageNow)
				packet.messagePlayer(player)
			}
		}

		capacityLastTick = capacityNow
		usageLastTick = usageNow
	}

	fun tick(level: ServerLevel) {
		gpProducers.removeIf { !it.isStillValid() }
		gpConsumers.removeIf { !it.isStillValid() }

		updateClient(level)
	}

}