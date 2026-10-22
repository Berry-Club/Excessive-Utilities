package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class EnergyTrashCanBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.ENERGY_TRASH_CAN.get(), pos, state) {

	private val energyStorage: EnergyHandler =
		object : SimpleEnergyHandler(Int.MAX_VALUE) {
			override fun insert(amount: Int, transaction: TransactionContext): Int = amount
			override fun extract(amount: Int, transaction: TransactionContext): Int = 0
		}

	fun getEnergyStorage(direction: Direction?): EnergyHandler = energyStorage


}
