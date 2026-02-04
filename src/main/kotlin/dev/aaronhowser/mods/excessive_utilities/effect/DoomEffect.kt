package dev.aaronhowser.mods.excessive_utilities.effect

import dev.aaronhowser.mods.aaron.AaronExtensions.tell
import dev.aaronhowser.mods.excessive_utilities.registry.ModMobEffects
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity

class DoomEffect : MobEffect(
	MobEffectCategory.HARMFUL,
	0x000000
) {

	override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
		val remainingDuration = livingEntity.getEffect(ModMobEffects.DOOM)?.duration ?: return false

		if (remainingDuration % 20 == 0) {
			val seconds = remainingDuration / 20
			if (seconds == 0) {
				livingEntity.hurt(livingEntity.damageSources().magic(), Float.MAX_VALUE)
			} else {
				livingEntity.tell("$seconds")
			}
		}

		return true
	}

}