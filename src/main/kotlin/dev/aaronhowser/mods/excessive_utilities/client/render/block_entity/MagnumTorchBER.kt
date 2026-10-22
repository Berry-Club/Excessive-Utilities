package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.client.render.AaronRenderUtil
import dev.aaronhowser.mods.excessive_utilities.block_entity.MagnumTorchBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.world.phys.AABB
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.world.phys.Vec3

class MagnumTorchBER(
	val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<MagnumTorchBlockEntity, LegacyBlockEntityRenderState<MagnumTorchBlockEntity>> {
	override fun createRenderState() = LegacyBlockEntityRenderState<MagnumTorchBlockEntity>()
	override fun extractRenderState(blockEntity: MagnumTorchBlockEntity, state: LegacyBlockEntityRenderState<MagnumTorchBlockEntity>, partialTick: Float, cameraPosition: Vec3, crumblingOverlay: ModelFeatureRenderer.CrumblingOverlay?) {
		BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay)
		state.blockPos = blockEntity.blockPos
	}

	override fun submit(
		state: LegacyBlockEntityRenderState<MagnumTorchBlockEntity>,
		poseStack: PoseStack,
		collector: SubmitNodeCollector,
		camera: CameraRenderState
	) {
		val player = AaronClientUtil.localPlayer ?: return
		if (!player.isHolding(ModBlocks.MAGNUM_TORCH.asItem())) return

		val radius = ServerConfig.CONFIG.magnumTorchRadius.get().toDouble()

		val minX = -radius
		val minY = -radius
		val minZ = -radius
		val maxX = radius + 1
		val maxY = radius + 1
		val maxZ = radius + 1

		AaronRenderUtil.renderCubeWireframeThroughWalls(
			poseStack,
			minX, minY, minZ,
			maxX, maxY, maxZ,
			color = 0x66FFFFFF
		)

		AaronRenderUtil.renderCubeWireframeThroughWalls(
			poseStack,
			0.0, 0.0, 0.0,
			1.0, 1.0, 1.0,
			color = 0xFFC18D58.toInt()
		)
	}

	override fun getRenderBoundingBox(blockEntity: MagnumTorchBlockEntity): AABB {
		return AABB.INFINITE
	}

}
