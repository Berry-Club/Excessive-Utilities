package dev.aaronhowser.mods.excessive_utilities.client.render.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.aaronhowser.mods.excessive_utilities.entity.FlatTransferNodeEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.client.renderer.texture.TextureAtlas
import net.minecraft.core.Direction
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemDisplayContext

class FlatTransferNodeEntityRenderer(
	context: EntityRendererProvider.Context
) : EntityRenderer<FlatTransferNodeEntity>(context) {

	private val itemRenderer: ItemRenderer = context.itemRenderer

	override fun render(
		entity: FlatTransferNodeEntity,
		entityYaw: Float,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int
	) {
		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight)

		poseStack.pushPose()

		val aiming = entity.aiming

		when (aiming) {
			Direction.DOWN -> {
				poseStack.translate(0f, 0f, -0.225f)
				poseStack.mulPose(Axis.XP.rotationDegrees(90f))
			}

			Direction.UP -> {
				poseStack.translate(0f, 1f, -0.225f)
				poseStack.mulPose(Axis.XP.rotationDegrees(90f))
			}

			else -> {
				poseStack.mulPose(Axis.YP.rotationDegrees(-entityYaw))
				poseStack.translate(0f, 0.25f, 0.5f)
			}
		}

		poseStack.scale(1.8f, 1.8f, 1.8f)


		itemRenderer.renderStatic(
			entity.getAsItemStack(),
			ItemDisplayContext.GROUND,
			0xFFFFFF,
			OverlayTexture.NO_OVERLAY,
			poseStack,
			bufferSource,
			entity.level(),
			entity.id
		)

		poseStack.popPose()
	}

	override fun getTextureLocation(entity: FlatTransferNodeEntity): ResourceLocation = TextureAtlas.LOCATION_BLOCKS

}