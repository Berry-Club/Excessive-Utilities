package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.render.AaronRenderTypes
import dev.aaronhowser.mods.excessive_utilities.block_entity.EnderPorcupineBlockEntity
import dev.aaronhowser.mods.excessive_utilities.client.render.WandRenderer
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
			1f, 1f, 1f, 1f
		)

	}

}