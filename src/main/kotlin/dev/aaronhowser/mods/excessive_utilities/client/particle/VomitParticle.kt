package dev.aaronhowser.mods.excessive_utilities.client.particle

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.util.RandomSource

class VomitParticle(
	level: ClientLevel,
	x: Double,
	y: Double,
	z: Double,
	xSpeed: Double,
	ySpeed: Double,
	zSpeed: Double,
	sprites: SpriteSet
) : SingleQuadParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.get(level.random)) {

	init {
		xd = xSpeed
		yd = ySpeed
		zd = zSpeed
		gravity = 0.5f
		friction = 1f
		hasPhysics = true
		lifetime = 45 + random.nextInt(25)
		quadSize *= 0.55f + random.nextFloat() * 0.45f

		setColor(
			random.nextRange(0.22f, 0.34f),
			random.nextRange(0.75f, 0.95f),
			0.08f
		)

		setAlpha(0.9f)
	}

	override fun getLayer(): SingleQuadParticle.Layer = SingleQuadParticle.Layer.TRANSLUCENT

	class Provider(private val sprites: SpriteSet) : ParticleProvider.Sprite<SimpleParticleType> {
		override fun createParticle(
			type: SimpleParticleType,
			level: ClientLevel,
			x: Double,
			y: Double,
			z: Double,
			xSpeed: Double,
			ySpeed: Double,
			zSpeed: Double,
			random: RandomSource
		): SingleQuadParticle {
			return VomitParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites)
		}
	}

}
