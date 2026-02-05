package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

object ClientGridPower {

	var capacity: Double = 0.0
	var usage: Double = 0.0
	val isOverloaded: Boolean get() = usage > capacity

}