package dev.aaronhowser.mods.excessive_utilities.block

import net.minecraft.core.BlockPos
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.TransparentBlock
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.pathfinder.PathComputationType
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.EntityCollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape

open class SemiPermeableGlassBlock(
	isSolidForMobsOnly: Boolean,
	properties: Properties = Properties.ofFullCopy(Blocks.GLASS)
) : TransparentBlock(properties) {

	val mobsPassThrough: Boolean = !isSolidForMobsOnly
	val playersPassThrough: Boolean = isSolidForMobsOnly

	override fun getCollisionShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		if (context !is EntityCollisionContext) return Shapes.block()
		val entity = context.entity ?: return Shapes.block()

		return when {
			entity is Player && playersPassThrough -> Shapes.empty()
			entity !is Player && mobsPassThrough -> Shapes.empty()
			else -> Shapes.block()
		}
	}

	override fun isPathfindable(state: BlockState, pathComputationType: PathComputationType): Boolean {
		return when (pathComputationType) {
			PathComputationType.LAND -> !mobsPassThrough
			PathComputationType.WATER -> false
			PathComputationType.AIR -> mobsPassThrough
		}
	}

	class Dark(
		isSolidForMobsOnly: Boolean,
		properties: Properties = Properties.ofFullCopy(Blocks.GLASS)
	) : SemiPermeableGlassBlock(isSolidForMobsOnly, properties) {

		override fun propagatesSkylightDown(state: BlockState, level: BlockGetter, pos: BlockPos): Boolean {
			return false
		}

		override fun getLightBlock(state: BlockState, level: BlockGetter, pos: BlockPos): Int {
			return level.maxLightLevel
		}

	}

}