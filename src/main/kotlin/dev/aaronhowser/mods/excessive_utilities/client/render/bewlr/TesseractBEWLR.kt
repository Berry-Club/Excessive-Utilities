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
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt

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

			val squares = buildList {
				for (i in 0 until amountSquares) {
					val phaseOffset = phaseStep * i

					val loopAngle = speed * time + phaseOffset
					val rawLoopProgress = (loopAngle + Math.PI / 2) / (Math.PI * 2)
					val loopProgress = (rawLoopProgress - floor(rawLoopProgress)).toFloat()

					val dz = if (loopProgress <= 0.5) {
						2 * loopProgress
					} else {
						2 - 2 * loopProgress
					}

					val scale = when (loopProgress) {
						in 0.0f..0.125f -> {
							val shrinkProgress = Mth.inverseLerp(loopProgress, 0f, 0.125f)
							Mth.lerp(shrinkProgress, 0.75f, 0.5f)
						}

						in 0.125f..0.375f -> 0.5f

						in 0.375f..0.5f -> {
							val growProgress = Mth.inverseLerp(loopProgress, 0.375f, 0.5f)
							Mth.lerp(growProgress, 0.5f, 0.75f)
						}

						// 0.5 = it hit the other side

						else -> {
							val arcProgress = Mth.inverseLerp(loopProgress, 0.5f, 1f)
							Mth.lerp(sin(arcProgress * Math.PI).toFloat(), 0.75f, 1f)
						}
					}

					add(Square(0.5f * scale, dz - 0.5f - 2, colors[i]))
				}
			}

			for (i in squares.indices) {
				val square = squares[i]
				val nextSquare = squares[(i + 1) % squares.size]

				renderSquare(poseStack, vertexConsumer, square.halfSize, square.z, square.color, 0xFF)
				renderCornerArms(poseStack, vertexConsumer, square, nextSquare, square.color, 0xFF)
			}
		}

		private data class Square(
			val halfSize: Float,
			val z: Float,
			val color: Int
		)

		private fun renderCornerArms(
			poseStack: PoseStack,
			vertexConsumer: VertexConsumer,
			square: Square,
			otherSquare: Square,
			color: Int,
			alpha: Int
		) {
			val min = -square.halfSize
			val max = square.halfSize
			val otherMin = -otherSquare.halfSize
			val otherMax = otherSquare.halfSize

			line(poseStack, vertexConsumer, min, min, square.z, otherMin, otherMin, otherSquare.z, color, alpha)
			line(poseStack, vertexConsumer, max, min, square.z, otherMax, otherMin, otherSquare.z, color, alpha)
			line(poseStack, vertexConsumer, max, max, square.z, otherMax, otherMax, otherSquare.z, color, alpha)
			line(poseStack, vertexConsumer, min, max, square.z, otherMin, otherMax, otherSquare.z, color, alpha)
		}

		private fun renderSquare(
			poseStack: PoseStack,
			vertexConsumer: VertexConsumer,
			halfSize: Float,
			z: Float,
			color: Int,
			alpha: Int
		) {
			val min = -halfSize
			val max = halfSize

			line(poseStack, vertexConsumer, min, min, z, max, min, z, color, alpha)
			line(poseStack, vertexConsumer, max, min, z, max, max, z, color, alpha)
			line(poseStack, vertexConsumer, max, max, z, min, max, z, color, alpha)
			line(poseStack, vertexConsumer, min, max, z, min, min, z, color, alpha)
		}

		private fun line(
			poseStack: PoseStack,
			vertexConsumer: VertexConsumer,
			x1: Float,
			y1: Float,
			z1: Float,
			x2: Float,
			y2: Float,
			z2: Float,
			color: Int,
			alpha: Int
		) {
			val pose = poseStack.last()

			val dx = x2 - x1
			val dy = y2 - y1
			val dz = z2 - z1
			val length = sqrt(dx * dx + dy * dy + dz * dz)
			val normalX = if (length == 0f) 0f else dx / length
			val normalY = if (length == 0f) 0f else dy / length
			val normalZ = if (length == 0f) 1f else dz / length

			vertex(pose, vertexConsumer, x1, y1, z1, color, alpha, normalX, normalY, normalZ)
			vertex(pose, vertexConsumer, x2, y2, z2, color, alpha, normalX, normalY, normalZ)
		}

		private fun vertex(
			pose: PoseStack.Pose,
			vertexConsumer: VertexConsumer,
			x: Float,
			y: Float,
			z: Float,
			color: Int,
			alpha: Int,
			normalX: Float,
			normalY: Float,
			normalZ: Float
		) {
			val red = color shr 16 and 255
			val green = color shr 8 and 255
			val blue = color and 255

			vertexConsumer.addVertex(pose, x, y, z)
				.setColor(red, green, blue, alpha)
				.setNormal(pose, normalX, normalY, normalZ)
		}
	}

	object ClientItemExtensions : IClientItemExtensions {
		val BEWLR = TesseractBEWLR()

		override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
			return BEWLR
		}
	}

}
