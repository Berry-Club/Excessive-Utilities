package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.item.tier.UnstableTier
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.AxeItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.Unbreakable
import net.minecraft.world.level.Level

class HealingAxeItem(properties: Properties) : AxeItem(UnstableTier, properties) {

	// 1 in 200 chance to heal the player by 1 hunger point (0.2 saturation) every tick while held
	override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {
		if (!isSelected || level.isClientSide || entity !is Player || !level.random.chance(1.0 / 100)) return

		entity.foodData.eat(1, 0.2f)
	}

	override fun onLeftClickEntity(
		stack: ItemStack,
		player: Player,
		entity: Entity
	): Boolean {
		if (entity.level().isClientSide || entity !is LivingEntity) return false

		val isUndead = entity.isEntity(EntityTypeTags.UNDEAD)
		val selfHurtDamage = ServerConfig.CONFIG.healingAxeHealthTransferAmount.get().toFloat()

		if (isUndead) {
			entity.hurt(player.damageSources().playerAttack(player), selfHurtDamage * 4f)
		} else {
			entity.heal(selfHurtDamage)
		}

		player.hurt(player.damageSources().playerAttack(player), selfHurtDamage)

		return true
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
			.component(DataComponents.UNBREAKABLE, Unbreakable(false))
			.attributes(createAttributes(UnstableTier, 5f, -3f))
	}

}