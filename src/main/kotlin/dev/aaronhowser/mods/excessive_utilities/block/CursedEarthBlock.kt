package dev.aaronhowser.mods.excessive_utilities.block

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.chance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isBlock
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.IntegerProperty

class CursedEarthBlock : Block(Properties.ofFullCopy(Blocks.GRASS_BLOCK)) {

	init {
		registerDefaultState(
			stateDefinition.any()
				.setValue(DISTANCE, 7)
		)
	}

	override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
		builder.add(DISTANCE)
	}

	override fun onPlace(state: BlockState, level: Level, pos: BlockPos, oldState: BlockState, movedByPiston: Boolean) {
		val distance = state.getValue(DISTANCE)
		if (distance <= 1) return

		for (dx in -1..1) for (dy in -1..1) for (dz in -1..1) {
			if (!level.random.chance(0.5)) continue

			val neighborPos = pos.offset(dx, dy, dz)
			if (!level.isEmptyBlock(neighborPos.above())) continue

			val neighborState = level.getBlockState(neighborPos)

			if (neighborState.isBlock(this)) {
				val neighborDistance = neighborState.getValue(DISTANCE)
				if (neighborDistance >= distance) continue
			} else if (!neighborState.isBlock(ModBlockTagsProvider.CURSED_EARTH_REPLACEABLE)) {
				continue
			}

			val newState = defaultBlockState().setValue(DISTANCE, distance - 1)
			level.setBlockAndUpdate(neighborPos, newState)
		}
	}

	companion object {
		val DISTANCE: IntegerProperty = BlockStateProperties.DISTANCE
	}

}