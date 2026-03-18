package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.client.render.AaronRenderTypes
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.EnderPorcupineBlockEntity
import dev.aaronhowser.mods.excessive_utilities.client.render.WandRenderer
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

		WandRenderer.renderCubeWireframe(
			poseStack,
			linesConsumer,
			x, y, z,
			x + 1f, y + 1f, z + 1f,
			1f, 1f, 1f, 0.5f
		)

		val minOffset = blockEntity.minimumOffset
		val maxOffset = blockEntity.maximumOffset

		WandRenderer.renderCubeWireframe(
			poseStack,
			linesConsumer,
			minOffset.x.toFloat(), minOffset.y.toFloat(), minOffset.z.toFloat(),
			maxOffset.x.toFloat() + 1f, maxOffset.y.toFloat() + 1f, maxOffset.z.toFloat() + 1f,
			0f, 1f, 0f, 0.2f
		)

	}

}