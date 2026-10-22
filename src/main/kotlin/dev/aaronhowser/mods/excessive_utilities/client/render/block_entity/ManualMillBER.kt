package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.aaronhowser.mods.excessive_utilities.block_entity.mill.ManualMillBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.item.ItemModelResolver
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3

class ManualMillBER(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<ManualMillBlockEntity, ManualMillRenderState> {
    private val itemResolver: ItemModelResolver = context.itemModelResolver
    private val gearStack by lazy { ModItems.REDSTONE_GEAR.get().defaultInstance }

    override fun createRenderState() = ManualMillRenderState()

    override fun extractRenderState(blockEntity: ManualMillBlockEntity, state: ManualMillRenderState, partialTick: Float, cameraPosition: Vec3, crumblingOverlay: ModelFeatureRenderer.CrumblingOverlay?) {
        BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay)
        itemResolver.updateForTopItem(state.gear, gearStack, ItemDisplayContext.NONE, blockEntity.level, null, 0)
        state.turnDegrees = Mth.rotLerp(partialTick, blockEntity.prevTurnDegrees, blockEntity.turnDegrees)
    }

    override fun submit(state: ManualMillRenderState, poseStack: PoseStack, collector: SubmitNodeCollector, camera: CameraRenderState) {
        poseStack.pushPose()
        poseStack.translate(0.5, 0.4, 0.5)
        poseStack.mulPose(Axis.YP.rotationDegrees(state.turnDegrees))
        poseStack.mulPose(Axis.XP.rotationDegrees(90f))
        state.gear.submit(poseStack, collector, state.lightCoords, 0, 0)
        poseStack.popPose()
    }
}
