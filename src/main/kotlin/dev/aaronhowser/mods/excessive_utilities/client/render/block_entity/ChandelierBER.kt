package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.client.render.AaronRenderUtil
import dev.aaronhowser.mods.excessive_utilities.block_entity.ChandelierBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.phys.AABB

class ChandelierBER(
	val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<ChandelierBlockEntity> {

	override fun render(
		blockEntity: ChandelierBlockEntity,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		val player = AaronClientUtil.localPlayer ?: return
		if (!player.isHolding(ModBlocks.CHANDELIER.asItem())) return

		val radius = ServerConfig.CONFIG.chandelierRadius.get().toDouble()

		val minX = -radius
		val minY = -radius
		val minZ = -radius
		val maxX = radius + 1
		val maxY = radius + 1
		val maxZ = radius + 1

		AaronRenderUtil.renderCubeWireframeThroughWalls(
			poseStack,
			minX, minY, minZ,
			maxX, maxY, maxZ,
			color = 0x66FFFFFF
		)

		AaronRenderUtil.renderCubeWireframeThroughWalls(
			poseStack,
			0.0, 0.0, 0.0,
			1.0, 1.0, 1.0,
			color = 0xFFFCFC95.toInt()
		)
	}

	override fun getRenderBoundingBox(blockEntity: ChandelierBlockEntity): AABB {
		return AABB.INFINITE
	}

	override fun shouldRenderOffScreen(blockEntity: ChandelierBlockEntity): Boolean {
		return true
	}

	override fun getViewDistance(): Int {
		return Int.MAX_VALUE
	}

}
