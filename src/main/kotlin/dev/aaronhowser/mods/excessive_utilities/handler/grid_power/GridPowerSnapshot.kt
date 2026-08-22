package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

data class GridPowerSnapshot(
	val capacity: Double,
	val usage: Double
) {

	val isOverloaded: Boolean
		get() = usage > capacity

}