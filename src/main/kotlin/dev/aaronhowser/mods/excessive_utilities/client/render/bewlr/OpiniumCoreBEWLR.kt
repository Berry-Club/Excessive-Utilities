package dev.aaronhowser.mods.excessive_utilities.client.render.bewlr

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.mojang.serialization.MapCodec
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronDsls.withPose
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.SubmitNodeCollector
import net.minecraft.client.renderer.item.ItemModelResolver
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import org.joml.Vector3fc
import java.util.function.Consumer

class OpiniumCoreBEWLR : SpecialModelRenderer<ItemStack> {

	private val itemModelResolver: ItemModelResolver = Minecraft.getInstance().itemModelResolver
	private val centerRenderState = ItemStackRenderState()
	private val outerRenderState = ItemStackRenderState()

	override fun extractArgument(stack: ItemStack): ItemStack = stack.copy()
	override fun getExtents(output: Consumer<Vector3fc>) {}

	override fun submit(
		argument: ItemStack?,
		poseStack: PoseStack,
		collector: SubmitNodeCollector,
		lightCoords: Int,
		overlayCoords: Int,
		hasFoil: Boolean,
		outlineColor: Int
	) {
		val stack = argument ?: return
		val contents = stack.get(ModDataComponents.OPINIUM_CORE_CONTENTS) ?: return

		itemModelResolver.updateForTopItem(
			centerRenderState,
			contents.getCenter(),
			ItemDisplayContext.NONE,
			AaronClientUtil.localLevel,
			null,
			0
		)

		itemModelResolver.updateForTopItem(
			outerRenderState,
			contents.getOuter(),
			ItemDisplayContext.NONE,
			AaronClientUtil.localLevel,
			null,
			0
		)

		val time = (AaronClientUtil.localLevel?.gameTime ?: 0L).toFloat()

		renderCenter(poseStack, collector, lightCoords, overlayCoords, outlineColor, time)
		renderOuterItems(poseStack, collector, lightCoords, overlayCoords, outlineColor, time)
	}

	private fun renderCenter(
		poseStack: PoseStack,
		collector: SubmitNodeCollector,
		lightCoords: Int,
		overlayCoords: Int,
		outlineColor: Int,
		time: Float
	) {
		poseStack.withPose {
			poseStack.translate(0.5, 0.5, 0.5)
			poseStack.mulPose(Axis.YP.rotationDegrees(time * 4f))
			poseStack.mulPose(Axis.XP.rotationDegrees(time * 2f))
			poseStack.scale(0.5f, 0.5f, 0.5f)

			centerRenderState.submit(
				poseStack,
				collector,
				lightCoords,
				overlayCoords,
				outlineColor
			)
		}
	}

	private fun renderOuterItems(
		poseStack: PoseStack,
		collector: SubmitNodeCollector,
		lightCoords: Int,
		overlayCoords: Int,
		outlineColor: Int,
		time: Float
	) {
		for (index in 0 until OUTER_ITEM_COUNT) {
			poseStack.withPose {

				poseStack.translate(0.5, 0.5, 0.5)
				poseStack.mulPose(Axis.YP.rotationDegrees(index * 90f + time * 2f))
				poseStack.translate(0.3, 0.0, 0.0)
				poseStack.scale(0.25f, 0.25f, 0.25f)

				outerRenderState.submit(
					poseStack,
					collector,
					lightCoords,
					overlayCoords,
					outlineColor
				)

			}
		}
	}

	data object Unbaked : SpecialModelRenderer.Unbaked<ItemStack> {
		val CODEC: MapCodec<Unbaked> = MapCodec.unit(Unbaked)
		override fun type() = CODEC
		override fun bake(context: SpecialModelRenderer.BakingContext) = OpiniumCoreBEWLR()
	}

	companion object {
		private const val OUTER_ITEM_COUNT = 4
	}
}