package dev.aaronhowser.mods.excessive_utilities.effect

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isDamageSource
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.phys.Vec3
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent

class OilyEffect : MobEffect(
	MobEffectCategory.HARMFUL,
	0x1E377E
) {

	override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean {
		return true
	}

	override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
		if (livingEntity is Player && !livingEntity.abilities.flying) {
			if (livingEntity.level().isRainingAt(livingEntity.blockPosition())) {
				livingEntity.addDeltaMovement(
					Vec3(
						0.0,
						livingEntity.gravity * 1.1,
						0.0
					)
				)
			}
		}

		return true
	}

	companion object {
		fun handleIncomingDamage(event: LivingIncomingDamageEvent) {
			if (event.isCanceled) return
			if (!event.source.isDamageSource(DamageTypeTags.IS_FIRE)) return

			event.amount *= ServerConfig.CONFIG.oilyFireDamageFactor.get().toFloat()
		}
	}

}