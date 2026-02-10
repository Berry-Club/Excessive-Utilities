package dev.aaronhowser.mods.excessive_utilities.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toVec3
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.menu.flat_transfer_node.FlatTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModEntityTypes
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.*
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.fluids.capability.IFluidHandler
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.ItemHandlerHelper

class FlatTransferNodeEntity(entityType: EntityType<*>, level: Level) : Entity(entityType, level), MenuProvider {

	var isItemNode: Boolean
		get() = entityData.get(IS_ITEM_NODE_DATA)
		private set(value) = entityData.set(IS_ITEM_NODE_DATA, value)

	var aiming: Direction
		get() = entityData.get(AIMING_DATA)
		private set(value) {
			entityData.set(AIMING_DATA, value)

			val lookTowards = eyePosition.add(value.stepX.toDouble(), value.stepY.toDouble(), value.stepZ.toDouble())
			lookAt(EntityAnchorArgument.Anchor.EYES, lookTowards)
		}

	private val container: SimpleContainer =
		object : SimpleContainer(1) {
			override fun canAddItem(stack: ItemStack): Boolean {
				return stack.isItem(ModItemTagsProvider.FILTERS) && super.canAddItem(stack)
			}
		}

	val originItemHandler: IItemHandler?
		get() = level().getCapability(Capabilities.ItemHandler.BLOCK, blockPosition(), aiming)
	val targetItemHandler: IItemHandler?
		get() = level().getCapability(Capabilities.ItemHandler.BLOCK, blockPosition().relative(aiming), aiming.opposite)

	val originFluidHandler: IFluidHandler?
		get() = level().getCapability(Capabilities.FluidHandler.BLOCK, blockPosition(), aiming)
	val targetFluidHandler: IFluidHandler?
		get() = level().getCapability(Capabilities.FluidHandler.BLOCK, blockPosition().relative(aiming), aiming.opposite)

	override fun tick() {
		super.tick()

		val level = level()
		if (level is ServerLevel) {
			if (tryBreak(level)) {
				discard()
				return
			}

			if (isItemNode) {
				transferItem(level)
			} else {
				transferFluid(level)
			}
		}
	}

	private fun tryBreak(level: ServerLevel): Boolean {
		val shouldBreak = if (isItemNode) {
			originItemHandler == null
		} else {
			originFluidHandler == null
		}

		return shouldBreak
	}

	override fun remove(reason: RemovalReason) {
		if (reason == RemovalReason.DISCARDED || reason == RemovalReason.KILLED) {
			val pos = position().add(direction.step().toVec3().scale(0.5))

			Containers.dropContents(level(), pos.x, pos.y, pos.z, container)

			val item = if (isItemNode) ModItems.FLAT_TRANSFER_NODE_ITEMS.get() else ModItems.FLAT_TRANSFER_NODE_FLUIDS.get()
			Containers.dropItemStack(level(), pos.x, pos.y, pos.z, item.defaultInstance)
		}

		super.remove(reason)
	}

	private fun transferFluid(level: ServerLevel) {

		val source = originFluidHandler ?: return
		val target = targetFluidHandler ?: return

		for (i in 0 until source.tanks) {
			val fluidToMove = source.getFluidInTank(i)
			if (fluidToMove.isEmpty) continue

			val copy = fluidToMove.copyWithAmount(1000)

			val simulatedResult = target.fill(copy, IFluidHandler.FluidAction.SIMULATE)

			if (simulatedResult != 0) {
				val actualResult = target.fill(copy, IFluidHandler.FluidAction.EXECUTE)
				source.drain(actualResult, IFluidHandler.FluidAction.EXECUTE)
				return
			}
		}
	}

	private fun transferItem(level: ServerLevel) {
		val pos = blockPosition()
		pos.relative(aiming)

		val source = originItemHandler ?: return
		val target = targetItemHandler ?: return

		for (i in 0 until source.slots) {
			val stackToMove = source.getStackInSlot(i)
			if (stackToMove.isEmpty) continue

			val copy = stackToMove.copyWithCount(1)

			val simulatedResult = ItemHandlerHelper.insertItemStacked(target, copy, true)

			if (simulatedResult.count != copy.count) {
				val actualResult = ItemHandlerHelper.insertItemStacked(target, copy, false)
				val amountInserted = copy.count - actualResult.count
				stackToMove.shrink(amountInserted)
				return
			}
		}
	}

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
		builder.define(IS_ITEM_NODE_DATA, true)
		builder.define(AIMING_DATA, Direction.NORTH)
	}

	override fun addAdditionalSaveData(compound: CompoundTag) {
		compound.putBoolean(IS_ITEM_NODE_TAG, isItemNode)
		compound.putInt(AIMING_TAG, aiming.ordinal)
		ContainerHelper.saveAllItems(compound, container.items, registryAccess())
	}

	override fun readAdditionalSaveData(compound: CompoundTag) {
		isItemNode = compound.getBoolean(IS_ITEM_NODE_TAG)
		val directionOrdinal = compound.getInt(AIMING_TAG)
		aiming = Direction.entries[directionOrdinal]
		ContainerHelper.loadAllItems(compound, container.items, registryAccess())
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return FlatTransferNodeMenu(containerId, playerInventory, container, this)
	}

	companion object {
		val IS_ITEM_NODE_DATA: EntityDataAccessor<Boolean> =
			SynchedEntityData.defineId(FlatTransferNodeEntity::class.java, EntityDataSerializers.BOOLEAN)
		val AIMING_DATA: EntityDataAccessor<Direction> =
			SynchedEntityData.defineId(FlatTransferNodeEntity::class.java, EntityDataSerializers.DIRECTION)

		const val IS_ITEM_NODE_TAG = "IsItemNode"
		const val AIMING_TAG = "Aiming"

		fun getNodeAt(level: Level, blockPos: BlockPos, direction: Direction): FlatTransferNodeEntity? {
			return level.getEntitiesOfClass(
				FlatTransferNodeEntity::class.java,
				AABB(blockPos)
			).firstOrNull { it.aiming == direction }
		}

		@Suppress("FoldInitializerAndIfToElvis")
		fun place(
			level: ServerLevel,
			blockPos: BlockPos,
			direction: Direction,
			isItemNode: Boolean = true
		): FlatTransferNodeEntity? {
			if (getNodeAt(level, blockPos, direction) != null) return null

			if (isItemNode) {
				val itemHandlerThere = level.getCapability(Capabilities.ItemHandler.BLOCK, blockPos, direction)
				if (itemHandlerThere == null) return null
			} else {
				val fluidHandlerThere = level.getCapability(Capabilities.FluidHandler.BLOCK, blockPos, direction)
				if (fluidHandlerThere == null) return null
			}

			val node = FlatTransferNodeEntity(ModEntityTypes.FLAT_TRANSFER_NODE.get(), level)

			node.setPos(blockPos.bottomCenter)
			node.isItemNode = isItemNode
			node.aiming = direction

			level.addFreshEntity(node)
			return node
		}

		fun handleRightClickBlock(event: PlayerInteractEvent.RightClickBlock) {
			val player = event.entity
			if (!player.isHolding { it.isItem(ModItemTagsProvider.INTERACT_WITH_FLAT_TRANSFER_NODES) }) return

			val level = event.level
			val blockPos = event.pos
			val direction = event.face ?: return

			val node = getNodeAt(level, blockPos, direction) ?: return

			if (player.isSecondaryUseActive) {
				node.kill()
			} else {
				player.openMenu(node)
			}

			player.swing(player.usedItemHand, true)

			event.cancellationResult = InteractionResult.CONSUME
			event.isCanceled = true
		}
	}

}