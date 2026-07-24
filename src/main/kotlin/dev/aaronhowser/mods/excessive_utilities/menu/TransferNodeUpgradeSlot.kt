package dev.aaronhowser.mods.excessive_utilities.menu

import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack

class TransferNodeUpgradeSlot(
	container: Container,
	private val slotIndex: Int,
	x: Int,
	y: Int
) : FilteredSlot(container, slotIndex, x, y) {

	override fun mayPlace(stack: ItemStack): Boolean {
		val isUpgrade = stack.isItem(ModItemTagsProvider.TRANSFER_NODE_UPGRADES)
				|| stack.isItem(ModItemTagsProvider.RETRIEVAL_NODE_UPGRADES)
		return isUpgrade && container.canPlaceItem(slotIndex, stack)
	}

	override fun getMaxStackSize(stack: ItemStack): Int {
		if (stack.isItem(ModItems.DEPTH_FIRST_SEARCH_UPGRADE)
			|| stack.isItem(ModItems.BREADTH_FIRST_SEARCH_UPGRADE)
		) {
			return 1
		}

		return super.getMaxStackSize(stack)
	}

}