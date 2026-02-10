package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.block.base.DataDrivenGeneratorType
import dev.aaronhowser.mods.excessive_utilities.block.entity.generator.DataDrivenGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.entity.LivingEntity
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

}