package dev.aaronhowser.mods.excessive_utilities.client.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import dev.aaronhowser.mods.excessive_utilities.item.AngelRingItem
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.client.ICurioRenderer
import kotlin.math.cos

// TODO: GET NEW TEXTURES!!!!!! DO NOT SHIP WITH THESE!!!!!!!!
class AngelWingRenderer : ICurioRenderer {

	override fun <T : LivingEntity, M : EntityModel<T?>> render(
		stack: ItemStack,
		slotContext: SlotContext,
		poseStack: PoseStack,
		renderLayerParent: RenderLayerParent<T, M>,
		renderTypeBuffer: MultiBufferSource,
		light: Int,
		limbSwing: Float,
		limbSwingAmount: Float,
		partialTicks: Float,
		ageInTicks: Float,
		netHeadYaw: Float,
		headPitch: Float
	) {
		val entity = slotContext.entity
		val wingType = AngelRingItem.PLAYER_WINGS[entity.uuid] ?: return
		if (wingType == AngelRingItem.Type.INVISIBLE) return

		val parentModel = renderLayerParent.model as? HumanoidModel<*> ?: return

		val texture = wingType.textureLocation
		val consumer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(texture))

		val flying = !entity.onGround()

		val angleAmplitude = cos(ageInTicks / if (flying) 4 else 16) + 1
		val angle = 25f + angleAmplitude * if (flying) 20 else 8

		poseStack.withPose {
			parentModel.body.translateAndRotate(poseStack)
			poseStack.translate(0.0, 0.2, 0.0)
			poseStack.mulPose(Axis.YP.rotationDegrees(90f))

			poseStack.withPose {
				poseStack.mulPose(Axis.YP.rotationDegrees(angle))
				renderQuad(poseStack, consumer, light, isFlipped = false)
			}

			poseStack.withPose {
				poseStack.mulPose(Axis.YP.rotationDegrees(-angle))
				renderQuad(poseStack, consumer, light, isFlipped = true)
			}
		}
	}

	private fun renderQuad(
		poseStack: PoseStack,
		vertexConsumer: VertexConsumer,
		packedLight: Int,
		isFlipped: Boolean
	) {
		val pose = poseStack.last()

		val minX = if (isFlipped) 0f else -1f
		val maxX = if (isFlipped) -1f else 0f

		val minY = -0.5f
		val maxY = 0.5f

		val minU = if (isFlipped) 0f else 1f
		val maxU = if (isFlipped) 1f else 0f

		vertexConsumer.addVertex(pose, minX, minY, 0f)
			.setColor(255, 255, 255, 255)
			.setUv(minU, 0f)
			.setOverlay(OverlayTexture.NO_OVERLAY)
			.setLight(packedLight)
			.setNormal(pose, 0f, 0f, 1f)

		vertexConsumer.addVertex(pose, minX, maxY, 0f)
			.setColor(255, 255, 255, 255)
			.setUv(minU, 1f)
			.setOverlay(OverlayTexture.NO_OVERLAY)
			.setLight(packedLight)
			.setNormal(pose, 0f, 0f, 1f)

		vertexConsumer.addVertex(pose, maxX, maxY, 0f)
			.setColor(255, 255, 255, 255)
			.setUv(maxU, 1f)
			.setOverlay(OverlayTexture.NO_OVERLAY)
			.setLight(packedLight)
			.setNormal(pose, 0f, 0f, 1f)

		vertexConsumer.addVertex(pose, maxX, minY, 0f)
			.setColor(255, 255, 255, 255)
			.setUv(maxU, 0f)
			.setOverlay(OverlayTexture.NO_OVERLAY)
			.setLight(packedLight)
			.setNormal(pose, 0f, 0f, 1f)
	}

}