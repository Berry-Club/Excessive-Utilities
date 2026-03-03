package dev.aaronhowser.mods.excessive_utilities.client.render.layer

import net.minecraft.client.model.AgeableListModel
import net.minecraft.client.model.geom.ModelPart
import net.minecraft.client.model.geom.PartPose
import net.minecraft.client.model.geom.builders.CubeDeformation
import net.minecraft.client.model.geom.builders.CubeListBuilder
import net.minecraft.client.model.geom.builders.LayerDefinition
import net.minecraft.client.model.geom.builders.MeshDefinition
import net.minecraft.client.player.AbstractClientPlayer
import net.minecraft.util.Mth

class AngelRingWingsModel(
	root: ModelPart
) : AgeableListModel<AbstractClientPlayer>() {

	private val leftWing: ModelPart = root.getChild("left_wing")
	private val rightWing: ModelPart = root.getChild("right_wing")

	override fun headParts(): Iterable<ModelPart> = emptyList()
	override fun bodyParts(): Iterable<ModelPart> = listOf(leftWing, rightWing)

	override fun setupAnim(
		entity: AbstractClientPlayer,
		limbSwing: Float,
		limbSwingAmount: Float,
		ageInTicks: Float,
		netHeadYaw: Float,
		headPitch: Float
	) {
		val wingFlap = Mth.sin(ageInTicks * 0.5f) * 0.5f + 0.5f
		leftWing.zRot = Mth.PI / 12f + wingFlap * Mth.PI / 6f
		rightWing.zRot = -Mth.PI / 12f - wingFlap * Mth.PI / 6f
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