package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.world.Container
import net.minecraft.world.level.block.state.BlockState

class ItemTransferNodeBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : TransferNodeBlockEntity(ModBlockEntityTypes.ITEM_TRANSFER_NODE.get(), pos, blockState) {

	private val bufferContainer = ImprovedSimpleContainer(this, BUFFER_CONTAINER_SIZE)

	override fun getContainers(): List<Container> {
		return listOf(bufferContainer, upgradeContainer)
	}

	companion object {
		const val BUFFER_CONTAINER_SIZE = 1
	}

}