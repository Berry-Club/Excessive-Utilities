package dev.aaronhowser.mods.excessive_utilities.menu.base

import net.minecraft.world.Container
import net.minecraft.world.inventory.Slot

open class ContainerSlot(
	container: Container,
	slotIndex: Int,
	x: Int,
	y: Int
) : Slot(container, slotIndex, x, y)
