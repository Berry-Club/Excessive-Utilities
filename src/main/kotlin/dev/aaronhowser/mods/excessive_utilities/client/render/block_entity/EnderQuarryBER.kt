package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.excessive_utilities.block_entity.EnderQuarryBlockEntity
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BeaconRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.world.phys.AABB
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.world.phys.Vec3

class EnderQuarryBER : BlockEntityRenderer<EnderQuarryBlockEntity, EnderQuarryRenderState> {
    override fun createRenderState() = EnderQuarryRenderState()

    override fun extractRenderState(blockEntity: EnderQuarryBlockEntity, state: EnderQuarryRenderState, partialTick: Float, cameraPosition: Vec3, crumblingOverlay: ModelFeatureRenderer.CrumblingOverlay?) {
        BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay)
        val target = blockEntity.targetPos
        state.active = target != null
        state.partialTick = partialTick
        if (target != null) {
            state.dx = blockEntity.blockPos.x - target.x
            state.dy = blockEntity.blockPos.y - target.y
            state.dz = blockEntity.blockPos.z - target.z
            state.gameTime = blockEntity.level?.gameTime ?: 0L
        }
    }

    override fun submit(state: EnderQuarryRenderState, poseStack: PoseStack, collector: SubmitNodeCollector, camera: CameraRenderState) {
        if (!state.active) return
        poseStack.pushPose()
        poseStack.translate(-state.dx.toDouble(), -state.dy.toDouble(), -state.dz.toDouble())
        repeat(state.dy + 1) { index ->
        }
        poseStack.popPose()
    }

    override fun getRenderBoundingBox(blockEntity: EnderQuarryBlockEntity): AABB = AABB.INFINITE
}
