package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.aaron.registry.AaronMobEffectsRegistry
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.effect.*
import net.minecraft.core.registries.Registries
import net.minecraft.world.effect.MobEffect
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

}