package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import dev.aaronhowser.mods.excessive_utilities.block_entity.EnderQuarryBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BeaconRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.phys.AABB

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

		val dx = pos.x - target.x
		val dz = pos.z - target.z
		val dy = pos.y - target.y

		poseStack.withPose {
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
		}
	}

	override fun getRenderBoundingBox(blockEntity: EnderQuarryBlockEntity): AABB {
		return AABB.INFINITE
	}

	override fun shouldRenderOffScreen(blockEntity: EnderQuarryBlockEntity): Boolean {
		return true
	}

	override fun getViewDistance(): Int {
		return Int.MAX_VALUE
	}

}