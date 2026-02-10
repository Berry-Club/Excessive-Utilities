package dev.aaronhowser.mods.excessive_utilities.block.entity.generator

import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.block.base.DataDrivenGeneratorType
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.particles.ColorParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB

class NetherStarGeneratorBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : DataDrivenGeneratorBlockEntity(ModBlockEntityTypes.NETHER_STAR_GENERATOR.get(), DataDrivenGeneratorType.NETHER_STAR, pos, blockState) {

	override fun effectOnSuccess(level: ServerLevel) {
		if (level.gameTime % 20 != 0L) return

		val pos = blockPos
		val radius = 10
		val aabb = AABB(pos).inflate(radius.toDouble())

		val entities = level.getEntitiesOfClass(LivingEntity::class.java, aabb)

		for (entity in entities) {
			if (entity.hasInfiniteMaterials()) continue

			val effect = MobEffectInstance(MobEffects.WITHER, 20 * 5, 1)
			entity.addEffect(effect)
		}
	}

	override fun clientTick(level: Level) {
		val isActive = blockState.getValue(GeneratorBlock.LIT)
		if (!isActive) return

		val particle = ColorParticleOption.create(ParticleTypes.ENTITY_EFFECT, -1)
		level.addAlwaysVisibleParticle(
			particle,
			blockPos.x.toDouble(),
			blockPos.y + 1.0,
			blockPos.z.toDouble(),
			0.0,
			0.0,
			0.0
		)
	}

}