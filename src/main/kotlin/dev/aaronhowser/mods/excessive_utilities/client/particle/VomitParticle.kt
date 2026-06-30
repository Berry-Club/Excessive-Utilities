package dev.aaronhowser.mods.excessive_utilities.client.particle

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.particle.*
import net.minecraft.core.particles.SimpleParticleType

class VomitParticle(
	level: ClientLevel,
	x: Double,
	y: Double,
	z: Double,
	xSpeed: Double,
	ySpeed: Double,
	zSpeed: Double,
	sprites: SpriteSet
) : TextureSheetParticle(level, x, y, z, xSpeed, ySpeed, zSpeed) {

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
		pickSprite(sprites)
	}

	override fun getRenderType(): ParticleRenderType = ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT

	class Provider(private val sprites: SpriteSet) : ParticleProvider<SimpleParticleType> {
		override fun createParticle(
			type: SimpleParticleType,
			level: ClientLevel,
			x: Double,
			y: Double,
			z: Double,
			xSpeed: Double,
			ySpeed: Double,
			zSpeed: Double
		): Particle {
			return VomitParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites)
		}
	}

}
