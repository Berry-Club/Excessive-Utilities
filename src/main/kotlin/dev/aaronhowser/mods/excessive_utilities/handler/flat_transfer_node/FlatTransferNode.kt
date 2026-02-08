package dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel

class FlatTransferNode(
	val onPos: BlockPos,
	val facing: Direction,
	val isItemNode: Boolean
) {

	fun tick(level: ServerLevel) {
		// Get item handler it's on
		// Get item handler it's facing
		// If both are present, transfer items from one to the other
	}

	companion object {
		val CODEC: Codec<FlatTransferNode> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					BlockPos.CODEC
						.fieldOf("pos")
						.forGetter(FlatTransferNode::onPos),
					Direction.CODEC
						.fieldOf("facing")
						.forGetter(FlatTransferNode::facing),
					Codec.BOOL
						.optionalFieldOf("is_item_node", true)
						.forGetter(FlatTransferNode::isItemNode)
				).apply(instance, ::FlatTransferNode)
			}
	}

}