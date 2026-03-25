package dev.aaronhowser.mods.excessive_utilities.client.render

import com.mojang.blaze3d.vertex.PoseStack
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.AngelRingItem
import net.minecraft.client.model.EntityModel
import net.minecraft.client.model.HumanoidModel
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.entity.RenderLayerParent
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.client.ICurioRenderer

class AngelWingRenderer : ICurioRenderer {

	override fun <T : LivingEntity, M : EntityModel<T?>> render(
		stack: ItemStack,
		slotContext: SlotContext,
		matrixStack: PoseStack,
		renderLayerParent: RenderLayerParent<T, M>,
		renderTypeBuffer: MultiBufferSource,
		light: Int,
		limbSwing: Float,
		limbSwingAmount: Float,
		partialTicks: Float,
		ageInTicks: Float,
		netHeadYaw: Float,
		headPitch: Float
	) {
		val entity = slotContext.entity
		val wingType = AngelRingItem.PLAYER_WINGS[entity.uuid] ?: return

		val parentModel = renderLayerParent.model as? HumanoidModel<*> ?: return

		val texture = ExcessiveUtilities.modResource("textures/entity/angel_wings/${wingType.id}.png")
		val consumer = renderTypeBuffer.getBuffer(RenderType.entityCutoutNoCull(texture))

	}

}