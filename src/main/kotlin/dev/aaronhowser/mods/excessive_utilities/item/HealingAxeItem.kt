package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isEntity
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.item.tier.UnstableTier
import net.minecraft.core.component.DataComponents
import net.minecraft.tags.EntityTypeTags
import net.minecraft.world.entity.Entity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.component.ItemAttributeModifiers
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class HealingAxeItem(properties: Properties) : Item(properties) {

	override fun inventoryTick(stack: ItemStack, level: ServerLevel, entity: Entity, slot: EquipmentSlot?) {
		if (slot != EquipmentSlot.MAINHAND || entity !is Player) return
		val chance = ServerConfig.CONFIG.healingAxeChancePerTick.get()
		if (!level.random.chance(chance)) return

		entity.foodData.eat(
			ServerConfig.CONFIG.healingAxeFoodAmount.get(),
			ServerConfig.CONFIG.healingAxeSaturationAmount.get().toFloat()
		)
	}

	override fun onLeftClickEntity(
		stack: ItemStack,
		player: Player,
		entity: Entity
	): Boolean {
		val level = entity.level()
		if (level !is ServerLevel || entity !is LivingEntity) return false

		val isUndead = entity.isEntity(EntityTypeTags.UNDEAD)
		val selfHurtDamage = ServerConfig.CONFIG.healingAxeHealthTransferAmount.get().toFloat()

		if (isUndead) {
			entity.hurtServer(level, player.damageSources().playerAttack(player), selfHurtDamage * 4f)
		} else {
			entity.heal(selfHurtDamage)
		}

		player.hurtServer(level, player.damageSources().playerAttack(player), selfHurtDamage)

		return true
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()
			.stacksTo(1)
			.durability(UnstableTier.durability())
			.attributes(ItemAttributeModifiers.builder()
				.add(Attributes.ATTACK_DAMAGE, AttributeModifier(Item.BASE_ATTACK_DAMAGE_ID, UnstableTier.attackDamageBonus() + 5.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.ATTACK_SPEED, AttributeModifier(Item.BASE_ATTACK_SPEED_ID, -3.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.ENTITY_INTERACTION_RANGE, AttributeModifier(ExcessiveUtilities.modResource("healing_axe_range"), 4.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.add(Attributes.BLOCK_INTERACTION_RANGE, AttributeModifier(ExcessiveUtilities.modResource("healing_axe_range"), 4.0, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
				.build())
	}

}




