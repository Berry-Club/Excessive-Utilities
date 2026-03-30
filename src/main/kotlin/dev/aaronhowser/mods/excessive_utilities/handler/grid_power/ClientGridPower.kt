package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import java.text.DecimalFormat

object ClientGridPower {

	var capacity: Double = 0.0
	var usage: Double = 0.0
	val isOverloaded: Boolean get() = usage > capacity

	val RENDER_FORMAT = DecimalFormat("#,##0.##")
	fun format(amount: Double): String = RENDER_FORMAT.format(amount)

}