package dev.aaronhowser.mods.excessive_utilities.client.render.bewlr

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.util.Mth
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions

class ManualMillBEWLR : BlockEntityWithoutLevelRenderer(
	Minecraft.getInstance().blockEntityRenderDispatcher,
	Minecraft.getInstance().entityModels
) {

	val itemRenderer: ItemRenderer = Minecraft.getInstance().itemRenderer
	val gearStack: ItemStack = ModItems.REDSTONE_GEAR.toStack()
	val blockState: BlockState = ModBlocks.MANUAL_MILL.get().defaultBlockState()

	override fun renderByItem(
		stack: ItemStack,
		displayContext: ItemDisplayContext,
		poseStack: PoseStack,
		buffer: MultiBufferSource,
		packedLight: Int,
		packedOverlay: Int
	) {
		val time = (AaronClientUtil.localLevel?.gameTime ?: 0) + Minecraft.getInstance().timer.gameTimeDeltaTicks
		val turnDegrees = Mth.wrapDegrees(time * 0.8f)

		Minecraft.getInstance().blockRenderer.renderSingleBlock(
			blockState,
			poseStack,
			buffer,
			packedLight,
			packedOverlay
		)

		poseStack.withPose {
			poseStack.translate(0.5, 0.4, 0.5)
			poseStack.mulPose(Axis.YP.rotationDegrees(turnDegrees))
			poseStack.mulPose(Axis.XP.rotationDegrees(90f))

			itemRenderer.renderStatic(
				gearStack,
				ItemDisplayContext.NONE,
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
