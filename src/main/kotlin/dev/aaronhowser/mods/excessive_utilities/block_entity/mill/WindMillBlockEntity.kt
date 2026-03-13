package dev.aaronhowser.mods.excessive_utilities.block_entity.mill

import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.sin

class WindMillBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpSourceBlockEntity(ModBlockEntityTypes.WIND_MILL.get(), pos, blockState) {

	override fun getGpGeneration(): Double {
		val level = level ?: return 0.0
		if (!level.isEmptyBlock(blockPos.north()) || !level.isEmptyBlock(blockPos.south())) return 0.0

		val time = level.gameTime

		val base = 0.5
		val bonus = sin(time / (20.0 * 60 * 5)) + 1.0 // Slowly oscillating bonus between 0 and 2 over a 5 minute period
		val weatherBonus = when {
			level.isRaining -> 1.0
			level.isThundering -> 2.0
			else -> 0.0
		}

		return base + bonus + weatherBonus
	}

}