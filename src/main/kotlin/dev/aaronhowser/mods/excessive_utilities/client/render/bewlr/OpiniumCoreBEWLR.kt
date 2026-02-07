package dev.aaronhowser.mods.excessive_utilities.client.render.bewlr

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

class OpiniumCoreBEWLR : BlockEntityWithoutLevelRenderer(
	Minecraft.getInstance().blockEntityRenderDispatcher,
	Minecraft.getInstance().entityModels
) {

	override fun renderByItem(
		stack: ItemStack,
		displayContext: ItemDisplayContext,
		poseStack: PoseStack,
		buffer: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		val component = stack.get(ModDataComponents.OPINIUM_CORE_CONTENTS) ?: return

		val (inner, outer) = component

		renderInner(inner, poseStack, buffer, displayContext, packedLight, packedOverlay)
	}

	companion object {
		private val ITEM_RENDERER: ItemRenderer = Minecraft.getInstance().itemRenderer

		private fun renderInner(
			innerStack: ItemStack,
			poseStack: PoseStack,
			buffer: MultiBufferSource,
			displayContext: ItemDisplayContext,
			packedLight: Int,
			packedOverlay: Int
		) {
			poseStack.pushPose()

			poseStack.translate(0.5, 0.5, 0.5)

			val time = AaronClientUtil.localLevel?.gameTime ?: 0
			val yRotPerTick = 4f
			val yRot = Mth.wrapDegrees(time * yRotPerTick)
			poseStack.mulPose(Axis.YP.rotationDegrees(yRot))

			val xRotPerTick = 2f
			val xRot = Mth.wrapDegrees(time * xRotPerTick)
			poseStack.mulPose(Axis.XP.rotationDegrees(xRot))

			val zRotPerTick = 1f
			val zRot = Mth.wrapDegrees(time * zRotPerTick)
			poseStack.mulPose(Axis.ZP.rotationDegrees(zRot))

			poseStack.scale(0.75f, 0.75f, 0.75f)

			ITEM_RENDERER.renderStatic(
				innerStack,
				displayContext,
				packedLight,
				packedOverlay,
				poseStack,
				buffer,
				null,
				0
			)

			poseStack.popPose()
		}
	}

	object ClientItemExtensions : IClientItemExtensions {
		val BEWLR = OpiniumCoreBEWLR()

		override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
			return BEWLR
		}
	}

}