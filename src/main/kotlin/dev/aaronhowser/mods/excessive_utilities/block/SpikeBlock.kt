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
import net.minecraft.world.phys.shapes.VoxelShape
import net.neoforged.neoforge.common.util.FakePlayerFactory
import java.util.*

class SpikeBlock(
	val damagePerHit: Float,
	val canKill: Boolean = true,
	val dropsExperience: Boolean = false,
	val killsAsPlayer: Boolean = false ,
	properties: Properties
) : Block(properties) {

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return box(4.0, 4.0, 4.0, 5.0, 5.0, 5.0)
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