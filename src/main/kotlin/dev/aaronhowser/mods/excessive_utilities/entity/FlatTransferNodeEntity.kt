package dev.aaronhowser.mods.excessive_utilities.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getDefaultInstance
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toVec3
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
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
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.ResourceHandlerUtil
import net.neoforged.neoforge.transfer.fluid.FluidResource
import net.neoforged.neoforge.transfer.item.ItemResource

class FlatTransferNodeEntity(
	entityType: EntityType<*>,
	level: Level
) : Entity(entityType, level), MenuProvider {

	var isItemNode: Boolean
		get() = entityData.get(IS_ITEM_NODE_DATA)
		private set(value) = entityData.set(IS_ITEM_NODE_DATA, value)

	var aiming: Direction
		get() = entityData.get(AIMING_DATA)
		private set(value) {
			entityData.set(AIMING_DATA, value)

			val lookTowards = eyePosition.add(
				value.stepX.toDouble(),
				value.stepY.toDouble(),
				value.stepZ.toDouble()
			)

			lookAt(EntityAnchorArgument.Anchor.EYES, lookTowards)
		}

	private val container: SimpleContainer = FlatTransferNodeContainer()

	val originItemHandler: ResourceHandler<ItemResource>?
		get() = level()
			.getCapability(
				Capabilities.Item.BLOCK,
				blockPosition(),
				aiming
			)
	val targetItemHandler: ResourceHandler<ItemResource>?
		get() = level()
			.getCapability(
				Capabilities.Item.BLOCK,
				blockPosition().relative(aiming),
				aiming.opposite
			)

	val originFluidHandler: ResourceHandler<FluidResource>?
		get() = level().getCapability(
			Capabilities.Fluid.BLOCK,
			blockPosition(),
			aiming
		)
	val targetFluidHandler: ResourceHandler<FluidResource>?
		get() = level().getCapability(
			Capabilities.Fluid.BLOCK,
			blockPosition().relative(aiming),
			aiming.opposite
		)

    override fun hurtServer(level: ServerLevel, source: DamageSource, amount: Float): Boolean = false

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

	fun getAsItemStack(): ItemStack {
		val item = if (isItemNode) {
			ModItems.FLAT_TRANSFER_NODE_ITEMS
		} else {
			ModItems.FLAT_TRANSFER_NODE_FLUIDS
		}

		return item.getDefaultInstance()
	}

	override fun remove(reason: RemovalReason) {
		if (level().isServerSide && (reason == RemovalReason.DISCARDED || reason == RemovalReason.KILLED)) {
			val pos = position()
				.add(0.0, 0.5, 0.0)
				.add(direction.step().toVec3())

			Containers.dropContents(level(), pos.x, pos.y, pos.z, container)
			Containers.dropItemStack(level(), pos.x, pos.y, pos.z, getAsItemStack())
		}

		super.remove(reason)
	}

    private fun transferFluid(level: ServerLevel) {
        ResourceHandlerUtil.move(
            originFluidHandler, targetFluidHandler, { true },
            ServerConfig.CONFIG.flatFluidTransferNodeSpeed.get(), null
        )
    }

    private fun transferItem(level: ServerLevel) {
        ResourceHandlerUtil.moveStacking(
            originItemHandler, targetItemHandler, { true },
            ServerConfig.CONFIG.flatItemTransferNodeSpeed.get(), null
        )
    }

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
		builder.define(IS_ITEM_NODE_DATA, true)
		builder.define(AIMING_DATA, Direction.NORTH)
	}

	override fun addAdditionalSaveData(compound: ValueOutput) {
		compound.putBoolean(IS_ITEM_NODE_TAG, isItemNode)
		compound.putString(AIMING_TAG, aiming.serializedName)
		ContainerHelper.saveAllItems(compound, container.items)
	}

	override fun readAdditionalSaveData(compound: ValueInput) {
		isItemNode = compound.getBooleanOr(IS_ITEM_NODE_TAG, true)
		val directionString = compound.getString(AIMING_TAG).orElseThrow()
		aiming = requireNotNull(Direction.byName(directionString)) { "Invalid transfer node direction: $directionString" }
		ContainerHelper.loadAllItems(compound, container.items)
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
				val itemHandlerThere = level.getCapability(Capabilities.Item.BLOCK, blockPos, direction)
				if (itemHandlerThere == null) return null
			} else {
				val fluidHandlerThere = level.getCapability(Capabilities.Fluid.BLOCK, blockPos, direction)
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
			if (player.isSecondaryUseActive) return
			if (!player.isHolding { it.isItem(ModItemTagsProvider.INTERACT_WITH_FLAT_TRANSFER_NODES) }) return

			val level = event.level
			val blockPos = event.pos
			val direction = event.face ?: return

			val node = getNodeAt(level, blockPos, direction) ?: return

			player.openMenu(node) { it.writeInt(node.id) }

			player.swing(player.usedItemHand, true)

			event.cancellationResult = InteractionResult.CONSUME
			event.isCanceled = true
		}

		fun handleLeftClickBlock(event: PlayerInteractEvent.LeftClickBlock) {
			val player = event.entity
			if (!player.isHolding { it.isItem(ModItemTagsProvider.INTERACT_WITH_FLAT_TRANSFER_NODES) }) return

			val level = event.level
			val blockPos = event.pos
			val direction = event.face ?: return

			val node = getNodeAt(level, blockPos, direction) ?: return

			node.kill(level as ServerLevel)
			event.isCanceled = true
		}
	}

	private inner class FlatTransferNodeContainer : SimpleContainer(1) {

		override fun canAddItem(stack: ItemStack): Boolean {
			if (!super.canAddItem(stack)) return false
			return canPlaceItem(0, stack)
		}

		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			return if (isItemNode) {
				stack.isItem(ModItems.ITEM_FILTER)
			} else {
				stack.isItem(ModItems.FLUID_FILTER)
			}
		}

	}

}
