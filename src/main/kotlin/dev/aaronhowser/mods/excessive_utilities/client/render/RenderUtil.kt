package dev.aaronhowser.mods.excessive_utilities.client.render

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import kotlin.math.sqrt

object RenderUtil {

	fun box(
		poseStack: PoseStack,
		vertexConsumer: VertexConsumer,
		minX: Float, minY: Float, minZ: Float,
		maxX: Float, maxY: Float, maxZ: Float,
		r: Float, g: Float, b: Float, a: Float
	) {
		val pose = poseStack.last()

		val nnn = floatArrayOf(minX, minY, minZ) // Negative X, Negative Y, Negative Z
		val nnp = floatArrayOf(minX, minY, maxZ) // Negative X, Negative Y, Positive Z
		val npn = floatArrayOf(minX, maxY, minZ) // Negative X, Positive Y, Negative Z
		val npp = floatArrayOf(minX, maxY, maxZ) // Negative X, Positive Y, Positive Z
		val pnn = floatArrayOf(maxX, minY, minZ) // Positive X, Negative Y, Negative Z
		val pnp = floatArrayOf(maxX, minY, maxZ) // Positive X, Negative Y, Positive Z
		val ppn = floatArrayOf(maxX, maxY, minZ) // Positive X, Positive Y, Negative Z
		val ppp = floatArrayOf(maxX, maxY, maxZ) // Positive X, Positive Y, Positive Z

		val lines = listOf(
			// Bottom face
			nnn to nnp,
			nnp to pnp,
			pnp to pnn,
			pnn to nnn,

			// Top face
			npn to npp,
			npp to ppp,
			ppp to ppn,
			ppn to npn,

			// Vertical edges
			nnn to npn,
			nnp to npp,
			pnn to ppn,
			pnp to ppp
		)

		for (edge in lines) {
			val (start, end) = edge
			drawLine(
				vertexConsumer, pose,
				start[0], start[1], start[2],
				end[0], end[1], end[2],
				r, g, b, a
			)
		}

	}

	fun drawLine(
		vertexConsumer: VertexConsumer,
		pose: PoseStack.Pose,
		x1: Float, y1: Float, z1: Float,
		x2: Float, y2: Float, z2: Float,
		r: Float, g: Float, b: Float, a: Float
	) {
		var dx = x2 - x1
		var dy = y2 - y1
		var dz = z2 - z1

		val length = sqrt(dx * dx + dy * dy + dz * dz)
		if (length == 0f) return

		dx /= length
		dy /= length
		dz /= length

		vertexConsumer
			.addVertex(pose, x1, y1, z1)
			.setColor(r, g, b, a)
			.setNormal(pose, dx, dy, dz)

		vertexConsumer
			.addVertex(pose, x2, y2, z2)
			.setColor(r, g, b, a)
			.setNormal(pose, dx, dy, dz)
	}

}