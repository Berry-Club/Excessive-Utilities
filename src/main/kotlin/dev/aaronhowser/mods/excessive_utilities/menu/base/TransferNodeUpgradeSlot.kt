package dev.aaronhowser.mods.excessive_utilities.menu.base

import dev.aaronhowser.mods.aaron.menu.components.ContainerSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack

class TransferNodeUpgradeSlot(
	container: Container,
	slotIndex: Int,
	x: Int,
	y: Int
) : ContainerSlot(container, slotIndex, x, y) {

	override fun getMaxStackSize(stack: ItemStack): Int {
		if (stack.isItem(ModItems.DEPTH_FIRST_SEARCH_UPGRADE)
			|| stack.isItem(ModItems.BREADTH_FIRST_SEARCH_UPGRADE)
		) {
			return 1
		}

		return super.getMaxStackSize(stack)
	}

}