package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

interface GridPowerContribution {
	fun getAmount(): Double
	fun isStillValid(): Boolean

	abstract class HeldItem(
		val gpStack: ItemStack,
		val player: Player
	) : GridPowerContribution
}