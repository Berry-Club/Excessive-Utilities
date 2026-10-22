package dev.aaronhowser.mods.excessive_utilities.block_entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.item.FluidFilterItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.network.chat.Component
import net.minecraft.world.ContainerHelper
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.VoidingResourceHandler
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

class FluidTrashCanBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.FLUID_TRASH_CAN.get(), pos, state), MenuProvider {

	private val filterContainer = ImprovedSimpleContainer(this, 1)
	val filterStack: ItemStack get() = filterContainer.getItem(FILTER_SLOT)

    private val fluidHandler = object : VoidingResourceHandler<FluidResource>(FluidResource.EMPTY) {
        override fun isValid(index: Int, resource: FluidResource): Boolean = passesFilter(resource.toStack(1))
        override fun insert(index: Int, resource: FluidResource, amount: Int, transaction: TransactionContext): Int {
            return if (isValid(index, resource)) super.insert(index, resource, amount, transaction) else 0
        }
    }
    fun getFluidHandler(direction: Direction?): ResourceHandler<FluidResource> = fluidHandler

	override fun getDisplayName(): Component {
		return blockState.block.name
	}

	fun passesFilter(stack: FluidStack): Boolean {
		if (filterStack.isEmpty) return true
		return FluidFilterItem.passesFilter(filterStack, stack)
	}

	// TODO: Make menu
	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? {
		return null
	}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)
		ContainerHelper.saveAllItems(tag, filterContainer.items)
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)
		ContainerHelper.loadAllItems(tag, filterContainer.items)
	}

	companion object {
		const val FILTER_SLOT = 0
	}

}
