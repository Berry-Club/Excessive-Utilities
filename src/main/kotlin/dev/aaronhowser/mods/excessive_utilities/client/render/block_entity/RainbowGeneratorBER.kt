package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.generator.RainbowGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.rendertype.RenderTypes
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.util.RandomSource
import net.minecraft.world.phys.Vec3
import org.joml.Quaternionf
import org.joml.Vector3f
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

		poseStack.pushPose()
		poseStack.translate(.5, .5, .5)
		collector.submitCustomGeometry(poseStack, RenderTypes.dragonRays()) { pose, vertexConsumer ->
			val random = RandomSource.create(432L)
			val zero = Vector3f()
			val first = Vector3f()
			val second = Vector3f()
			val third = Vector3f()
			val rotation = Quaternionf()
			for (rayIndex in 0 until 15) {
				rotation.identity()
					.rotateXYZ(
						random.nextFloat() * (Math.PI * 2).toFloat(),
						random.nextFloat() * (Math.PI * 2).toFloat(),
						random.nextFloat() * (Math.PI * 2).toFloat()
					)
					.rotateXYZ(
						random.nextFloat() * (Math.PI * 2).toFloat(),
						random.nextFloat() * (Math.PI * 2).toFloat(),
						random.nextFloat() * (Math.PI * 2).toFloat() + state.time * (Math.PI / 2).toFloat()
					)

				first.set(-0.8660254f * state.width, state.length, -0.5f * state.width)
				second.set(0.8660254f * state.width, state.length, -0.5f * state.width)
				third.set(0f, state.length, state.width)

				rotation.transform(first)
				rotation.transform(second)
				rotation.transform(third)

				vertexConsumer.addVertex(pose, zero).setColor(centerColor)
				vertexConsumer.addVertex(pose, first).setColor(centerColor and 0x00FFFFFF)
				vertexConsumer.addVertex(pose, second).setColor(centerColor and 0x00FFFFFF)
				vertexConsumer.addVertex(pose, zero).setColor(centerColor)
				vertexConsumer.addVertex(pose, second).setColor(centerColor and 0x00FFFFFF)
				vertexConsumer.addVertex(pose, third).setColor(centerColor and 0x00FFFFFF)
				vertexConsumer.addVertex(pose, zero).setColor(centerColor)
				vertexConsumer.addVertex(pose, third).setColor(centerColor and 0x00FFFFFF)
				vertexConsumer.addVertex(pose, first).setColor(centerColor and 0x00FFFFFF)
			}
		}
		poseStack.popPose()
	}

	class State : BlockEntityRenderState() {
		var time = 0f
		var width = 0f
		var length = 0f
		var active = false
	}
}