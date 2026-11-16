package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import dev.aaronhowser.mods.excessive_utilities.block_entity.EnderQuarryBlockEntity
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BeaconRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3

class EnderQuarryBER(
	context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<EnderQuarryBlockEntity, EnderQuarryBER.State> {

	override fun createRenderState() = State()

	override fun extractRenderState(
		blockEntity: EnderQuarryBlockEntity,
		state: State,
		partialTick: Float,
		cameraPosition: Vec3,
		crumblingOverlay: ModelFeatureRenderer.CrumblingOverlay?
	) {
		super.extractRenderState(blockEntity, state, partialTick, cameraPosition, crumblingOverlay)

		val target = blockEntity.targetPos
		state.active = target != null

		if (target != null) {
			state.dx = blockEntity.blockPos.x - target.x
			state.dy = blockEntity.blockPos.y - target.y
			state.dz = blockEntity.blockPos.z - target.z

			val gameTime = blockEntity.level?.gameTime ?: 0L
			state.animationTime = Math.floorMod(gameTime, 40).toFloat() + partialTick
		}
	}

	override fun submit(state: State, poseStack: PoseStack, collector: SubmitNodeCollector, camera: CameraRenderState) {
		if (!state.active) return
		poseStack.withPose {
			poseStack.translate(-state.dx.toDouble(), -state.dy.toDouble(), -state.dz.toDouble())
			for (index in 0 until state.dy + 1) {
				BeaconRenderer.submitBeaconBeam(
					poseStack,
					collector,
					BeaconRenderer.BEAM_LOCATION,
					1f,
					state.animationTime,
					index,
					1,
					0xFFFFFFFF.toInt(),
					0.2f,
					0.25f
				)
			}
		}
	}

	override fun getRenderBoundingBox(blockEntity: EnderQuarryBlockEntity): AABB = AABB.INFINITE
	override fun shouldRenderOffScreen() = true
	override fun getViewDistance() = Int.MAX_VALUE

	class State : BlockEntityRenderState() {
		var dx = 0
		var dy = 0
		var dz = 0
		var animationTime = 0f
		var active = false
	}
}