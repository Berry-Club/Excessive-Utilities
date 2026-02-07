package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.item.component.OpiniumCoreContentsComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

class OpiniumCoreItem(properties: Properties) : Item(properties) {

	override fun getName(stack: ItemStack): Component {
		return stack
			.get(ModDataComponents.OPINIUM_CORE_CONTENTS)
			?.name
			?: super.getName(stack)
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.component(
					ModDataComponents.OPINIUM_CORE_CONTENTS.get(),
					OpiniumCoreContentsComponent.getDefaultTiers().first()
				)
		}
	}

}