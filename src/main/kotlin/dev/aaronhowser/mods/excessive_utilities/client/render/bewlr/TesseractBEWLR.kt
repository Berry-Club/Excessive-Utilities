package dev.aaronhowser.mods.excessive_utilities.client.render.bewlr

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import kotlin.math.sin

class TesseractBEWLR : BlockEntityWithoutLevelRenderer(
	Minecraft.getInstance().blockEntityRenderDispatcher,
	Minecraft.getInstance().entityModels
) {

	override fun renderByItem(
		stack: ItemStack,
		displayContext: ItemDisplayContext,
		poseStack: PoseStack,
		buffer: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		val gameTime = AaronClientUtil.localLevel?.gameTime ?: 0
		val time = gameTime + Minecraft.getInstance().timer.gameTimeDeltaTicks
		val vertexConsumer = buffer.getBuffer(RenderType.lines())

		poseStack.withPose {
			poseStack.translate(0.5, 0.5, 0.5)
			poseStack.scale(0.82f, 0.82f, 0.82f)

			if (displayContext == ItemDisplayContext.GUI) {
				poseStack.mulPose(Axis.XP.rotationDegrees(28f))
				poseStack.mulPose(Axis.YP.rotationDegrees(-36f))
			}

			renderCyclingSquares(poseStack, vertexConsumer, time)
		}
	}

	companion object {
		private val COLORS = intArrayOf(
			0xFF2FFFFF.toInt(),
			0xFF7B5CFF.toInt(),
			0xFFFF4EF8.toInt(),
			0xFFFFFFFF.toInt()
		)

		private fun renderCyclingSquares(
			poseStack: PoseStack,
			vertexConsumer: VertexConsumer,
			time: Float
		) {
			for (plane in 0 until 3) {
				poseStack.withPose {
					when (plane) {
						1 -> poseStack.mulPose(Axis.XP.rotationDegrees(90f))
						2 -> poseStack.mulPose(Axis.YP.rotationDegrees(90f))
					}

					val planeSpin = Mth.wrapDegrees(time * (1.2f + plane * 0.35f))
					poseStack.mulPose(Axis.ZP.rotationDegrees(planeSpin))

					for (i in 0 until 4) {
						val phase = time * 0.08f + i * 0.25f + plane * 0.12f
						val pulse = (sin(phase * Mth.TWO_PI) + 1f) * 0.5f
						val halfSize = 0.18f + i * 0.095f + pulse * 0.035f
						val color = COLORS[(i + plane) % COLORS.size]
						val alpha = 190 + (pulse * 65).toInt()

						poseStack.withPose {
							poseStack.mulPose(Axis.ZP.rotationDegrees(i * 45f + time * (2.5f + plane)))
							renderSquare(poseStack, vertexConsumer, halfSize, color, alpha)
						}
					}
				}
			}
		}

		private fun renderSquare(
			poseStack: PoseStack,
			vertexConsumer: VertexConsumer,
			halfSize: Float,
			color: Int,
			alpha: Int
		) {
			val min = -halfSize
			val max = halfSize

			line(poseStack, vertexConsumer, min, min, max, min, color, alpha)
			line(poseStack, vertexConsumer, max, min, max, max, color, alpha)
			line(poseStack, vertexConsumer, max, max, min, max, color, alpha)
			line(poseStack, vertexConsumer, min, max, min, min, color, alpha)
		}

		private fun line(
			poseStack: PoseStack,
			vertexConsumer: VertexConsumer,
			x1: Float,
			y1: Float,
			x2: Float,
			y2: Float,
			color: Int,
			alpha: Int
		) {
			val pose = poseStack.last()
			vertex(pose, vertexConsumer, x1, y1, color, alpha)
			vertex(pose, vertexConsumer, x2, y2, color, alpha)
		}

		private fun vertex(
			pose: PoseStack.Pose,
			vertexConsumer: VertexConsumer,
			x: Float,
			y: Float,
			color: Int,
			alpha: Int
		) {
			val red = color shr 16 and 255
			val green = color shr 8 and 255
			val blue = color and 255

			vertexConsumer.addVertex(pose, x, y, 0f)
				.setColor(red, green, blue, alpha)
				.setNormal(pose, 0f, 0f, 1f)
		}
	}

	object ClientItemExtensions : IClientItemExtensions {
		val BEWLR = TesseractBEWLR()

		override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
			return BEWLR
		}
	}

}
