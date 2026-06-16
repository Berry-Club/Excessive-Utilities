package dev.aaronhowser.mods.excessive_utilities.block_entity.base

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

interface MachineWithBuffer {

	fun collectReceivingItemHandlers(level: ServerLevel): Collection<IItemHandler>

	fun pushOutItems(
		level: ServerLevel,
		bufferContainer: Container,
		bufferItemHandler: IItemHandler
	) {
		if (bufferContainer.isEmpty) return

		val targets = collectReceivingItemHandlers(level)
		for (target in targets) {
			if (bufferContainer.isEmpty) break

			for (slot in 0 until bufferItemHandler.slots) {
				val stack = bufferItemHandler.getStackInSlot(slot)
				if (stack.isEmpty) continue

				val simRemainder = ItemHandlerHelper.insertItemStacked(target, stack, true)
				val amountAccepted = stack.count - simRemainder.count
				if (amountAccepted > 0) {
					val extracted = bufferItemHandler.extractItem(slot, amountAccepted, false)
					ItemHandlerHelper.insertItemStacked(target, extracted, false)
				}
			}
		}

	}

}