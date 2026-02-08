package dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.UUIDUtil
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.ChunkPos
import net.neoforged.neoforge.capabilities.Capabilities
import java.util.*

class FlatTransferNode(
	val id: UUID,
	val onPos: BlockPos,
	val facing: Direction,
	val isItemNode: Boolean
) {

	val chunkPos: ChunkPos = ChunkPos(onPos)
	val targetPos: BlockPos = onPos.relative(facing)

	fun tick(level: ServerLevel) {
		if (!level.isLoaded(onPos) || !level.isLoaded(targetPos)) return

		if (isItemNode) {
			transferItem(level)
		} else {
			transferFluid(level)
		}

	}

	private fun transferItem(level: ServerLevel) {
		val itemHandlerOn = level.getCapability(Capabilities.ItemHandler.BLOCK, onPos, facing) ?: return
		val itemHandlerTarget = level.getCapability(Capabilities.ItemHandler.BLOCK, targetPos, facing.opposite) ?: return
	}

	private fun transferFluid(level: ServerLevel) {
		val fluidHandlerOn = level.getCapability(Capabilities.FluidHandler.BLOCK, onPos, facing) ?: return
		val fluidHandlerTarget = level.getCapability(Capabilities.FluidHandler.BLOCK, targetPos, facing.opposite) ?: return
	}

	companion object {
		val CODEC: Codec<FlatTransferNode> =
			RecordCodecBuilder.create { instance ->
				instance.group(
					UUIDUtil.CODEC
						.fieldOf("id")
						.forGetter(FlatTransferNode::id),
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