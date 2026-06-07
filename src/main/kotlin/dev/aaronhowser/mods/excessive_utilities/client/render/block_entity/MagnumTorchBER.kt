package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.client.render.AaronRenderTypes
import dev.aaronhowser.mods.excessive_utilities.block_entity.MagnumTorchBlockEntity
import dev.aaronhowser.mods.excessive_utilities.client.render.RenderUtil
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.phys.AABB

class MagnumTorchBER(
	val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<MagnumTorchBlockEntity> {

	override fun render(
		blockEntity: MagnumTorchBlockEntity,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		val player = AaronClientUtil.localPlayer ?: return
		if (!player.isHolding(ModBlocks.MAGNUM_TORCH.asItem())) return

		val linesConsumer = bufferSource.getBuffer(AaronRenderTypes.LINES_THROUGH_WALL_RENDER_TYPE)

		val radius = ServerConfig.CONFIG.magnumTorchRadius.get().toFloat()

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
			1f, 1f, 1f, 0.4f
		)
	}

	override fun getRenderBoundingBox(blockEntity: MagnumTorchBlockEntity): AABB {
		return AABB.INFINITE
	}

	override fun shouldRenderOffScreen(blockEntity: MagnumTorchBlockEntity): Boolean {
		return true
	}

	override fun getViewDistance(): Int {
		return Int.MAX_VALUE
	}

}