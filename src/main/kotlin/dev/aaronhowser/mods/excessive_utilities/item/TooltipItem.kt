package dev.aaronhowser.mods.excessive_utilities.item

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import java.util.function.Consumer

interface TooltipItem {
	fun appendTooltip(
		stack: ItemStack,
		context: Item.TooltipContext,
		display: TooltipDisplay,
		builder: Consumer<Component>,
		tooltipFlag: TooltipFlag
	)
}
