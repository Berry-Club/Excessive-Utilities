package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.block.Block
import java.util.*

class CompressedBlock(
	val compressionLevel: Int,
	properties: Properties
) : Block(properties) {

	override fun appendHoverText(
		stack: ItemStack,
		context: Item.TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val amount = compressionLevel.toLong() * compressionLevel.toLong() * compressionLevel.toLong()
		val formatted = "%,d".format(Locale.US, amount)
		tooltipComponents.add(Component.literal(formatted))
	}

}