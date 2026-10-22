package dev.aaronhowser.mods.excessive_utilities.effect

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.server.level.ServerLevel

class GreekFireEffect : MobEffect(
	MobEffectCategory.HARMFUL,
	0xEF6094
) {

	override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean {
		return true
	}

	override fun applyEffectTick(level: ServerLevel, livingEntity: LivingEntity, amplifier: Int): Boolean {
		if (livingEntity.remainingFireTicks == 0) {
			livingEntity.remainingFireTicks = 15
		}

		return true
	}

}