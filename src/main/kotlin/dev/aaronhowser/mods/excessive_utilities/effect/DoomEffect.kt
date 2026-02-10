package dev.aaronhowser.mods.excessive_utilities.effect

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.datapack.ModDamageTypeProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMessageLang
import dev.aaronhowser.mods.excessive_utilities.registry.ModMobEffects
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity

class DoomEffect : MobEffect(
	MobEffectCategory.HARMFUL,
	0x000000
) {

	override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean {
		return true
	}

	override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
		if (livingEntity.isClientSide || livingEntity.isDeadOrDying) return true
		val remainingDuration = livingEntity.getEffect(ModMobEffects.DOOM)?.duration ?: return false

		if (remainingDuration % 20 == 0) {
			val seconds = remainingDuration / 20
			if (seconds % 10 == 0 || seconds <= 10) {
				val component = ModMessageLang.DOOM_EFFECT_TIME.toComponent(seconds)
				livingEntity.tell(component)
			}
		}

		if (remainingDuration <= 1) {
			livingEntity.hurt(livingEntity.damageSources().source(ModDamageTypeProvider.DOOM), Float.MAX_VALUE)
		}

		return true
	}

}