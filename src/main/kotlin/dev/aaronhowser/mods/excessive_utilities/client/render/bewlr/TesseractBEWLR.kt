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

		private fun renderCyclingSquares(
			poseStack: PoseStack,
			vertexConsumer: VertexConsumer,
			time: Float
		) {
			val amountSquares = 4
			val phaseStep = Mth.TWO_PI / amountSquares

			val speed = 0.1

			val colors = listOf(
				0xFFFFFF,
				0xFF0000,
				0x00FF00,
				0x0000FF,
			)

			for (i in 0 until amountSquares) {
				val phaseOffset = phaseStep * i
				val dz = 0.5 * (1 + sin(speed * time + phaseOffset))

				poseStack.withPose {
					poseStack.translate(0.0, 0.0, dz)
					poseStack.scale(dz.toFloat(), dz.toFloat(), dz.toFloat())
					renderSquare(
						poseStack,
						vertexConsumer,
						0.5f,
						colors[i],
						0xFF
					)
				}
			}

			renderSquare(poseStack, vertexConsumer, 0.5f, 0, 0xFF)
			poseStack.withPose {
				poseStack.translate(0.0, 0.0, 1.0)
				renderSquare(poseStack, vertexConsumer, 0.5f, 0, 0xFF)
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
