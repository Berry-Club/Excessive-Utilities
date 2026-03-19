package dev.aaronhowser.mods.excessive_utilities.effect

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity

class GreekFireEffect : MobEffect(
	MobEffectCategory.HARMFUL,
	0x000000
) {

	override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean {
		return true
	}

	override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
		livingEntity.remainingFireTicks = 5
		return true
	}

}