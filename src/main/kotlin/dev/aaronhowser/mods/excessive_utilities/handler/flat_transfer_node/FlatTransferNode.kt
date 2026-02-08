package dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import io.netty.buffer.ByteBuf
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.UUIDUtil
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
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

	fun toTag(): Tag {
		val tag = CODEC.encodeStart(NbtOps.INSTANCE, this)
		return tag.result().orElseThrow()
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

		val STREAM_CODEC: StreamCodec<ByteBuf, FlatTransferNode> =
			StreamCodec.composite(
				UUIDUtil.STREAM_CODEC, FlatTransferNode::id,
				BlockPos.STREAM_CODEC, FlatTransferNode::onPos,
				Direction.STREAM_CODEC, FlatTransferNode::facing,
				ByteBufCodecs.BOOL, FlatTransferNode::isItemNode,
				::FlatTransferNode
			)

		fun fromTag(tag: Tag): FlatTransferNode {
			val result = CODEC.parse(NbtOps.INSTANCE, tag)
			return result.result().orElseThrow()
		}
	}

}