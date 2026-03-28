package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.excessive_utilities.block_entity.QedBlockEntity
import dev.aaronhowser.mods.excessive_utilities.client.render.RenderUtil
import dev.aaronhowser.mods.excessive_utilities.client.render.WandRenderer
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider

class QedBER(
	val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<QedBlockEntity> {

	override fun render(
		blockEntity: QedBlockEntity,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		val player = AaronClientUtil.localPlayer ?: return
		if (!player.isHolding(ModBlocks.ENDER_FLUX_CRYSTAL.asItem())) return

		val linesConsumer = bufferSource.getBuffer(RenderType.lines())

		val radius = 9f
		val minX = -radius
		val minY = -radius
		val minZ = -radius
		val maxX = radius + 1
		val maxY = radius + 1
		val maxZ = radius + 1

		RenderUtil.box(
			poseStack,
			linesConsumer,
			minX, minY, minZ,
			maxX, maxY, maxZ,
			1f, 1f, 1f, 1f
		)

	}

	override fun shouldRenderOffScreen(blockEntity: QedBlockEntity): Boolean {
		return true
	}

}