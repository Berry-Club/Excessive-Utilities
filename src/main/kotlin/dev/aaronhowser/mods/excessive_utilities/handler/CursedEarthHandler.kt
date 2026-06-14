package dev.aaronhowser.mods.excessive_utilities.handler

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModAttachmentTypes
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

object CursedEarthHandler {

	val ATTACK_ATTRIBUTE_MODIFIER = ExcessiveUtilities.modResource("cursed_attack")
	val SPEED_ATTRIBUTE_MODIFIER = ExcessiveUtilities.modResource("cursed_speed")

	fun isCursed(entity: Entity): Boolean = entity.getData(ModAttachmentTypes.IS_CURSED)

	fun setCursed(mob: Mob, value: Boolean) {
		if (isCursed(mob) == value) return
		mob.setData(ModAttachmentTypes.IS_CURSED, value)

		val attackAttribute = mob.getAttribute(Attributes.ATTACK_DAMAGE)
		val speedAttribute = mob.getAttribute(Attributes.MOVEMENT_SPEED)

		if (value) {
			attackAttribute?.addPermanentModifier(
				AttributeModifier(
					ATTACK_ATTRIBUTE_MODIFIER,
					ServerConfig.CONFIG.cursedEarthBonusStrength.get(),
					AttributeModifier.Operation.ADD_MULTIPLIED_BASE
				)
			)

			speedAttribute?.addPermanentModifier(
				AttributeModifier(
					SPEED_ATTRIBUTE_MODIFIER,
					ServerConfig.CONFIG.cursedEarthBonusSpeed.get(),
					AttributeModifier.Operation.ADD_MULTIPLIED_BASE
				)
			)
		} else {
			attackAttribute?.removeModifier(ATTACK_ATTRIBUTE_MODIFIER)
			speedAttribute?.removeModifier(SPEED_ATTRIBUTE_MODIFIER)
		}
	}

	fun spawnParticles(entity: Entity) {
		if (entity !is Mob) return
		val level = entity.level()
		if (level.isServerSide) return

		if (!isCursed(entity)) return

		val random = entity.random

		val px = entity.x + random.nextDouble() * entity.bbWidth
		val py = entity.y + random.nextDouble() * entity.bbHeight
		val pz = entity.z + random.nextDouble() * entity.bbWidth

		level.addParticle(
			ParticleTypes.SMOKE,
			px, py, pz,
			0.0, 0.0, 0.0
		)
	}

}