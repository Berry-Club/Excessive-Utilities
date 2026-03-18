package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.world.item.DyeColor
import net.minecraft.world.item.Item

class PaintbrushItem(properties: Properties) : Item(properties) {

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties =
			{
				Properties()
					.stacksTo(1)
					.component(ModDataComponents.COLOR, DyeColor.WHITE)
			}
	}

}