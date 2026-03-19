package dev.aaronhowser.mods.excessive_utilities.effect

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.excessive_utilities.registry.ModMobEffects.SECOND_CHANCE
import net.minecraft.network.chat.Component
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent

class SecondChanceEffect : MobEffect(
	MobEffectCategory.BENEFICIAL,
	0x92FAF0
) {

	override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean {
		return true
	}

	// TODO: Remove itself if the entity is marked as having used it already
	override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
		return super.applyEffectTick(livingEntity, amplifier)
	}

	companion object {
		fun cancelDeath(event: LivingIncomingDamageEvent) {
			if (event.isCanceled) return

			val entity = event.entity
			if (!entity.hasEffect(SECOND_CHANCE)) return

			val damageAmount = event.amount
			if (damageAmount < entity.health) return

			event.isCanceled = true
			entity.removeEffect(SECOND_CHANCE)
			entity.health = entity.maxHealth
			entity.addEffect(
				MobEffectInstance(
					MobEffects.WEAKNESS,
					20 * 10
				)
			)

			entity.level()
				.playSound(
					null,
					entity.x,
					entity.y,
					entity.z,
					SoundEvents.TOTEM_USE,
					entity.soundSource,
					1f,
					1f
				)

			entity.tell(Component.literal("Your second chance has been used up!"))
		}
	}

}