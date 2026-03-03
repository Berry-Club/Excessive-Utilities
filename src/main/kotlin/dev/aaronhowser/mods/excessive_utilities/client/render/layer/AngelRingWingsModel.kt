package dev.aaronhowser.mods.excessive_utilities.client.render.layer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.renderer.RenderType

class AngelRingWingsModel : EntityModel<AbstractClientPlayer> {

	private val leftWing: ModelPart
	private val rightWing: ModelPart

	constructor(root: ModelPart) : super(RenderType::entityCutoutNoCull) {
		leftWing = root.getChild("left_wing")
		rightWing = root.getChild("right_wing")
	}

	override fun setupAnim(
		entity: AbstractClientPlayer,
		limbSwing: Float,
		limbSwingAmount: Float,
		ageInTicks: Float,
		netHeadYaw: Float,
		headPitch: Float
	) {

	}

	override fun renderToBuffer(
		poseStack: PoseStack,
		buffer: VertexConsumer,
		packedLight: Int,
		packedOverlay: Int,
		color: Int
	) {

	}
}