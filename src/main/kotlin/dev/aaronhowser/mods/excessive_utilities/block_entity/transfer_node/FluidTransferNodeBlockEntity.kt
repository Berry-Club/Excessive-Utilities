package dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.item.FluidFilterItem
import dev.aaronhowser.mods.excessive_utilities.item.ItemFilterItem
import dev.aaronhowser.mods.excessive_utilities.menu.fluid_transfer_node.FluidTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.WorldInteractionFluidRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandlerUtil
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.transaction.Transaction
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.ConfigurableFluidTank

class FluidTransferNodeBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : TransferNodeBlockEntity(ModBlockEntityTypes.FLUID_TRANSFER_NODE.get(), pos, blockState) {

	private val bufferTank =
		object : ConfigurableFluidTank(TANK_SIZE) {
			override fun onContentsChanged() {
				this@FluidTransferNodeBlockEntity.setChanged()
			}
		}

	private val filterContainer = FluidTransferNodeFilterContainer()

	override fun getContainers(): List<Container> {
		return listOf(upgradeContainer, filterContainer)
	}

	private fun passesFilter(stack: ItemStack): Boolean {
		val filterStack = filterContainer.getItem(0)
		if (filterStack.isEmpty) return true

		return ItemFilterItem.passesFilter(filterStack, stack)
	}

	private fun getParentFluidHandler(level: ServerLevel): ResourceHandler<FluidResource>? {
		return level.getCapability(Capabilities.Fluid.BLOCK, placedOnPos, placedOnDirection.opposite)
	}

	override fun getBufferAmount(): Int = bufferTank.fluidAmount

    override fun pushIntoPingPos(level: ServerLevel) {
        if (bufferTank.isEmpty) return
        if (hasConfiguredTransmitter()) {
            val remaining = bufferTank.fluid
            visitEnderReceivers(level) { receiver -> transferToEnderReceiver(receiver, remaining) }
        }
        for (handler in getFluidHandlersAroundPing(level)) {
            if (bufferTank.isEmpty) break
            if (pushBuffer(handler) > 0) didWorkThisTick = true
        }
    }

    private fun pushBuffer(handler: ResourceHandler<FluidResource>): Int {
        if (bufferTank.isEmpty) return 0
        if (!hasCreativeUpgrade()) return ResourceHandlerUtil.move(bufferTank, handler, { true }, bufferTank.fluidAmount, null)
        return Transaction.openRoot().use { transaction ->
            val inserted = handler.insert(bufferTank.getResource(0), bufferTank.fluidAmount, transaction)
            transaction.commit()
            inserted
        }
    }

    override fun pullFromPingPos(level: ServerLevel) {
        if (hasConfiguredReceiver()) return
        val maxAmount = if (hasStackUpgrade()) 1_000 else 200
        val filter = filterContainer.getItem(0)
        for (handler in getFluidHandlersAroundPing(level)) {
            val moved = ResourceHandlerUtil.move(handler, bufferTank,
                { resource -> !filter.isItem(ModItems.FLUID_FILTER) || FluidFilterItem.passesFilter(filter, resource.toStack(1)) }, maxAmount, null)
            if (moved > 0) { didWorkThisTick = true; return }
        }
    }

	private fun getFluidHandlersAroundPing(level: ServerLevel): List<ResourceHandler<FluidResource>> {
		return getCapabilitiesAroundPing(level) { neighborPos, side ->
			level.getCapability(Capabilities.Fluid.BLOCK, neighborPos, side)
		}
	}

	fun receiveEnderTransfer(stack: FluidStack): Int {
		val filterStack = filterContainer.getItem(0)
		if (filterStack.isItem(ModItems.FLUID_FILTER) && !FluidFilterItem.passesFilter(filterStack, stack)) return 0
		return bufferTank.fill(stack)
	}

    private fun transferToEnderReceiver(receiver: TransferNodeBlockEntity, remaining: FluidStack): Boolean {
        if (receiver !is FluidTransferNodeBlockEntity || bufferTank.isEmpty) return false
        val filter = receiver.filterContainer.getItem(0)
        if (filter.isItem(ModItems.FLUID_FILTER) && !FluidFilterItem.passesFilter(filter, bufferTank.fluid)) return false
        val accepted = pushBuffer(receiver.bufferTank)
        if (accepted == 0) return false
        if (!hasCreativeUpgrade()) remaining.shrink(accepted)
        didWorkThisTick = true
        return true
    }

    override fun pushIntoParent(level: ServerLevel) {
        val parent = getParentFluidHandler(level) ?: return
        if (pushBuffer(parent) > 0) didWorkThisTick = true
    }

    override fun pullFromParent(level: ServerLevel) {
        if (worldInteraction(level)) return
        val parent = getParentFluidHandler(level) ?: return
        val moved = ResourceHandlerUtil.move(parent, bufferTank, { true }, if (hasStackUpgrade()) 1_000 else 200, null)
        if (moved > 0) didWorkThisTick = true
    }

	private fun worldInteraction(level: ServerLevel): Boolean {
		val hasUpgrade = upgradeContainer.countItem(ModItems.WORLD_INTERACTION_UPGRADE.get()) > 0
		if (!hasUpgrade) return false

		val craftedRecipe = tryCraftRecipe(level)
		return craftedRecipe
	}

	private fun tryCraftRecipe(level: ServerLevel): Boolean {
		val onBlock = level.getBlockState(placedOnPos)
		val blockBehind = level.getBlockState(placedOnPos.relative(placedOnDirection))
		val adjacentBlocks = buildList {
			for (dir in Direction.entries) {
				if (dir.axis == placedOnDirection.axis) continue
				add(level.getBlockState(placedOnPos.relative(dir)))
			}
		}

		val recipe = WorldInteractionFluidRecipe.getRecipe(level, onBlock, adjacentBlocks, blockBehind) ?: return false

		val amountToExtract = if (hasStackUpgrade()) 1_000 else 25
		val output = recipe.output.copyWithAmount(amountToExtract)

		val fluidInBuffer = bufferTank.fluid
		if (fluidInBuffer.isEmpty) {
			bufferTank.fill(output)
			didWorkThisTick = true
			return true
		}

		if (!FluidStack.isSameFluidSameComponents(fluidInBuffer, output)) return false

		val amountThatFits = bufferTank.tankCapacity - bufferTank.fluidAmount
		val amountToAdd = output.amount.coerceAtMost(amountThatFits)
		if (amountToAdd <= 0) return false

		bufferTank.fill(output.copyWithAmount(amountToAdd))
		didWorkThisTick = true
		return true
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return FluidTransferNodeMenu(
			containerId,
			playerInventory,
			upgradeContainer,
			this,
			filterContainer,
			containerData
		)
	}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)

		tag.store(BUFFER_NBT, FluidStack.OPTIONAL_CODEC, bufferTank.fluid)

		if (!filterContainer.isEmpty) {
			tag.child(FILTER_NBT).saveItems(filterContainer)
		}
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)

		tag.read(BUFFER_NBT, FluidStack.OPTIONAL_CODEC).ifPresent(bufferTank::setFluid)

		tag.child(FILTER_NBT).ifPresent { it.loadItems(filterContainer) }
	}

	companion object {
		const val FILTER_NBT = "Filter"
		const val BUFFER_NBT = "BufferTank"

		const val TANK_SIZE = 16_000
		const val FILTER_CONTAINER_SIZE = 1

	}

	private inner class FluidTransferNodeFilterContainer : ImprovedSimpleContainer(this, FILTER_CONTAINER_SIZE) {

		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			return stack.isItem(ModItems.FLUID_FILTER)
		}

	}

}
