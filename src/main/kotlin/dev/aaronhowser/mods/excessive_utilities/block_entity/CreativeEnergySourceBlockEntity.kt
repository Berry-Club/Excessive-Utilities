package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class CreativeEnergySourceBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.CREATIVE_ENERGY_SOURCE.get(), pos, blockState) {

	val energyStorage: EnergyHandler =
		object : SimpleEnergyHandler(Int.MAX_VALUE) {
			override fun insert(amount: Int, transaction: TransactionContext): Int = 0
			override fun extract(amount: Int, transaction: TransactionContext): Int = amount
		}

	private fun serverTick(level: ServerLevel) {
		for (dir in Direction.entries) {
			val energyCap = level.getCapability(
				Capabilities.Energy.BLOCK,
				blockPos.relative(dir),
				dir.opposite
			) ?: continue

			energyCap.insertEnergy(Int.MAX_VALUE, false)
		}
	}

	companion object {
		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: CreativeEnergySourceBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			}
		}

		fun getEnergyCapability(source: CreativeEnergySourceBlockEntity, direction: Direction?): EnergyHandler {
			return source.energyStorage
		}
	}

}
