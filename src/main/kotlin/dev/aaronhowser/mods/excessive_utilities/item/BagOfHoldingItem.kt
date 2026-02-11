package dev.aaronhowser.mods.excessive_utilities.item

import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemContainerContents

class BagOfHoldingItem(properties: Properties) : Item(properties) {

	companion object {
		val DEFAULT_PROPERTIES: Properties =
			Properties()
				.stacksTo(1)
				.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
	}

}