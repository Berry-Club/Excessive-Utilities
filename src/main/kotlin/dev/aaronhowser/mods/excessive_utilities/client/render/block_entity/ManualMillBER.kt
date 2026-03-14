package dev.aaronhowser.mods.excessive_utilities.client.render.block_entity

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.excessive_utilities.block_entity.mill.ManualMillBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext

// TODO: Add the gear to the item model
class ManualMillBER(
	val context: BlockEntityRendererProvider.Context
) : BlockEntityRenderer<ManualMillBlockEntity> {

	val gearStack = ModItems.REDSTONE_GEAR.getDefaultInstance()

	override fun render(
		blockEntity: ManualMillBlockEntity,
		partialTick: Float,
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		val turnDegrees = Mth.rotLerp(partialTick, blockEntity.prevTurnDegrees, blockEntity.turnDegrees)
		val itemRenderer = context.itemRenderer

		poseStack.pushPose()

		poseStack.translate(0.5, 0.4, 0.5)

		poseStack.mulPose(Axis.YP.rotationDegrees(turnDegrees))
		poseStack.mulPose(Axis.XP.rotationDegrees(90f))

		itemRenderer.renderStatic(
			gearStack,
			ItemDisplayContext.NONE,
			packedLight,
			packedOverlay,
			poseStack,
			bufferSource,
			blockEntity.level,
			0
		)

		poseStack.popPose()
	}
}