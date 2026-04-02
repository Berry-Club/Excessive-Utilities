package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import dev.aaronhowser.mods.excessive_utilities.block_entity.CreativeHarvestBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemDisplayContext

// https://github.com/mekanism/Mekanism/blob/1.21.x/src/main/java/mekanism/client/render/tileentity/RenderBin.java
class CreativeHarvestBER(
	val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<CreativeHarvestBlockEntity> {

	override fun render(
		blockEntity: CreativeHarvestBlockEntity,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		val level = blockEntity.level ?: return
		val stack = blockEntity.mimicStack

		val itemRenderer = Minecraft.getInstance().itemRenderer

		val directions = blockEntity.visibleDirections
		for (dir in directions) {
			poseStack.withPose {

				val flattening = 0.0001

				when (dir) {
					Direction.NORTH -> {
						poseStack.translate(0.71, 0.7, -flattening)
						poseStack.mulPose(Axis.YP.rotationDegrees(180f))
					}

					Direction.SOUTH -> {
						poseStack.translate(0.29, 0.7, 1.0 + flattening)
					}

					Direction.WEST -> {
						poseStack.translate(-flattening, 0.7, 0.29)
						poseStack.mulPose(Axis.YP.rotationDegrees(-90f))
					}

					Direction.EAST -> {
						poseStack.translate(1.0 + flattening, 0.7, 0.71)
						poseStack.mulPose(Axis.YP.rotationDegrees(90f))
					}

					else -> {}
				}

				val scale = 0.025f
				poseStack.scale(scale, scale, flattening.toFloat())
				poseStack.translate(8.0, -8.0, 8.0)
				poseStack.scale(16f, 16f, 16f)

				val light = LevelRenderer.getLightColor(
					level,
					blockEntity.blockPos.relative(dir)
				)

				itemRenderer
					.renderStatic(
						stack,
						ItemDisplayContext.GUI,
						light,
						packedOverlay,
						poseStack,
						bufferSource,
						level,
						blockEntity.blockPos.asLong().toInt()
					)
			}
		}

	}
}