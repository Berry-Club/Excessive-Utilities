package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.block.entity.base.GpSourceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerContribution
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class DragonEggMillBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : GpSourceBlockEntity(ModBlockEntityTypes.DRAGON_EGG_MILL.get(), pos, blockState) {

	override val gpGeneration: GridPowerContribution =
		object : GridPowerContribution {
			override fun getAmount(): Int {
				TODO("Not yet implemented")
			}

			override fun isStillValid(): Boolean {
				TODO("Not yet implemented")
			}

		}

}