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

		val start = target.atY(blockEntity.blockPos.y)

		BeaconRenderer.renderBeaconBeam(
			poseStack,
			bufferSource,
			BeaconRenderer.BEAM_LOCATION,
			partialTick,
			1f,
			gameTime,
			0,
			0,
			0,
			0.2f,
			0.25f
		)

	}
}