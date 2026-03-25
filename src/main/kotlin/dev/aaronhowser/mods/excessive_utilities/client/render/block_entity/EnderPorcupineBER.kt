package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.client.render.AaronRenderTypes
import dev.aaronhowser.mods.aaron.misc.ARGB
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.EnderPorcupineBlockEntity
import dev.aaronhowser.mods.excessive_utilities.client.render.WandRenderer
import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider

class EnderPorcupineBER(
	val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<EnderPorcupineBlockEntity> {

	override fun render(
		blockEntity: EnderPorcupineBlockEntity,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		val player = AaronClientUtil.localPlayer ?: return
		if (!player.isHolding { it.isItem(ModItemTagsProvider.RENDER_ENDER_PORCUPINE_WHILE_HOLDING) }) return

		val offset = blockEntity.getCurrentOffset()
		val linesConsumer = bufferSource.getBuffer(AaronRenderTypes.LINES_THROUGH_WALL_RENDER_TYPE)

		val x = offset.x.toFloat()
		val y = offset.y.toFloat()
		val z = offset.z.toFloat()

		val targetColor = ClientConfig.CONFIG.enderPorcupineCurrentTargetColor.get()

		val targetArgb = ARGB.fromInt(targetColor)
		val (taInt, trInt, tgInt, tbInt) = targetArgb

		val targetA = taInt / 255f
		val targetR = trInt / 255f
		val targetG = tgInt / 255f
		val targetB = tbInt / 255f

		WandRenderer.renderCubeWireframe(
			poseStack,
			linesConsumer,
			x, y, z,
			x + 1f, y + 1f, z + 1f,
			targetR, targetG, targetB, targetA
		)

		val minOffset = blockEntity.minimumOffset
		val maxOffset = blockEntity.maximumOffset

		val searchVolumeColor = ClientConfig.CONFIG.enderPorcupineSearchVolumeColor.get()

		val fullArgb = ARGB.fromInt(searchVolumeColor)
		val (faInt, frInt, fgInt, fbInt) = fullArgb

		val fullA = faInt / 255f
		val fullR = frInt / 255f
		val fullG = fgInt / 255f
		val fullB = fbInt / 255f

		WandRenderer.renderCubeWireframe(
			poseStack,
			linesConsumer,
			minOffset.x.toFloat(), minOffset.y.toFloat(), minOffset.z.toFloat(),
			maxOffset.x.toFloat() + 1f, maxOffset.y.toFloat() + 1f, maxOffset.z.toFloat() + 1f,
			fullR, fullG, fullB, fullA
		)

	}

}