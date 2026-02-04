package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.excessive_utilities.block.entity.base.ConfigurableFluidTank
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import java.util.function.IntSupplier

class DrumBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.CHANDELIER.get(), pos, blockState) {

	private val capacityGetter: IntSupplier =
		when (blockState.block) {
			ModBlocks.STONE_DRUM.get() -> IntSupplier { ServerConfig.CONFIG.stoneDrumCapacity.get() }
			ModBlocks.IRON_DRUM.get() -> IntSupplier { ServerConfig.CONFIG.ironDrumCapacity.get() }
			ModBlocks.REINFORCED_LARGE_DRUM.get() -> IntSupplier { ServerConfig.CONFIG.reinforcedLargeDrumCapacity.get() }
			ModBlocks.DEMONICALLY_GARGANTUAN_DRUM.get() -> IntSupplier { ServerConfig.CONFIG.demonicallyGargantuanDrumCapacity.get() }
			ModBlocks.CREATIVE_DRUM.get() -> IntSupplier { Int.MAX_VALUE }

			else -> IntSupplier { 0 }
		}

	val tank: ConfigurableFluidTank =
		object : ConfigurableFluidTank(capacityGetter) {
			override fun onContentsChanged() {
				setChanged()
			}
		}

}