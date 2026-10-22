package dev.aaronhowser.mods.excessive_utilities.client.render.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.aaronhowser.mods.excessive_utilities.entity.FlatTransferNodeEntity
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemDisplayContext

class FlatTransferNodeEntityRenderer(context: EntityRendererProvider.Context) :
    EntityRenderer<FlatTransferNodeEntity, ItemEntityRenderState>(context) {
    private val itemModelResolver = context.itemModelResolver
    override fun createRenderState() = ItemEntityRenderState()
    override fun extractRenderState(entity: FlatTransferNodeEntity, state: ItemEntityRenderState, partialTick: Float) {
        super.extractRenderState(entity, state, partialTick)
        itemModelResolver.updateForNonLiving(state.item, entity.getAsItemStack(), ItemDisplayContext.GROUND, entity)
        state.direction = entity.aiming
		state.yaw = entity.yRot
    }
    override fun submit(state: ItemEntityRenderState, poseStack: PoseStack, collector: SubmitNodeCollector, camera: CameraRenderState) {
        super.submit(state, poseStack, collector, camera)
        poseStack.pushPose()
        when (state.direction) {
            Direction.DOWN -> { poseStack.translate(0.0, 0.0, -0.225); poseStack.mulPose(Axis.XP.rotationDegrees(90f)) }
            Direction.UP -> { poseStack.translate(0.0, 1.0, -0.225); poseStack.mulPose(Axis.XP.rotationDegrees(90f)) }
            else -> { poseStack.mulPose(Axis.YP.rotationDegrees(-state.yaw)); poseStack.translate(0.0, 0.25, 0.5) }
        }
        poseStack.scale(1.8f, 1.8f, 1.8f)
        state.item.submit(poseStack, collector, state.lightCoords, 0, state.outlineColor)
        poseStack.popPose()
    }
}
