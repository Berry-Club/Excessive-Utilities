package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.block.base.DataDrivenGeneratorType
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModMobEffects
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ColorParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB

class DeathGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : DataDrivenGeneratorBlockEntity(ModBlockEntityTypes.DEATH_GENERATOR.get(), DataDrivenGeneratorType.DEATH, pos, blockState) {

	override fun effectOnSuccess(level: ServerLevel) {
		if (level.gameTime % 20 != 0L) return

		val pos = blockPos
		val radius = ServerConfig.CONFIG.deathGeneratorEffectRadius.get()
		val aabb = AABB(pos).inflate(radius.toDouble())

		val entities = level.getEntitiesOfClass(LivingEntity::class.java, aabb)

		for (entity in entities) {
			if (entity.hasInfiniteMaterials() || entity.hasEffect(ModMobEffects.DOOM)) continue

			val effect = MobEffectInstance(ModMobEffects.DOOM, 20 * 60)
			entity.addEffect(effect)
		}
	}

	override fun clientTick(level: Level) {
		val isActive = blockState.getValue(GeneratorBlock.LIT)
		if (!isActive) return

		val radius = ServerConfig.CONFIG.deathGeneratorEffectRadius.get()
		val particleCount = Mth.ceil(Math.PI * radius * radius) / 4

		val particle = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, 0xFF660000.toInt())

		for (i in 0 until particleCount) {
			val dx = level.random.nextRange(-radius, radius)
			val dy = level.random.nextRange(-radius, radius)
			val dz = level.random.nextRange(-radius, radius)

			val x = blockPos.x + 0.5 + dx
			val y = blockPos.y + 1.0 + dy
			val z = blockPos.z + 0.5 + dz

			level.addAlwaysVisibleParticle(
				particle,
				x, y, z,
				0.0, 0.0, 0.0
			)
		}
	}

}