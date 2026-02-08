package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node.FlatTransferNode
import dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node.FlatTransferNodeHandler
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext
import java.util.*

class FlatTransferNodeItem(properties: Properties) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level

		if (level is ServerLevel) {
			val direction = context.clickedFace
			val pos = context.clickedPos

			val handler = FlatTransferNodeHandler.get(level)

			val node = FlatTransferNode(
				id = UUID.randomUUID(),
				onPos = pos,
				facing = direction,
				isItemNode = true,
			)

			handler.addNode(node)
		}

		return InteractionResult.SUCCESS
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(16)
	}

}