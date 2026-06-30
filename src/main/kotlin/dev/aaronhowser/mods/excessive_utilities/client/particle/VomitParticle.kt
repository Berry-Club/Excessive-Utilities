package dev.aaronhowser.mods.excessive_utilities.client.particle

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
	private val sprites: SpriteSet
) : TextureSheetParticle(level, x, y, z, xSpeed, ySpeed, zSpeed) {

	private var stuckTicks = 0

	init {
		xd = xSpeed
		yd = ySpeed
		zd = zSpeed
		gravity = 0.08f
		friction = 0.92f
		hasPhysics = true
		lifetime = 45 + random.nextInt(25)
		quadSize *= 0.55f + random.nextFloat() * 0.45f
		setColor(0.22f + random.nextFloat() * 0.12f, 0.75f + random.nextFloat() * 0.2f, 0.08f)
		setAlpha(0.9f)
		pickSprite(sprites)
	}

	override fun tick() {
		val previousX = x
		val previousY = y
		val previousZ = z

		super.tick()

		if (stuckTicks > 0) {
			stuckTicks++
			xd = 0.0
			yd = 0.0
			zd = 0.0
			alpha = 0.9f * (1f - stuckTicks / 35f)

			if (stuckTicks >= 35) {
				remove()
			}

			return
		}

		val barelyMoved = x == previousX && y == previousY && z == previousZ
		if (onGround || barelyMoved) {
			stuckTicks = 1
			hasPhysics = false
			xd = 0.0
			yd = 0.0
			zd = 0.0
			setPos(previousX, previousY, previousZ)
		}
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
