package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.aaronhowser.mods.excessive_utilities.block_entity.CreativeHarvestBlockEntity
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.LevelRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.item.ItemModelResolver
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.world.phys.Vec3

class CreativeHarvestBER(context: BlockEntityRendererProvider.Context) : BlockEntityRenderer<CreativeHarvestBlockEntity, CreativeHarvestRenderState> {
    private val itemResolver: ItemModelResolver = context.itemModelResolver

    override fun createRenderState() = CreativeHarvestRenderState()

    override fun extractRenderState(blockEntity: CreativeHarvestBlockEntity, state: CreativeHarvestRenderState, partialTick: Float, cameraPosition: Vec3, crumblingOverlay: ModelFeatureRenderer.CrumblingOverlay?) {
        BlockEntityRenderState.extractBase(blockEntity, state, crumblingOverlay)
        itemResolver.updateForTopItem(state.item, blockEntity.mimicStack.copy(), ItemDisplayContext.GUI, blockEntity.level, null, 0)
        state.directions.clear()
        val level = blockEntity.level ?: return
        for (direction in blockEntity.visibleDirections) {
            state.directions += direction to 0xF000F0
        }
    }

    override fun submit(state: CreativeHarvestRenderState, poseStack: PoseStack, collector: SubmitNodeCollector, camera: CameraRenderState) {
        for ((direction, light) in state.directions) {
            poseStack.pushPose()
            val flattening = 0.0001
            when (direction) {
                Direction.NORTH -> { poseStack.translate(0.71, 0.7, -flattening); poseStack.mulPose(Axis.YP.rotationDegrees(180f)) }
                Direction.SOUTH -> poseStack.translate(0.29, 0.7, 1.0 + flattening)
                Direction.WEST -> { poseStack.translate(-flattening, 0.7, 0.29); poseStack.mulPose(Axis.YP.rotationDegrees(-90f)) }
                Direction.EAST -> { poseStack.translate(1.0 + flattening, 0.7, 0.71); poseStack.mulPose(Axis.YP.rotationDegrees(90f)) }
                else -> {}
            }
            poseStack.scale(0.025f, 0.025f, flattening.toFloat())
            poseStack.translate(8.0, -8.0, 8.0)
            poseStack.scale(16f, 16f, 16f)
            state.item.submit(poseStack, collector, light, 0, 0xFFFFFF)
            poseStack.popPose()
        }
    }
}
