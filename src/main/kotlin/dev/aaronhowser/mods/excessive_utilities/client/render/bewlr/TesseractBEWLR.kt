package dev.aaronhowser.mods.excessive_utilities.client.render.bewlr

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import com.mojang.math.Axis
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.client.render.AaronRenderTypes
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions
import kotlin.math.floor
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
			val amountSquares = 8
			val phaseStep = Mth.TWO_PI / amountSquares

			val speed = 0.025

			val colors = listOf(
				0xFFFFFF,
				0xFF0000,
				0x00FF00,
				0x0000FF,
				0x00FFFF,
				0xFF00FF,
				0xFFFF00,
				0xF0F0F0,
				0x0F0F0F
			)

			for (i in 0 until amountSquares) {
				val phaseOffset = phaseStep * i

				val loopAngle = speed * time + phaseOffset
				val dz = (1 + sin(loopAngle).toFloat()) / 2

				val rawLoopProgress = (loopAngle + Math.PI / 2) / (Math.PI * 2)
				val loopProgress = (rawLoopProgress - floor(rawLoopProgress)).toFloat()

				val scale = when (loopProgress) {
					in 0.0f..0.125f -> {
						val shrinkProgress = Mth.inverseLerp(loopProgress, 0f, 0.125f)
						Mth.lerp(shrinkProgress, 1f, 0.5f)
					}

					in 0.125f..0.375f -> 0.5f

					in 0.375f..0.5f -> {
						val growProgress = Mth.inverseLerp(loopProgress, 0.375f, 0.5f)
						Mth.lerp(growProgress, 0.5f, 1f)
					}

					else -> 1f
				}

				poseStack.withPose {
					poseStack.translate(0.0, 0.0, dz.toDouble() - 0.5)
					renderSquare(poseStack, vertexConsumer, 0.5f * scale, colors[i], 0xFF)
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
