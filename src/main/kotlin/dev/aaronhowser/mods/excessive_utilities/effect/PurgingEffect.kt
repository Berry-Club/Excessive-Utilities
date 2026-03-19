package dev.aaronhowser.mods.excessive_utilities.effect

import net.minecraft.world.effect.InstantenousMobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

class PurgingEffect : InstantenousMobEffect(
	MobEffectCategory.HARMFUL,
	0x000000
) {

	//TODO: Puke particles
	override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
		livingEntity.removeAllEffects()

		if (livingEntity is Player) {
			livingEntity.foodData.foodLevel = 0
			livingEntity.foodData.setSaturation(0f)
		}

		return true
	}

}