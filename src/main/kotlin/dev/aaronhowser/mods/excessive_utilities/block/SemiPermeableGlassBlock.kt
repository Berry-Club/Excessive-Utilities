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
	private val isInverted: Boolean,
	properties: Properties = Properties.ofFullCopy(Blocks.GLASS)
) : TransparentBlock(properties) {

	override fun getCollisionShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
		if (context !is EntityCollisionContext) return Shapes.block()
		val entity = context.entity ?: return Shapes.block()

		val canPassThrough = if (entity is Player) {
			entity.isShiftKeyDown == isInverted
		} else {
			isInverted
		}

		return if (canPassThrough) Shapes.empty() else Shapes.block()
	}

	override fun isPathfindable(state: BlockState, pathComputationType: PathComputationType): Boolean {
		return when (pathComputationType) {
			PathComputationType.LAND -> isInverted
			PathComputationType.WATER -> false
			PathComputationType.AIR -> isInverted
		}
	}

	class Dark(
		isInverted: Boolean,
		properties: Properties = Properties.ofFullCopy(Blocks.GLASS)
	) : SemiPermeableGlassBlock(isInverted, properties) {


	}

}











