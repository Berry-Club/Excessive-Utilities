package dev.aaronhowser.mods.excessive_utilities.block_entity

import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.transaction.Transaction

fun EnergyHandler.insertEnergy(amount: Int, simulate: Boolean): Int {
	if (simulate) {
		Transaction.openRoot().use { transaction ->
			return insert(amount, transaction)
		}
	}
	Transaction.openRoot().use { transaction ->
		val inserted = insert(amount, transaction)
		transaction.commit()
		return inserted
	}
}

fun EnergyHandler.extractEnergy(amount: Int, simulate: Boolean): Int {
	if (simulate) {
		Transaction.openRoot().use { transaction ->
			return extract(amount, transaction)
		}
	}
	Transaction.openRoot().use { transaction ->
		val extracted = extract(amount, transaction)
		transaction.commit()
		return extracted
	}
}
