package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.item.tier.OpiniumTier
import dev.aaronhowser.mods.excessive_utilities.item.tier.UnstableTier
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.component.Unbreakable

class FireAxeItem(properties: Properties) : AxeItem(OpiniumTier, properties) {

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
			.component(DataComponents.UNBREAKABLE, Unbreakable(false))
			.attributes(createAttributes(UnstableTier, 6f, -3.5f))
	}

}