package dev.aaronhowser.mods.excessive_utilities.client.render.layer

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.blaze3d.vertex.VertexConsumer
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.client.renderer.RenderType
import net.minecraft.util.Mth

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

	companion object {
		fun createLayer(): LayerDefinition {
			val meshDefinition = MeshDefinition()
			val partDefinition = meshDefinition.root
			val cubeDeformation = CubeDeformation(1f)

			partDefinition.addOrReplaceChild(
				"left_wing",
				CubeListBuilder.create()
					.texOffs(22, 0)
					.addBox(
						-10f, 0f, 0f,
						10f, 20f, 2f,
						cubeDeformation
					),
				PartPose.offsetAndRotation(5f, 0f, 0f, Mth.PI / 12f, 0f, -Mth.PI / 12f)
			)

			partDefinition.addOrReplaceChild(
				"right_wing",
				CubeListBuilder.create()
					.texOffs(22, 0)
					.addBox(
						0f, 0f, 0f,
						10f, 20f, 2f,
						cubeDeformation
					),
				PartPose.offsetAndRotation(-5f, 0f, 0f, Mth.PI / 12f, 0f, Mth.PI / 12f)
			)

			return LayerDefinition.create(meshDefinition, 64, 32)
		}
	}

}