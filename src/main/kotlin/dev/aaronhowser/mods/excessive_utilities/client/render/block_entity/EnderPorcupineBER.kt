package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.client.render.AaronRenderUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.ender_porcupine.EnderPorcupineBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec3

class EnderPorcupineBER(
	context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<EnderPorcupineBlockEntity, EnderPorcupineBER.State> {

	override fun createRenderState() = State()
	override fun extractRenderState(
		blockEntity: EnderPorcupineBlockEntity,
		state: State,
		partialTick: Float,
		cameraPosition: Vec3,
		crumblingOverlay: ModelFeatureRenderer.CrumblingOverlay?
	) {
		BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay)
		state.currentOffset = blockEntity.getCurrentOffset()
		state.minimumOffset = blockEntity.minimumOffset
		state.maximumOffset = blockEntity.maximumOffset
	}

	override fun submit(
		state: State,
		poseStack: PoseStack,
		collector: SubmitNodeCollector,
		camera: CameraRenderState
	) {
		val player = AaronClientUtil.localPlayer ?: return
		if (!player.isHolding { it.isItem(ModItemTagsProvider.RENDER_ENDER_PORCUPINE_WHILE_HOLDING) }) return

		val offset = state.currentOffset

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

		val minOffset = state.minimumOffset
		val maxOffset = state.maximumOffset

		val searchVolumeColor = ClientConfig.CONFIG.enderPorcupineSearchVolumeColor.get()

		AaronRenderUtil.renderCubeWireframeThroughWalls(
			poseStack,
			minOffset.x.toDouble(), minOffset.y.toDouble(), minOffset.z.toDouble(),
			maxOffset.x.toDouble() + 1.0, maxOffset.y.toDouble() + 1.0, maxOffset.z.toDouble() + 1.0,
			searchVolumeColor
		)

	}

	class State : BlockEntityRenderState() {
		var radius = 0.0
		var currentOffset = Vec3i.ZERO
		var minimumOffset = Vec3i.ZERO
		var maximumOffset = Vec3i.ZERO
	}

}