package dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node

import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isFull
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.item.ItemFilterItem
import dev.aaronhowser.mods.excessive_utilities.menu.item_transfer_node.ItemTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.recipe.machine.WorldInteractionItemRecipe
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.AABB
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

	private fun passesFilter(stack: ItemStack): Boolean {
		val filterStack = filterContainer.getItem(0)
		if (filterStack.isEmpty) return true

		return ItemFilterItem.passesFilter(filterStack, stack)
	}

	private fun getParentItemHandler(level: ServerLevel): IItemHandler? {
		return level.getCapability(Capabilities.ItemHandler.BLOCK, placedOnPos, placedOnDirection.opposite)
	}

	override fun getBufferAmount(): Int = bufferContainer.getItem(0).count

	override fun pushIntoPingPos(level: ServerLevel) {
		val stackInBuffer = bufferContainer.getItem(0)
		if (stackInBuffer.isEmpty) return

		val itemHandlers = getItemHandlersAroundPing(level)
		if (itemHandlers.isEmpty()) return

		for (handler in itemHandlers) {
			val simulatedRemainder = ItemHandlerHelper.insertItemStacked(handler, stackInBuffer.copy(), true)
			val amountAccepted = stackInBuffer.count - simulatedRemainder.count
			if (amountAccepted <= 0) continue

			val remainder = ItemHandlerHelper.insertItemStacked(handler, stackInBuffer, false)

			if (!hasCreativeUpgrade()) {
				bufferContainer.setItem(0, remainder)
			}

			didWorkThisTick = true

			if (remainder.isEmpty) break
		}
	}

	override fun pullFromPingPos(level: ServerLevel) {
		val itemHandlers = getItemHandlersAroundPing(level)
		if (itemHandlers.isEmpty()) return

		val amountToExtract = if (hasStackUpgrade()) 64 else 1

		for (handler in itemHandlers) {
			for (slot in 0 until handler.slots) {
				val simExtract = handler.extractItem(slot, amountToExtract, true)
				if (simExtract.isEmpty || !passesFilter(simExtract)) continue

				val extracted = handler.extractItem(slot, amountToExtract, false)
				bufferContainer.setItem(0, extracted)
				didWorkThisTick = true
				return
			}
		}
	}

	private fun getItemHandlersAroundPing(level: ServerLevel): List<IItemHandler> {
		return getCapabilitiesAroundPing(level) { neighborPos, side ->
			level.getCapability(Capabilities.ItemHandler.BLOCK, neighborPos, side)
		}
	}

	override fun pushIntoParent(level: ServerLevel) {
		val parentHandler = getParentItemHandler(level) ?: return

		val stackInBuffer = bufferContainer.getItem(0)
		if (stackInBuffer.isEmpty) return

		val inserted = ItemHandlerHelper.insertItemStacked(parentHandler, stackInBuffer, true)
		val amountInserted = stackInBuffer.count - inserted.count
		if (amountInserted <= 0) return

		ItemHandlerHelper.insertItemStacked(parentHandler, stackInBuffer, false)

		if (!hasCreativeUpgrade()) {
			val newStack = stackInBuffer.copy()
			newStack.shrink(amountInserted)
			bufferContainer.setItem(0, newStack)
		}

		didWorkThisTick = true
	}

	private fun worldInteraction(level: ServerLevel): Boolean {
		val hasUpgrade = upgradeContainer.countItem(ModItems.WORLD_INTERACTION_UPGRADE.get()) > 0
		if (!hasUpgrade) return false

		val craftedRecipe = tryCraftRecipe(level)
		if (craftedRecipe) return true

		return tryPickupItems(level)
	}

	private fun tryPickupItems(level: ServerLevel): Boolean {
		val itemEntities = level.getEntitiesOfClass(ItemEntity::class.java, AABB(placedOnPos))
		if (itemEntities.isEmpty()) return false

		val amountToPickup = if (hasStackUpgrade()) 64 else 1

		for (itemEntity in itemEntities) {
			val entityStack = itemEntity.item
			if (!passesFilter(entityStack)) continue

			val stackInBuffer = bufferContainer.getItem(0)

			if (stackInBuffer.isEmpty) {
				val toPickup = entityStack.copy()
				toPickup.count = entityStack.count.coerceAtMost(amountToPickup)
				bufferContainer.setItem(0, toPickup)

				entityStack.shrink(toPickup.count)
				if (entityStack.isEmpty) {
					itemEntity.discard()
				} else {
					itemEntity.item = entityStack
				}

				didWorkThisTick = true
				return true
			}

			if (!ItemStack.isSameItemSameComponents(stackInBuffer, entityStack)) continue

			val amountThatFits = stackInBuffer.maxStackSize - stackInBuffer.count
			val amountToAdd = entityStack.count
				.coerceAtMost(amountThatFits)
				.coerceAtMost(amountToPickup)

			if (amountToAdd <= 0) continue

			val newStack = stackInBuffer.copy()
			newStack.grow(amountToAdd)
			bufferContainer.setItem(0, newStack)

			entityStack.shrink(amountToAdd)
			if (entityStack.isEmpty) {
				itemEntity.discard()
			} else {
				itemEntity.item = entityStack
			}

			didWorkThisTick = true
			return true
		}

		return false
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

		val recipe = WorldInteractionItemRecipe.getRecipe(level, onBlock, adjacentBlocks, blockBehind) ?: return false

		val amountToExtract = if (hasStackUpgrade()) 64 else 1
		val input = WorldInteractionItemRecipe.Input(onBlock, adjacentBlocks, blockBehind)
		val output = recipe.assemble(input, level.registryAccess()).copyWithCount(amountToExtract)

		val stackInBuffer = bufferContainer.getItem(0)
		if (stackInBuffer.isEmpty) {
			bufferContainer.setItem(0, output)
			didWorkThisTick = true
			return true
		}

		if (!ItemStack.isSameItemSameComponents(stackInBuffer, output)) return false

		val amountThatFits = stackInBuffer.maxStackSize - stackInBuffer.count
		val amountToAdd = output.count.coerceAtMost(amountThatFits)
		if (amountToAdd <= 0) return false

		val newStack = stackInBuffer.copy()
		newStack.grow(amountToAdd)
		bufferContainer.setItem(0, newStack)
		didWorkThisTick = true
		return true
	}

	override fun pullFromParent(level: ServerLevel) {
		if (worldInteraction(level)) return

		val parentHandler = getParentItemHandler(level) ?: return

		val stackInBuffer = bufferContainer.getItem(0)
		if (stackInBuffer.isFull()) return

		var amountToExtract = if (hasStackUpgrade()) 64 else 1

		if (stackInBuffer.isEmpty) {
			for (slot in 0 until parentHandler.slots) {
				val simExtract = parentHandler.extractItem(slot, amountToExtract, true)
				if (simExtract.isEmpty || !passesFilter(simExtract)) continue

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

		if (!filterContainer.isEmpty) {
			val filterTag = CompoundTag()
			filterTag.saveItems(filterContainer, registries)
			tag.put(FILTER_NBT, filterTag)
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		tag.loadItems(bufferContainer, registries)

		if (tag.contains(FILTER_NBT)) {
			val filterTag = tag.getCompound(FILTER_NBT)
			filterTag.loadItems(filterContainer, registries)
		}
	}

	companion object {
		const val FILTER_NBT = "Filter"

		const val BUFFER_CONTAINER_SIZE = 1
		const val FILTER_CONTAINER_SIZE = 1

	}

}