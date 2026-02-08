package dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction

class FlatTransferNode(
	val onPos: BlockPos,
	val facing: Direction,
	val isItemNode: Boolean
) {

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