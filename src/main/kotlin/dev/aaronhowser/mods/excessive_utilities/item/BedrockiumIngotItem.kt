package dev.aaronhowser.mods.excessive_utilities.item

import net.minecraft.world.InteractionResult
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.Level

class BedrockiumIngotItem(properties: Properties) : Item(properties) {

	override fun onItemUseFirst(stack: ItemStack, context: UseOnContext): InteractionResult {
		return super.onItemUseFirst(stack, context)
	}

	override fun inventoryTick(
		stack: ItemStack,
		level: Level,
		entity: Entity,
		slotId: Int,
		isSelected: Boolean
	) {
		if (level.isClientSide
			|| entity !is LivingEntity
			|| entity.hasInfiniteMaterials()
			|| level.gameTime % 20L != 0L
		) return

		if (entity.hasEffect(MobEffects.DAMAGE_BOOST)) {
			entity.removeEffect(MobEffects.MOVEMENT_SLOWDOWN)
		} else {
			entity.addEffect(MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2))
		}
	}

}