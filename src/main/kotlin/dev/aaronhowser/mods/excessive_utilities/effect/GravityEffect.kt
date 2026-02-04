package dev.aaronhowser.mods.excessive_utilities.effect

import net.minecraft.world.effect.MobEffect
import net.minecraft.world.effect.MobEffectCategory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.HitResult
import kotlin.math.abs

class GravityEffect : MobEffect(
	MobEffectCategory.HARMFUL,
	0xFFFFFF
) {

	override fun applyEffectTick(livingEntity: LivingEntity, amplifier: Int): Boolean {
		val level = livingEntity.level()
		val clip = level.clip(
			ClipContext(
				livingEntity.position(),
				livingEntity.position().subtract(0.0, 200.0, 0.0),
				ClipContext.Block.COLLIDER,
				ClipContext.Fluid.NONE,
				livingEntity
			)
		)

		if (clip.type == HitResult.Type.BLOCK) {
			val distance = abs(livingEntity.position().y - clip.location.y)
		}

		return true
	}

}