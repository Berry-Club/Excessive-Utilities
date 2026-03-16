package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.render.RenderUtil
import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.generator.RainbowGeneratorBlockEntity
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider

class RainbowGeneratorBER(
	val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<RainbowGeneratorBlockEntity> {

	override fun render(
		blockEntity: RainbowGeneratorBlockEntity,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		if (!blockEntity.blockState.getValue(GeneratorBlock.LIT)) return

		val tick = blockEntity.level?.gameTime ?: 0
		val time = tick + partialTick

		poseStack.pushPose()
		poseStack.translate(0.5f, 0.5f, 0.5f)

		RenderUtil.renderDragonRays(
			poseStack = poseStack,
			time = time,
			bufferSource = bufferSource,
		)

		poseStack.popPose()
	}

}