package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import org.joml.Vector3f

class EnderMarkerBlock : Block(Properties.ofFullCopy(Blocks.OBSIDIAN)) {

	override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		return SHAPE
	}

	override fun useWithoutItem(
		state: BlockState,
		level: Level,
		pos: BlockPos,
		player: Player,
		hitResult: BlockHitResult
	): InteractionResult {
		if (level.isServerSide) return InteractionResult.PASS

		val distanceToSearch = ServerConfig.CONFIG.enderQuarryMarkerSearchDistance.get()
		val otherPositions = mutableListOf<BlockPos>()

		for (dir in Direction.Plane.HORIZONTAL) {
			var distanceSearched = 0
			val posThere = pos.relative(dir).mutable()

			while (distanceSearched < distanceToSearch) {
				val stateThere = level.getBlockState(posThere)
				distanceSearched++

				if (stateThere.isBlock(this)) {
					otherPositions.add(posThere.immutable())
					break
				}
			}
		}

		if (otherPositions.isEmpty()) return InteractionResult.PASS

		val particle = DustParticleOptions(Vector3f(), 1f)

		val y = pos.y + 0.8

		for (otherPos in otherPositions) {
			var x = pos.x + 0.5
			var z = pos.z + 0.5

			if (otherPos.x == pos.x) {
				while (z < otherPos.z + 0.5) {
					level.addParticle(particle, x, y, z, 0.0, 0.0, 0.0)
					z += 0.1
				}
			} else {
				while (x < otherPos.x + 0.5) {
					level.addParticle(particle, x, y, z, 0.0, 0.0, 0.0)
					x += 0.1
				}
			}
		}

		return InteractionResult.SUCCESS
	}

	companion object {
		val SHAPE: VoxelShape = box(6.0, 0.0, 6.0, 10.0, 16.0, 10.0)
	}

}