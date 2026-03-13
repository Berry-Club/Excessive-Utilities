package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.menu.single_slot.SingleSlotMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.util.ContainerContainer
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.wrapper.InvWrapper

class MiniChestBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(ModBlockEntityTypes.MINI_CHEST.get(), pos, blockState), MenuProvider, ContainerContainer {

	val container = ImprovedSimpleContainer(this, 1)
	override fun getContainers(): List<Container> = listOf(container)
	private val invWrapper = InvWrapper(container)

	fun getItemHandler(direction: Direction?): InvWrapper = invWrapper

	override fun getDisplayName(): Component = blockState.block.name

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return SingleSlotMenu(containerId, playerInventory, container)
	}

}