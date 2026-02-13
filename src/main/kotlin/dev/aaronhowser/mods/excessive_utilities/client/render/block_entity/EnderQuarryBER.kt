package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.excessive_utilities.block.entity.EnderQuarryBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BeaconRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider

class EnderQuarryBER(
	val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<EnderQuarryBlockEntity> {

	override fun render(
		blockEntity: EnderQuarryBlockEntity,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		val target = blockEntity.targetPos ?: return
		val gameTime = blockEntity.level?.gameTime ?: 0

		val pos = blockEntity.blockPos
		val start = target.atY(pos.y)

		val dx = pos.x - start.x
		val dz = pos.z - start.z
		val dy = pos.y - start.y

		poseStack.pushPose()
		poseStack.translate(-dx.toDouble(), -dy.toDouble(), -dz.toDouble())

		for (i in 0 until dy + 1) {
			BeaconRenderer.renderBeaconBeam(
				poseStack,
				bufferSource,
				BeaconRenderer.BEAM_LOCATION,
				partialTick,
				1f,
				gameTime,
				i,
				1,
				0xFFFFFFFF.toInt(),
				0.2f,
				0.25f
			)
		}

		poseStack.popPose()
	}
}