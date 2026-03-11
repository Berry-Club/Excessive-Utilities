package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isFull
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotFull
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.ContainerHelper
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

	private fun getParentItemHandler(level: ServerLevel): IItemHandler? {
		return level.getCapability(Capabilities.ItemHandler.BLOCK, placedOnPos, placedOnDirection.opposite)
	}

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)
		didWorkThisTick = false


	}

	private fun pullFromParent(level: ServerLevel) {
		val parentHandle = getParentItemHandler(level) ?: return

		val stackInBuffer = bufferContainer.getItem(0)
		if (stackInBuffer.isFull()) return

	}

	companion object {
		const val BUFFER_CONTAINER_SIZE = 1
	}

}