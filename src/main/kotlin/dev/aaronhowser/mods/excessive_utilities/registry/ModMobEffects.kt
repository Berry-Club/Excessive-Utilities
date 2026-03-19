package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronMobEffectsRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.effect.*
import net.minecraft.core.registries.Registries
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.effect.MobEffectInstance
import net.neoforged.neoforge.event.entity.living.MobEffectEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModMobEffects : AaronMobEffectsRegistry() {

	val EFFECT_REGISTRY: DeferredRegister<MobEffect> =
		DeferredRegister.create(Registries.MOB_EFFECT, ExcessiveUtilities.MOD_ID)

	override fun getMobEffectRegistry(): DeferredRegister<MobEffect> = EFFECT_REGISTRY

	val GRAVITY: DeferredHolder<MobEffect, GravityEffect> =
		register("gravity", ::GravityEffect)
	val DOOM =
		register("doom", ::DoomEffect)
	val OILY: DeferredHolder<MobEffect, OilyEffect> =
		register("oily", ::OilyEffect)
	val GREEK_FIRE: DeferredHolder<MobEffect, GreekFireEffect> =
		register("greek_fire", ::GreekFireEffect)
	val LOVE: DeferredHolder<MobEffect, LoveEffect> =
		register("love", ::LoveEffect)
	val SECOND_CHANCE: DeferredHolder<MobEffect, SecondChanceEffect> =
		register("second_chance", ::SecondChanceEffect)
	val PURGING: DeferredHolder<MobEffect, PurgingEffect> =
		register("purging", ::PurgingEffect)
	val RELAPSE: DeferredHolder<MobEffect, out MobEffect> =
		registerSimple("relapse", MobEffectCategory.HARMFUL, 0x814FF7)

	fun handleRelapse(event: MobEffectEvent.Remove) {
		if (event.isCanceled) return
		val effectInstance = event.effectInstance ?: return
		if (effectInstance.effect.value().category != MobEffectCategory.HARMFUL) return

		val entity = event.entity
		if (!entity.hasEffect(RELAPSE)) return

		val duration = effectInstance.duration
		if (duration <= 2) return

		val newEffectInstance = MobEffectInstance(
			effectInstance.effect,
			duration / 2,
			effectInstance.amplifier,
			effectInstance.isAmbient,
			effectInstance.isVisible,
			effectInstance.showIcon()
		)

		entity.forceAddEffect(
			newEffectInstance,
			entity
		)

		event.isCanceled = true
	}

}