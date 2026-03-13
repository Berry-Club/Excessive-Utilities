package dev.aaronhowser.mods.excessive_utilities.block.entity

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isFull
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextRange
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.block.base.TransferNodePing
import dev.aaronhowser.mods.excessive_utilities.block.base.entity.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.menu.item_transfer_node.ItemTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.core.particles.DustParticleOptions
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

class ItemTransferNodeBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : TransferNodeBlockEntity(ModBlockEntityTypes.ITEM_TRANSFER_NODE.get(), pos, blockState) {

	private val bufferContainer = ImprovedSimpleContainer(this, BUFFER_CONTAINER_SIZE)
	private val filterContainer =
		object : ImprovedSimpleContainer(this, FILTER_CONTAINER_SIZE) {
			override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
				return stack.isItem(ModItems.ITEM_FILTER.get())
			}
		}

	override fun getContainers(): List<Container> {
		return listOf(bufferContainer, upgradeContainer, filterContainer)
	}

	fun hasStackUpgrade(): Boolean {
		return upgradeContainer.countItem(ModItems.STACK_UPGRADE.get()) > 0
	}

	private fun getParentItemHandler(level: ServerLevel): IItemHandler? {
		return level.getCapability(Capabilities.ItemHandler.BLOCK, placedOnPos, placedOnDirection.opposite)
	}

	private val ping = TransferNodePing(blockPos, placedOnDirection)

	override fun activeTick(level: ServerLevel) {
		if (isRetrieval) {
			pullerTick(level)
		} else {
			pusherTick(level)
		}

		val pingPos = ping.currentPingPos

		for (i in 0 until 5) {
			val x = pingPos.x + 0.5 + level.random.nextRange(-0.5, 0.5)
			val y = pingPos.y + 0.5 + level.random.nextRange(-0.5, 0.5)
			val z = pingPos.z + 0.5 + level.random.nextRange(-0.5, 0.5)

			level.sendParticles(
				DustParticleOptions.REDSTONE,
				x, y, z,
				1,
				0.0, 0.0, 0.0,
				0.0
			)
		}
	}

	// Pull from distant inventories into the buffer,
	// then push from the buffer into the parent inventory.
	// if there's already stuff in the buffer, there's no reason to try to continue filling it,
	// so just reset the ping and don't continue searching
	private fun pullerTick(level: ServerLevel) {
		pushIntoParent(level)

		if (!bufferContainer.isEmpty) {
			ping.reset()
			return
		}

		// Try to pull from inventories at the ping position
		// If nothing was pulled, move the ping forward and try again next tick
		// If something was pulled, keep the ping where it is so it can continue pulling from it next tick

		pullFromPingPos(level)

		if (bufferContainer.isEmpty) {
			ping.march(level)
		}
	}

	// Pull from the parent inventory into the buffer,
	// then search for somewhere to push the items in the buffer to.
	// If there's nothing in the buffer, don't bother searching for somewhere to put it
	private fun pusherTick(level: ServerLevel) {
		pullFromParent(level)

		if (bufferContainer.isEmpty) {
			ping.reset()
			return
		}

		// Try to push into inventories at the ping position
		// If nothing was pushed, move the ping forward and try again next tick
		// If something was pushed, keep the ping where it is so it can continue pushing into it next tick

		val amountBefore = bufferContainer.getItem(0).count
		pushIntoPingPos(level)
		val amountAfter = bufferContainer.getItem(0).count

		if (amountBefore == amountAfter) {
			ping.march(level)
		}
	}

	private fun pushIntoPingPos(level: ServerLevel) {
		val stackInBuffer = bufferContainer.getItem(0)
		if (stackInBuffer.isEmpty) return

		val itemHandlers = getItemHandlersAroundPing(level)
		if (itemHandlers.isEmpty()) return

		for (handler in itemHandlers) {
			val simulatedRemainder = ItemHandlerHelper.insertItemStacked(handler, stackInBuffer.copy(), true)
			val amountAccepted = stackInBuffer.count - simulatedRemainder.count
			if (amountAccepted <= 0) continue

			val remainder = ItemHandlerHelper.insertItemStacked(handler, stackInBuffer, false)

			bufferContainer.setItem(0, remainder)
			didWorkThisTick = true

			if (remainder.isEmpty) break
		}
	}

	private fun pullFromPingPos(level: ServerLevel) {
		val itemHandlers = getItemHandlersAroundPing(level)
		if (itemHandlers.isEmpty()) return

		val amountToExtract = if (hasStackUpgrade()) 64 else 1

		for (handler in itemHandlers) {
			for (slot in 0 until handler.slots) {
				val simExtract = handler.extractItem(slot, amountToExtract, true)
				if (simExtract.isEmpty) continue

				val extracted = handler.extractItem(slot, amountToExtract, false)
				bufferContainer.setItem(0, extracted)
				didWorkThisTick = true
				return
			}
		}
	}

	private fun getItemHandlersAroundPing(level: ServerLevel): List<IItemHandler> {
		val possibleDirections = ping.getNextDirections(level)
		val pingPos = ping.currentPingPos

		val handlers = mutableListOf<IItemHandler>()
		for (dir in possibleDirections) {
			val neighborPos = pingPos.relative(dir)
			val handler = level.getCapability(Capabilities.ItemHandler.BLOCK, neighborPos, dir.opposite) ?: continue
			handlers.add(handler)
		}

		return handlers
	}

	private fun pushIntoParent(level: ServerLevel) {
		val parentHandler = getParentItemHandler(level) ?: return

		val stackInBuffer = bufferContainer.getItem(0)
		if (stackInBuffer.isEmpty) return

		val inserted = ItemHandlerHelper.insertItemStacked(parentHandler, stackInBuffer, true)
		val amountInserted = stackInBuffer.count - inserted.count
		if (amountInserted <= 0) return

		ItemHandlerHelper.insertItemStacked(parentHandler, stackInBuffer, false)

		val newStack = stackInBuffer.copy()
		newStack.shrink(amountInserted)
		bufferContainer.setItem(0, newStack)
		didWorkThisTick = true
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
			break
		}
	}

	private val containerData =
		object : ContainerData {
			override fun getCount(): Int = CONTAINER_DATA_SIZE

			override fun get(index: Int): Int {
				return when (index) {
					X_DATA_INDEX -> ping.currentPingPos.x
					Y_DATA_INDEX -> ping.currentPingPos.y
					Z_DATA_INDEX -> ping.currentPingPos.z
					else -> 0
				}
			}

			override fun set(index: Int, value: Int) {
				// Noop
			}
		}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return ItemTransferNodeMenu(
			containerId,
			playerInventory,
			upgradeContainer,
			bufferContainer,
			filterContainer,
			containerData
		)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.saveItems(bufferContainer, registries)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		tag.loadItems(bufferContainer, registries)
	}

	companion object {
		const val BUFFER_CONTAINER_SIZE = 1
		const val FILTER_CONTAINER_SIZE = 1

		const val CONTAINER_DATA_SIZE = 3
		const val X_DATA_INDEX = 0
		const val Y_DATA_INDEX = 1
		const val Z_DATA_INDEX = 2

	}

}