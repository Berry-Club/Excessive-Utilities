package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.render.RenderUtil
import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.generator.RainbowGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import java.awt.Color

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

		val width = ClientConfig.CONFIG.rainbowGeneratorRayWidth.get().toFloat()
		val length = ClientConfig.CONFIG.rainbowGeneratorRayLength.get().toFloat()

		if (width <= 0 || length <= 0) return

		val tick = blockEntity.level?.gameTime ?: 0
		val time = tick + partialTick * ClientConfig.CONFIG.rainbowGeneratorTimeFactor.get().toFloat()

		val centerColor = getColorFromTime(time)
		val outerColor = centerColor and 0x00FFFFFF

		poseStack.pushPose()
		poseStack.translate(0.5f, 0.5f, 0.5f)

		RenderUtil.renderDragonRays(
			poseStack = poseStack,
			time = time,
			bufferSource = bufferSource,
			centerColor = centerColor,
			outerColor = outerColor,
			rayLength = length,
			rayWidth = width
		)

		poseStack.popPose()
	}

	private fun getColorFromTime(time: Float): Int {
		val hue = (time % 1) * 360f

		val saturation = 1f
		val brightness = 1f

		return Color.HSBtoRGB(hue / 360f, saturation, brightness)
	}

}