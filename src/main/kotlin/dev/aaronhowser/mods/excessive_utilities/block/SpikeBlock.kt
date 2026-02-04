package dev.aaronhowser.mods.excessive_utilities.block

import com.mojang.authlib.GameProfile
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.common.util.FakePlayerFactory
import java.util.*

class SpikeBlock(
	val damagePerHit: Float,
	val canKill: Boolean = true,
	val dropsExperience: Boolean = false,
	val killsAsPlayer: Boolean = false,
	properties: Properties
) : Block(properties) {

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		var shape = Shapes.empty()
		val layers = 5
		val layerHeight = 16.0 / layers
		val insetStep = 16.0 / (layers * 2.0)

		for (i in 0 until layers) {
			val yMin = i * layerHeight
			val yMax = yMin + layerHeight
			val inset = i * insetStep
			val min = inset
			val max = 16.0 - inset

			shape = Shapes.or(
				shape,
				box(
					min, yMin, min,
					max, yMax, max
				)
			)
		}

		return shape
	}


	override fun entityInside(state: BlockState, level: Level, pos: BlockPos, entity: Entity) {
		if (entity !is LivingEntity || level !is ServerLevel || level.gameTime % 10 != 0L) return

		if (!canKill && entity.health <= damagePerHit) {
			return
		}

		if (dropsExperience) {
			entity.lastHurtByPlayerTime = 100
		}

		if (killsAsPlayer) {
			val fakePlayer = FakePlayerFactory.get(
				level,
				GameProfile(UUID.fromString("21b80106-00e9-4bf0-b903-3a1caf2da923"), "EU_SpikeBlock_Killer")
			)

			entity.hurt(level.damageSources().playerAttack(fakePlayer), damagePerHit)
		} else {
			entity.hurt(level.damageSources().cactus(), damagePerHit)
		}
	}

}