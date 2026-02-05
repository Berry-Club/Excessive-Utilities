package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

object ClientGridPower {

	var capacity: Int = 0
	var usage: Int = 0
	val isOverloaded: Boolean get() = usage > capacity

}