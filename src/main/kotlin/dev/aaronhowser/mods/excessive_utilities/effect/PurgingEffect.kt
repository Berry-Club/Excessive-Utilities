package dev.aaronhowser.mods.excessive_utilities.effect

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isClientSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.excessive_utilities.config.ClientConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModMobEffectTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModParticleTypes
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player

class PurgingEffect : MobEffect(
	MobEffectCategory.HARMFUL,
	0x43B02A
) {

	override fun shouldApplyEffectTickThisTick(duration: Int, amplifier: Int): Boolean = true

	override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
		if (livingEntity.isClientSide) {
			spawnVomitParticles(livingEntity, amplifier)
			return true
		}

		removeOtherEffects(livingEntity)

		if (livingEntity is Player) {
			livingEntity.foodData.foodLevel = 0
			livingEntity.foodData.setSaturation(0f)
		}

		return true
	}

	private fun removeOtherEffects(livingEntity: LivingEntity) {
		val effectsToRemove = livingEntity.activeEffects
			.map { it.effect }
			.filterNot { it.`is`(ModMobEffectTagsProvider.PURGING_BLACKLIST) }

		for (effect in effectsToRemove) {
			livingEntity.removeEffect(effect)
		}
	}

	private fun spawnVomitParticles(livingEntity: LivingEntity, amplifier: Int) {
		if (ClientConfig.CONFIG.disableVomit.get()) return

		val level = livingEntity.level()
		val lookAngle = livingEntity.lookAngle
		val random = livingEntity.random
		val particleCount = 4 + amplifier.coerceAtMost(4)

		for (i in 0 until particleCount) {
			val velocity = lookAngle
				.scale(random.nextRange(0.25, 0.8))
				.xRot(random.nextRange(-0.1f, 0.2f))
				.yRot(random.nextRange(-0.2f, 0.2f))
				.add(0.0, 0.04, 0.0)

			val spawnPos = livingEntity.eyePosition
				.add(lookAngle.scale(0.35))
				.add(0.0, -0.15, 0.0)

			level.addParticle(
				ModParticleTypes.VOMIT.get(),
				spawnPos.x,
				spawnPos.y,
				spawnPos.z,
				velocity.x,
				velocity.y,
				velocity.z
			)
		}
	}

}
