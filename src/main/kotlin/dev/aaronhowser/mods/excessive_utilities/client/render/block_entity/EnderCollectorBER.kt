package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.client.render.AaronRenderUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getPovResult
import dev.aaronhowser.mods.excessive_utilities.block_entity.EnderCollectorBlockEntity
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.world.phys.Vec3

class EnderCollectorBER(
	val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<EnderCollectorBlockEntity, LegacyBlockEntityRenderState<EnderCollectorBlockEntity>> {
	 override fun createRenderState() = LegacyBlockEntityRenderState<EnderCollectorBlockEntity>()
	 override fun extractRenderState(blockEntity: EnderCollectorBlockEntity, state: LegacyBlockEntityRenderState<EnderCollectorBlockEntity>, partialTick: Float, cameraPosition: Vec3, crumblingOverlay: ModelFeatureRenderer.CrumblingOverlay?) {
		BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay)
		state.blockPos = blockEntity.blockPos
		state.radius = blockEntity.radius
	}

	override fun submit(
		state: LegacyBlockEntityRenderState<EnderCollectorBlockEntity>,
		poseStack: PoseStack,
		collector: SubmitNodeCollector,
		camera: CameraRenderState
	) {
		val player = AaronClientUtil.localPlayer ?: return
		val lookingAt = player.getPovResult(player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE))

		val pos = state.blockPos
		if (lookingAt.blockPos != pos) return

		val radius = state.radius

		AaronRenderUtil.renderCubeWireframeThroughWalls(
			poseStack,
			-radius, -radius, -radius,
			radius + 1.0, radius + 1.0, radius + 1.0,
			0xFFFFFFFF.toInt()
		)

	}

}
