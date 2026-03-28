package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.RGBA
import dev.aaronhowser.mods.excessive_utilities.block_entity.QedBlockEntity
import dev.aaronhowser.mods.excessive_utilities.client.render.RenderUtil
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
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

		val radius = ServerConfig.CONFIG.qedRadius.get().toFloat()
		val minX = -radius
		val minY = -radius
		val minZ = -radius
		val maxX = radius + 1
		val maxY = radius + 1
		val maxZ = radius + 1

		val color = RGBA.fromInt(0x12BCA3FF)
		val (rInt, gInt, bInt, aInt) = color

		val r = rInt / 255f
		val g = gInt / 255f
		val b = bInt / 255f
		val a = aInt / 255f

		RenderUtil.box(
			poseStack,
			linesConsumer,
			minX, minY, minZ,
			maxX, maxY, maxZ,
			r, g, b, a
		)

	}

	override fun shouldRenderOffScreen(blockEntity: QedBlockEntity): Boolean {
		return true
	}

}