package dev.aaronhowser.mods.excessive_utilities.block.entity.trash

import dev.aaronhowser.mods.aaron.misc.ImprovedSimpleContainer
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
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
import net.neoforged.neoforge.fluids.capability.IFluidHandler

class FluidTrashCanBlockEntity(
	pos: BlockPos,
	state: BlockState
) : BlockEntity(ModBlockEntityTypes.FLUID_TRASH_CAN.get(), pos, state), MenuProvider {

	private val filterContainer = ImprovedSimpleContainer(this, 1)
	val filterStack: ItemStack get() = filterContainer.getItem(FILTER_SLOT)

	private val fluidHandler =
		object : IFluidHandler {
			override fun getTanks(): Int = 1
			override fun getFluidInTank(tank: Int): FluidStack = FluidStack.EMPTY
			override fun getTankCapacity(tank: Int): Int = Int.MAX_VALUE
			override fun fill(resource: FluidStack, action: IFluidHandler.FluidAction): Int = resource.amount
			override fun drain(resource: FluidStack, action: IFluidHandler.FluidAction): FluidStack = FluidStack.EMPTY
			override fun drain(maxDrain: Int, action: IFluidHandler.FluidAction): FluidStack = FluidStack.EMPTY
			override fun isFluidValid(tank: Int, stack: FluidStack): Boolean {
				//TODO: Implement filter logic
				return true
			}
		}

	fun getFluidHandler(direction: Direction?): IFluidHandler = fluidHandler

	override fun getDisplayName(): Component {
		return blockState.block.name
	}

	// TODO: Make menu for the filter
	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? {
		return null
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		ContainerHelper.saveAllItems(tag, filterContainer.items, registries)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)
		ContainerHelper.loadAllItems(tag, filterContainer.items, registries)
	}

	companion object {
		const val FILTER_SLOT = 0
	}

}