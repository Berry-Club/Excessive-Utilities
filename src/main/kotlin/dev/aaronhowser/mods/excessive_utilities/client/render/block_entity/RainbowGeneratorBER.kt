package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.aaron.client.render.AaronRenderUtil
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.generator.RainbowGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.world.phys.Vec3
import java.awt.Color

class RainbowGeneratorBER(
	context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<RainbowGeneratorBlockEntity, RainbowGeneratorBER.State> {

	override fun createRenderState() = State()

	override fun extractRenderState(
		blockEntity: RainbowGeneratorBlockEntity,
		state: State,
		partial: Float,
		camera: Vec3,
		overlay: ModelFeatureRenderer.CrumblingOverlay?
	) {
		BlockEntityRenderState.extractBase(blockEntity, state, overlay)
		state.active = blockEntity.blockState.getValue(GeneratorBlock.LIT)
		state.width = ClientConfig.CONFIG.rainbowGeneratorRayWidth.get().toFloat()
		state.length = ClientConfig.CONFIG.rainbowGeneratorRayLength.get().toFloat()
		val tick = blockEntity.level?.gameTime ?: 0L
		state.time = (tick + partial) * ClientConfig.CONFIG.rainbowGeneratorTimeFactor.get().toFloat()
	}

	override fun submit(
		state: State,
		poseStack: PoseStack,
		collector: SubmitNodeCollector,
		camera: CameraRenderState
	) {
		if (!state.active || state.width <= 0 || state.length <= 0) return
		val centerColor = Color.HSBtoRGB((state.time % 1f), 1f, 1f)

		poseStack.withPose {
			poseStack.translate(.5, .5, .5)
			AaronRenderUtil.submitDragonRays(
				poseStack = poseStack,
				time = state.time,
				collector = collector,
				centerColor = centerColor,
				outerColor = centerColor and 0x00FFFFFF,
				rayLength = state.length,
				rayWidth = state.width
			)
		}
	}

	class State : BlockEntityRenderState() {
		var time = 0f
		var width = 0f
		var length = 0f
		var active = false
	}
}