package dev.aaronhowser.mods.excessive_utilities.handler

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.tier.OpiniumTier
import dev.aaronhowser.mods.excessive_utilities.registry.ModAttributes
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.SwordItem.createAttributes
import net.minecraft.world.item.component.Unbreakable

object SoulRendHandler {

	val KIKOKU_PROPERTIES: () -> Properties = {
		Properties()
			.stacksTo(1)
			.setNoRepair()
			.component(DataComponents.UNBREAKABLE, Unbreakable(false))
			.attributes(
				createAttributes(OpiniumTier, -1f, -2.4f)
					.withModifierAdded(
						ModAttributes.SOUL_RENDING,
						AttributeModifier(
							ExcessiveUtilities.modResource("kikoku_rending"),
							2.0,
							AttributeModifier.Operation.ADD_VALUE
						),
						EquipmentSlotGroup.MAINHAND
					)
			)
	}

}