package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.client.render.AaronRenderUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getPovResult
import dev.aaronhowser.mods.excessive_utilities.block_entity.EnderCollectorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.client.render.RenderUtil
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.entity.ai.attributes.Attributes

class EnderCollectorBER(
	val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<EnderCollectorBlockEntity> {

	override fun render(
		blockEntity: EnderCollectorBlockEntity,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		val player = AaronClientUtil.localPlayer ?: return
		val lookingAt = player.getPovResult(player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE))

		val pos = blockEntity.blockPos
		if (lookingAt.blockPos != pos) return

		val radius = blockEntity.radius

		AaronRenderUtil.renderCubeWireframeThroughWalls(
			poseStack,
			-radius, -radius, -radius,
			radius + 1, radius + 1, radius + 1,
			0xFFFFFFFF.toInt()
		)

	}

}