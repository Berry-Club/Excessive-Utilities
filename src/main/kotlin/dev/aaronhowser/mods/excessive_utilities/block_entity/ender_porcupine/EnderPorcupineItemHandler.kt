package dev.aaronhowser.mods.excessive_utilities.block_entity.ender_porcupine

import net.minecraft.core.Direction
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.items.IItemHandler

class EnderPorcupineItemHandler(
	private val porcupine: EnderPorcupineBlockEntity,
	private val direction: Direction?
) : IItemHandler {

	private fun getTarget(): IItemHandler? = porcupine.findTargetItemHandler(direction)

	override fun getSlots(): Int = getTarget()?.slots ?: 0

	override fun getStackInSlot(slot: Int): ItemStack = getTarget()?.getStackInSlot(slot) ?: ItemStack.EMPTY

	override fun insertItem(slot: Int, stack: ItemStack, simulate: Boolean): ItemStack {
		return getTarget()?.insertItem(slot, stack, simulate) ?: stack
	}

	override fun extractItem(slot: Int, amount: Int, simulate: Boolean): ItemStack {
		return getTarget()?.extractItem(slot, amount, simulate) ?: ItemStack.EMPTY
	}

	override fun getSlotLimit(slot: Int): Int = getTarget()?.getSlotLimit(slot) ?: 0

	override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
		return getTarget()?.isItemValid(slot, stack) ?: false
	}

}