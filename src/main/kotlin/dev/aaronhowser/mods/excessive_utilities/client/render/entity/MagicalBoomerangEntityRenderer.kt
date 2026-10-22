package dev.aaronhowser.mods.excessive_utilities.client.render.entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.aaronhowser.mods.excessive_utilities.entity.MagicalBoomerangEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.entity.EntityRenderer
import net.minecraft.client.renderer.entity.EntityRendererProvider
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext

class MagicalBoomerangEntityRenderer(context: EntityRendererProvider.Context) :
    EntityRenderer<MagicalBoomerangEntity, ItemEntityRenderState>(context) {
    private val itemModelResolver = context.itemModelResolver
    private val boomerangStack by lazy { ModItems.MAGICAL_BOOMERANG.get().defaultInstance }
    override fun createRenderState() = ItemEntityRenderState()
    override fun extractRenderState(entity: MagicalBoomerangEntity, state: ItemEntityRenderState, partialTick: Float) {
        super.extractRenderState(entity, state, partialTick)
        itemModelResolver.updateForNonLiving(state.item, boomerangStack, ItemDisplayContext.GROUND, entity)
        state.rotation = Mth.wrapDegrees((entity.tickCount + partialTick) * 20f)
    }
    override fun submit(state: ItemEntityRenderState, poseStack: PoseStack, collector: SubmitNodeCollector, camera: CameraRenderState) {
        super.submit(state, poseStack, collector, camera)
        poseStack.pushPose()
        poseStack.mulPose(Axis.YP.rotationDegrees(state.rotation))
        poseStack.mulPose(Axis.XP.rotationDegrees(90f))
        state.item.submit(poseStack, collector, state.lightCoords, 0, state.outlineColor)
        poseStack.popPose()
    }
}
