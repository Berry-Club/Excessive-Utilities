package dev.aaronhowser.mods.excessive_utilities.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.excessive_utilities.menu.FlatTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModEntityTypes
import net.minecraft.commands.arguments.EntityAnchorArgument
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.MenuProvider
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.fluids.capability.IFluidHandler
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
			level.getCapability(Capabilities.ItemHandler.BLOCK, blockPosition(), aiming) == null
		} else {
			level.getCapability(Capabilities.FluidHandler.BLOCK, blockPosition(), aiming) == null
		}

		if (shouldBreak) {
			val player = level.players().first()
			player.tell("Breaking a node at $onPos")
		}

		return shouldBreak
	}

	private fun transferFluid(level: ServerLevel) {
		val pos = blockPosition()
		val targetPos = pos.relative(aiming)

		val source = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, aiming) ?: return
		val target = level.getCapability(Capabilities.FluidHandler.BLOCK, targetPos, aiming.opposite) ?: return

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
		val targetPos = pos.relative(aiming)

		val source = level.getCapability(Capabilities.ItemHandler.BLOCK, pos, aiming) ?: return
		val target = level.getCapability(Capabilities.ItemHandler.BLOCK, targetPos, aiming.opposite) ?: return

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
	}

	override fun readAdditionalSaveData(compound: CompoundTag) {
		isItemNode = compound.getBoolean(IS_ITEM_NODE_TAG)
		val directionOrdinal = compound.getInt(AIMING_TAG)
		aiming = Direction.entries[directionOrdinal]
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return FlatTransferNodeMenu(containerId, playerInventory, SimpleContainer(1), this)
	}

	companion object {
		val IS_ITEM_NODE_DATA: EntityDataAccessor<Boolean> =
			SynchedEntityData.defineId(FlatTransferNodeEntity::class.java, EntityDataSerializers.BOOLEAN)
		val AIMING_DATA: EntityDataAccessor<Direction> =
			SynchedEntityData.defineId(FlatTransferNodeEntity::class.java, EntityDataSerializers.DIRECTION)

		const val IS_ITEM_NODE_TAG = "IsItemNode"
		const val AIMING_TAG = "Aiming"

		fun place(
			level: ServerLevel,
			blockPos: BlockPos,
			direction: Direction,
			isItemNode: Boolean = true
		): FlatTransferNodeEntity? {
			val nodesAlreadyThere = level.getEntitiesOfClass(
				FlatTransferNodeEntity::class.java,
				AABB(blockPos)
			)

			if (nodesAlreadyThere.any { it.aiming == direction }) {
				return null
			}

			val node = FlatTransferNodeEntity(ModEntityTypes.FLAT_TRANSFER_NODE.get(), level)

			node.setPos(blockPos.bottomCenter)
			node.isItemNode = isItemNode
			node.aiming = direction

			level.addFreshEntity(node)
			return node
		}
	}

}