package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.handler.flat_transfer_node.ChunkFlatTransferNodes
import net.neoforged.neoforge.attachment.AttachmentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NeoForgeRegistries
import java.util.function.Supplier

object ModAttachmentTypes {

	val ATTACHMENT_TYPE_REGISTRY: DeferredRegister<AttachmentType<*>> =
		DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ExcessiveUtilities.MOD_ID)

	val FLAT_TRANSFER_NODES: DeferredHolder<AttachmentType<*>, AttachmentType<ChunkFlatTransferNodes>> =
		ATTACHMENT_TYPE_REGISTRY.register(
			"flat_transfer_nodes",
			Supplier {
				AttachmentType
					.builder(::ChunkFlatTransferNodes)
					.serialize(ChunkFlatTransferNodes.CODEC)
					.build()
			})

}