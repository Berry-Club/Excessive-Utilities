package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.item.tier.OpiniumTier
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.SwordItem
import net.minecraft.world.item.component.Unbreakable

class KikokuItem(properties: Properties) : SwordItem(OpiniumTier, properties) {

	override fun hurtEnemy(stack: ItemStack, target: LivingEntity, attacker: LivingEntity): Boolean {
		return super.hurtEnemy(stack, target, attacker)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
			.setNoRepair()
			.component(DataComponents.UNBREAKABLE, Unbreakable(false))
			.attributes(createAttributes(OpiniumTier, -1f, -2.4f))
	}

}