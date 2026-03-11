package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isFull
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.items.IItemHandler

class ItemTransferNodeBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : TransferNodeBlockEntity(ModBlockEntityTypes.ITEM_TRANSFER_NODE.get(), pos, blockState) {

	private val bufferContainer = ImprovedSimpleContainer(this, BUFFER_CONTAINER_SIZE)

	override fun getContainers(): List<Container> {
		return listOf(bufferContainer, upgradeContainer)
	}

	fun hasStackUpgrade(): Boolean {
		return upgradeContainer.countItem(ModItems.STACK_UPGRADE.get()) > 0
	}

	private fun getParentItemHandler(level: ServerLevel): IItemHandler? {
		return level.getCapability(Capabilities.ItemHandler.BLOCK, placedOnPos, placedOnDirection.opposite)
	}

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)
		didWorkThisTick = false


	}

	private fun pullFromParent(level: ServerLevel) {
		val parentHandler = getParentItemHandler(level) ?: return

		val stackInBuffer = bufferContainer.getItem(0)
		if (stackInBuffer.isFull()) return

		var amountToExtract = if (hasStackUpgrade()) 64 else 1

		if (stackInBuffer.isEmpty) {
			for (slot in 0 until parentHandler.slots) {
				val simExtract = parentHandler.extractItem(slot, amountToExtract, true)
				if (simExtract.isEmpty) continue

				val extracted = parentHandler.extractItem(slot, amountToExtract, false)
				bufferContainer.setItem(0, extracted)
				didWorkThisTick = true
				break
			}

			return
		}

		amountToExtract = amountToExtract.coerceAtMost(stackInBuffer.maxStackSize - stackInBuffer.count)

		for (slot in 0 until parentHandler.slots) {
			val simExtract = parentHandler.extractItem(slot, amountToExtract, true)
			if (simExtract.isEmpty || !ItemStack.isSameItemSameComponents(simExtract, stackInBuffer)) continue

			val extracted = parentHandler.extractItem(slot, amountToExtract, false)
			val newStack = stackInBuffer.copy()
			newStack.count += extracted.count
			bufferContainer.setItem(0, newStack)
			didWorkThisTick = true
		}
	}

	companion object {
		const val BUFFER_CONTAINER_SIZE = 1
	}

}