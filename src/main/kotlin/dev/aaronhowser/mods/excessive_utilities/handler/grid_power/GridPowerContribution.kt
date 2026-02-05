package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

interface GridPowerContribution {
	fun getAmount(): Int
	fun isStillValid(): Boolean
}