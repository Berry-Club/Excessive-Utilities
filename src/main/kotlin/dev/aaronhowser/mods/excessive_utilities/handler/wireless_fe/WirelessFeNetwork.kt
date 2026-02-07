package dev.aaronhowser.mods.excessive_utilities.handler.wireless_fe

import dev.aaronhowser.mods.excessive_utilities.block.entity.WirelessFeBatteryBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block.entity.WirelessFeTransmitterBlockEntity
import java.util.*

class WirelessFeNetwork(
	val ownerUuid: UUID
) {

	private val batteries: MutableSet<WirelessFeBatteryBlockEntity> = mutableSetOf()
	private val transmitters: MutableSet<WirelessFeTransmitterBlockEntity> = mutableSetOf()

}