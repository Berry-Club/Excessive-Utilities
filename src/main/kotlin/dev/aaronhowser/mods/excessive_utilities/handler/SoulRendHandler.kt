package dev.aaronhowser.mods.excessive_utilities.handler

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.tier.OpiniumTier
import dev.aaronhowser.mods.excessive_utilities.registry.ModAttributes
import net.minecraft.core.component.DataComponents
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EquipmentSlotGroup
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes
import net.minecraft.world.item.Item.Properties
import net.minecraft.world.item.SwordItem.createAttributes
import net.minecraft.world.item.component.Unbreakable
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent

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

	private val SOUL_RENT_HEALTH: ResourceLocation = ExcessiveUtilities.modResource("soul_rent")

	fun handleIncomingDamage(event: LivingIncomingDamageEvent) {
		if (event.isCanceled) return

		val victim = event.entity
		if (victim.level().isClientSide) return

		val attacker = event.source.entity as? LivingEntity ?: return
		if (attacker == victim) return

		val attackerSoulRending = attacker.getAttributeValue(ModAttributes.SOUL_RENDING)
		if (attackerSoulRending <= 0.0) return

		val victimMaxHealthAttribute = victim.getAttribute(Attributes.MAX_HEALTH) ?: return
		val currentModifierAmount = victimMaxHealthAttribute.getModifier(SOUL_RENT_HEALTH)?.amount ?: 0.0

		victimMaxHealthAttribute.addOrReplacePermanentModifier(
			AttributeModifier(
				SOUL_RENT_HEALTH,
				currentModifierAmount - attackerSoulRending,
				AttributeModifier.Operation.ADD_VALUE
			)
		)

		if (victim.getAttributeValue(Attributes.MAX_HEALTH) <= 0.0) {
			victim.kill()
		}

		spawnSoulParticles(victim)
	}

	fun lowerSoulRend(entity: Entity) {
		if (entity !is LivingEntity
			|| entity.isClientSide
			|| entity.level().gameTime % 60 != 0L
		) return

		val maxHealthAttribute = entity.getAttribute(Attributes.MAX_HEALTH) ?: return

		val currentModifier = maxHealthAttribute.getModifier(SOUL_RENT_HEALTH) ?: return
		val currentAmount = currentModifier.amount
		val nextAmount = currentAmount + 1

		if (nextAmount >= 0.0) {
			maxHealthAttribute.removeModifier(SOUL_RENT_HEALTH)
			return
		}

		maxHealthAttribute.addOrUpdateTransientModifier(
			AttributeModifier(
				SOUL_RENT_HEALTH,
				nextAmount,
				AttributeModifier.Operation.ADD_VALUE
			)
		)
	}

	private fun spawnSoulParticles(victim: LivingEntity) {
		val level = victim.level() as? ServerLevel ?: return
		val bb = victim.boundingBox

		level.sendParticles(
			ParticleTypes.SCULK_SOUL,
			victim.x,
			victim.y + victim.bbHeight * 0.5,
			victim.z,
			8,
			bb.xsize * 0.5,
			victim.bbHeight * 0.35,
			bb.zsize * 0.5,
			0.02
		)
	}

}
