package dev.aaronhowser.mods.excessive_utilities.client.render.bewlr

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

//FIXME: Doesn't work because the item model isn't `builtin/entity`
// but that means I'd have to recreate the block part of the model here too, which is gross
class ManualMillBEWLR : BlockEntityWithoutLevelRenderer(
	Minecraft.getInstance().blockEntityRenderDispatcher,
	Minecraft.getInstance().entityModels
) {

	val itemRenderer: ItemRenderer = Minecraft.getInstance().itemRenderer
	val gearStack: ItemStack = ModItems.REDSTONE_GEAR.toStack()

	override fun renderByItem(
		stack: ItemStack,
		displayContext: ItemDisplayContext,
		poseStack: PoseStack,
		buffer: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		poseStack.withPose {
			poseStack.translate(0.5, 0.5, 0.5)
			poseStack.mulPose(Axis.XP.rotationDegrees(90f))

			itemRenderer.renderStatic(
				gearStack,
				displayContext,
				packedLight,
				packedOverlay,
				poseStack,
				buffer,
				null,
				0
			)
		}
	}

	object ClientItemExtensions : IClientItemExtensions {
		val BEWLR = ManualMillBEWLR()

		override fun getCustomRenderer(): BlockEntityWithoutLevelRenderer {
			return BEWLR
		}
	}

}