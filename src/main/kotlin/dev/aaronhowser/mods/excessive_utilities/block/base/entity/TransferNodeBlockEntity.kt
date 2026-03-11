package dev.aaronhowser.mods.excessive_utilities.block.base.entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.ContainerContainer
import net.minecraft.core.BlockPos
import net.minecraft.world.Container
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

abstract class TransferNodeBlockEntity(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(type, pos, blockState), ContainerContainer {

	protected val upgradeContainer = ImprovedSimpleContainer(this, UPGRADE_CONTAINER_SIZE)

	override fun getContainers(): List<Container> {
		return listOf(upgradeContainer)
	}

	companion object {
		const val UPGRADE_CONTAINER_SIZE = 5
	}

}