package dev.aaronhowser.mods.excessive_utilities.client.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import dev.aaronhowser.mods.excessive_utilities.item.AngelRingItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModAttachmentTypes
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState
import net.minecraft.client.renderer.texture.OverlayTexture
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.client.ICurioRenderer
import kotlin.math.cos

class AngelWingRenderer : ICurioRenderer {

	override fun <T : LivingEntityRenderState, M : EntityModel<in T>> render(
		stack: ItemStack,
		slotContext: SlotContext,
		poseStack: PoseStack,
		submitNodeCollector: SubmitNodeCollector,
		light: Int,
		renderState: T,
		renderLayerParent: RenderLayerParent<T, M>,
		context: EntityRendererProvider.Context,
		yRotation: Float,
		xRotation: Float
	) {
		val wingType = slotContext.entity.getData(ModAttachmentTypes.WINGS)
		if (wingType == AngelRingItem.Type.INVISIBLE) return

		val parentModel = renderLayerParent.model
		if (parentModel !is HumanoidModel<*>) return

		val texture = wingType.textureLocation
		val flying = !slotContext.entity.onGround()

		val angleAmplitude = cos(renderState.ageInTicks / if (flying) 4 else 16) + 1
		val angle = 25f + angleAmplitude * if (flying) 20 else 8

		poseStack.withPose {
			parentModel.body.translateAndRotate(poseStack)
			poseStack.translate(0.0, 0.2, 0.15)
			poseStack.mulPose(Axis.YP.rotationDegrees(90f))

			poseStack.withPose {
				poseStack.mulPose(Axis.YP.rotationDegrees(angle))
				submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(texture)) { pose, consumer -> renderQuad(pose, consumer, light, isFlipped = false) }
			}

			poseStack.withPose {
				poseStack.mulPose(Axis.YP.rotationDegrees(-angle))
				submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.entityCutout(texture)) { pose, consumer -> renderQuad(pose, consumer, light, isFlipped = true) }
			}
		}
	}

	private fun renderQuad(
		pose: PoseStack.Pose,
		vertexConsumer: VertexConsumer,
		packedLight: Int,
		isFlipped: Boolean
	) {
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
