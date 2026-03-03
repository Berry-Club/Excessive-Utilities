package dev.aaronhowser.mods.excessive_utilities.client.render.layer

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.model.PlayerModel
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.client.renderer.entity.layers.RenderLayer

class AngelRingWingsLayer(
	renderer: RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>
) : RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>>(renderer) {

	override fun render(
		poseStack: PoseStack,
		bufferSource: MultiBufferSource,
		packedLight: Int,
		livingEntity: AbstractClientPlayer,
		limbSwing: Float,
		limbSwingAmount: Float,
		partialTick: Float,
		ageInTicks: Float,
		netHeadYaw: Float,
		headPitch: Float
	) {

	}

}