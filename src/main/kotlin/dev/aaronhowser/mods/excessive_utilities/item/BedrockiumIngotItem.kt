package dev.aaronhowser.mods.excessive_utilities.item

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class BedrockiumIngotItem(properties: Properties) : Item(properties) {

	override fun inventoryTick(
		stack: ItemStack,
		level: ServerLevel,
		entity: Entity,
		slot: EquipmentSlot?
	) {
		if (level.isClientSide
			|| entity !is LivingEntity
			|| entity.hasInfiniteMaterials()
			|| level.gameTime % 20L != 0L
		) return

		entity.addEffect(MobEffectInstance(MobEffects.SLOWNESS, 45, 2))
	}

}






