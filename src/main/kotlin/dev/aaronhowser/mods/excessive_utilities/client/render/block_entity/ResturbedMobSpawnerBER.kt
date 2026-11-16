package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.excessive_utilities.block_entity.ResturbedMobSpawnerBlockEntity
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.SpawnerRenderer
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState
import net.minecraft.client.renderer.blockentity.state.SpawnerRenderState
import net.minecraft.client.renderer.entity.EntityRenderDispatcher
import net.minecraft.client.renderer.feature.ModelFeatureRenderer
import net.minecraft.client.renderer.state.level.CameraRenderState
import net.minecraft.util.Mth
import net.minecraft.world.phys.Vec3

class ResturbedMobSpawnerBER(
	context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<ResturbedMobSpawnerBlockEntity, SpawnerRenderState> {

	private val dispatcher: EntityRenderDispatcher = context.entityRenderer
	override fun createRenderState() = SpawnerRenderState()

	override fun extractRenderState(
		blockEntity: ResturbedMobSpawnerBlockEntity,
		state: SpawnerRenderState,
		partial: Float,
		camera: Vec3,
		overlay: ModelFeatureRenderer.CrumblingOverlay?
	) {
		super.extractRenderState(blockEntity, state, partial, camera, overlay)

		val level = blockEntity.level ?: return
		val entity = blockEntity.spawner.getOrCreateDisplayEntity(level, blockEntity.blockPos) ?: return

		val entityState = dispatcher.extractEntity(entity, partial)
		entityState.lightCoords = state.lightCoords
		state.displayEntity = entityState

		state.spin = Mth.lerp(partial.toDouble(), blockEntity.spawner.getOSpin(), blockEntity.spawner.getSpin()).toFloat() * 10f
		state.scale = 0.53125f
		val maxLength = maxOf(entity.bbWidth, entity.bbHeight)
		if (maxLength > 1f) {
			state.scale /= maxLength
		}
	}

	override fun submit(
		state: SpawnerRenderState,
		poseStack: PoseStack,
		collector: SubmitNodeCollector,
		camera: CameraRenderState
	) {
		val displayEntity = state.displayEntity ?: return
		SpawnerRenderer.submitEntityInSpawner(poseStack, collector, displayEntity, dispatcher, state.spin, state.scale, camera)
	}
}