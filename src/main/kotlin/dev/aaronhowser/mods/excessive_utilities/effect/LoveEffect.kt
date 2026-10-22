package dev.aaronhowser.mods.excessive_utilities.effect

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.animal.Animal
import net.minecraft.server.level.ServerLevel

class LoveEffect : MobEffect(
	MobEffectCategory.BENEFICIAL,
	0xF1AAFF
) {

	override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean {
		return true
	}

	override fun applyEffectTick(level: ServerLevel, livingEntity: LivingEntity, amplifier: Int): Boolean {
		if (livingEntity is Animal) {
			livingEntity.inLoveTime = 10
		}

		return true
	}

}