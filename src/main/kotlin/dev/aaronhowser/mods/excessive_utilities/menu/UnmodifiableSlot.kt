package dev.aaronhowser.mods.excessive_utilities.menu

import net.minecraft.world.Container
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class UnmodifiableSlot(
	container: Container,
	slot: Int,
	x: Int,
	y: Int
) : Slot(container, slot, x, y) {

	override fun mayPlace(stack: ItemStack): Boolean = false

	override fun mayPickup(player: Player): Boolean = false
}