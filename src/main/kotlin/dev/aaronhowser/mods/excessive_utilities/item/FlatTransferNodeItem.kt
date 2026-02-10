package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.entity.FlatTransferNodeEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.context.UseOnContext

class FlatTransferNodeItem(
	properties: Properties,
	val isItemNode: Boolean
) : Item(properties) {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level

		if (level is ServerLevel) {
			val direction = context.clickedFace
			val pos = context.clickedPos

			val node = FlatTransferNodeEntity.place(level, pos, direction, isItemNode)
			val success = node != null

			if (success) {
				return InteractionResult.SUCCESS
			}
		}

		return InteractionResult.PASS
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(16)
	}

}