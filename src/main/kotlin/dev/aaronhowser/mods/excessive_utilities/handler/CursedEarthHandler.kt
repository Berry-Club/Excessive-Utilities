package dev.aaronhowser.mods.excessive_utilities.handler

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.randomX
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.randomY
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.randomZ
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModAttachmentTypes
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.Mob
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.entity.ai.attributes.Attributes

object CursedEarthHandler {

	val ATTACK_ATTRIBUTE_MODIFIER = ExcessiveUtilities.modResource("cursed_attack")
	val SPEED_ATTRIBUTE_MODIFIER = ExcessiveUtilities.modResource("cursed_speed")

	fun isCursed(entity: Entity): Boolean = entity.getData(ModAttachmentTypes.IS_CURSED)

	fun setCursed(entity: LivingEntity, value: Boolean) {
		if (isCursed(entity) == value) return
		entity.setData(ModAttachmentTypes.IS_CURSED, value)

		val attackAttribute = entity.getAttribute(Attributes.ATTACK_DAMAGE)
		val speedAttribute = entity.getAttribute(Attributes.MOVEMENT_SPEED)

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
		if (entity !is LivingEntity) return
		val level = entity.level()
		if (level.isServerSide) return

		if (!isCursed(entity)) return

		val random = entity.random
		val bb = entity.boundingBox

		val px = bb.randomX(random)
		val py = bb.randomY(random)
		val pz = bb.randomZ(random)

		level.addParticle(
			ParticleTypes.SMOKE,
			px, py, pz,
			0.0, 0.0, 0.0
		)
	}

	const val CURSED_ENTITY_TINT = 0.1f

	@JvmStatic
	fun darkenColor(color: Int): Int {
		val alpha = color and -0x1000000
		val red = darkenChannel(color shr 16)
		val green = darkenChannel(color shr 8)
		val blue = darkenChannel(color)

		return alpha or (red shl 16) or (green shl 8) or blue
	}

	private fun darkenChannel(color: Int): Int {
		return ((color and 0xFF) * CURSED_ENTITY_TINT).toInt()
	}

}