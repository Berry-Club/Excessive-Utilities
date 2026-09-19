package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.client.render.AaronRenderUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.ender_porcupine.EnderPorcupineBlockEntity
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

		val x = offset.x.toDouble()
		val y = offset.y.toDouble()
		val z = offset.z.toDouble()

		val targetColor = ClientConfig.CONFIG.enderPorcupineCurrentTargetColor.get()

		AaronRenderUtil.renderCubeWireframeThroughWalls(
			poseStack,
			x, y, z,
			x + 1.0, y + 1.0, z + 1.0,
			targetColor
		)

		val minOffset = blockEntity.minimumOffset
		val maxOffset = blockEntity.maximumOffset

		val searchVolumeColor = ClientConfig.CONFIG.enderPorcupineSearchVolumeColor.get()

		AaronRenderUtil.renderCubeWireframeThroughWalls(
			poseStack,
			minOffset.x.toDouble(), minOffset.y.toDouble(), minOffset.z.toDouble(),
			maxOffset.x.toDouble() + 1.0, maxOffset.y.toDouble() + 1.0, maxOffset.z.toDouble() + 1.0,
			searchVolumeColor
		)

	}

}