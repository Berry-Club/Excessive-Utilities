package dev.aaronhowser.mods.excessive_utilities.block_entity.ender_porcupine

import net.minecraft.core.Direction
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class EnderPorcupineSimpleEnergyHandler(
	private val porcupine: EnderPorcupineBlockEntity,
	private val direction: Direction?
) : EnergyHandler {

	private fun getTarget(): EnergyHandler? = porcupine.findTargetEnergyHandler(direction)

	override fun getAmountAsLong(): Long = getTarget()?.getAmountAsLong() ?: 0L

	override fun getCapacityAsLong(): Long = getTarget()?.getCapacityAsLong() ?: 0L

	override fun insert(amount: Int, transaction: TransactionContext): Int {
		return getTarget()?.insert(amount, transaction) ?: 0
	}

	override fun extract(amount: Int, transaction: TransactionContext): Int =
		getTarget()?.extract(amount, transaction) ?: 0

}
