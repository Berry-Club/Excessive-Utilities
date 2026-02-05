package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isFluid
import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.tags.FluidTags
import net.minecraft.world.level.block.state.BlockState

class LavaMillBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpSourceBlockEntity(ModBlockEntityTypes.LAVA_MILL.get(), pos, blockState) {

	override fun getGpGeneration(): Int {
		val level = level ?: return 0

		val touchingLava = Direction.Plane.HORIZONTAL
			.stream()
			.anyMatch { dir ->
				val fluidStateThere = level.getFluidState(worldPosition.relative(dir))
				fluidStateThere.isFluid(FluidTags.LAVA)
			}

		return if (touchingLava) {
			ServerConfig.CONFIG.lavaMillGeneration.get()
		} else {
			0
		}
	}

}