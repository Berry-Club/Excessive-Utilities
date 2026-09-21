package dev.aaronhowser.mods.excessive_utilities.block_entity.base

import dev.aaronhowser.mods.aaron.container.ContainerContainer
import dev.aaronhowser.mods.aaron.container.ImprovedSimpleContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.item.SpeedUpgradeItem
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.items.IItemHandler
import net.neoforged.neoforge.items.wrapper.InvWrapper
import net.neoforged.neoforge.items.wrapper.RangedWrapper

abstract class MechanicalInteractorBlockEntity(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState,
	containerSize: Int
) : GpDrainBlockEntity(type, pos, blockState), MenuProvider, ContainerContainer {

	protected val container: ImprovedSimpleContainer = MechanicalInteractorContainer(containerSize)

	private val itemHandler = RangedWrapper(InvWrapper(container), 0, INVENTORY_SIZE)

	var redstoneMode = RedstoneMode.ALWAYS_ON
		private set
	private var isPowered = false
	private var pendingPulses = 0
	private var cooldown = 0

	protected open fun canInsertItem(slot: Int, stack: ItemStack): Boolean {
		if (slot == UPGRADE_SLOT) {
			return stack.isItem(ModItemTagsProvider.SPEED_UPGRADES)
		}

		return true
	}

	override fun getContainers(): List<Container> = listOf(container)

	open fun getItemHandler(direction: Direction?): IItemHandler = itemHandler

	fun updateRedstoneState(powered: Boolean) {
		if (powered && !isPowered && redstoneMode == RedstoneMode.ON_PULSE) {
			pendingPulses++
		}

		isPowered = powered
		setChanged()
	}

	private fun shouldOperate(): Boolean {
		return when (redstoneMode) {
			RedstoneMode.ALWAYS_ON -> true
			RedstoneMode.WHILE_POWERED -> isPowered
			RedstoneMode.WHILE_UNPOWERED -> !isPowered
			RedstoneMode.ON_PULSE -> pendingPulses > 0
		}
	}

	override fun getGpUsage(): Double {
		return SpeedUpgradeItem.getGpCost(container.getItem(UPGRADE_SLOT).count)
	}

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)
		if (isOverloaded()) return
		if (!shouldOperate()) return

		cooldown -= 1 + container.getItem(UPGRADE_SLOT).count
		if (cooldown > 0) return

		cooldown += BASE_COOLDOWN
		if (redstoneMode == RedstoneMode.ON_PULSE) {
			pendingPulses--
		}

		operate(level)
		setChanged()
	}

	protected abstract fun operate(level: ServerLevel)

	protected fun setRedstoneMode(value: Int) {
		redstoneMode = RedstoneMode.fromOrdinal(value)
		pendingPulses = 0
		setChanged()
	}

	override fun getDisplayName(): Component = blockState.block.name

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)
		tag.saveItems(container, registries)
		tag.putInt(REDSTONE_MODE_NBT, redstoneMode.ordinal)
		tag.putBoolean(POWERED_NBT, isPowered)
		tag.putInt(PENDING_PULSES_NBT, pendingPulses)
		tag.putInt(COOLDOWN_NBT, cooldown)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)
		tag.loadItems(container, registries)
		redstoneMode = RedstoneMode.fromOrdinal(tag.getInt(REDSTONE_MODE_NBT))
		isPowered = tag.getBoolean(POWERED_NBT)
		pendingPulses = tag.getInt(PENDING_PULSES_NBT)
		cooldown = tag.getInt(COOLDOWN_NBT)
	}

	companion object {
		const val INVENTORY_SIZE = 9
		const val UPGRADE_SLOT = 9
		const val BASE_CONTAINER_SIZE = 10
		const val BASE_COOLDOWN = 20

		const val REDSTONE_MODE_DATA_INDEX = 0
		private const val REDSTONE_MODE_NBT = "RedstoneMode"
		private const val POWERED_NBT = "Powered"
		private const val PENDING_PULSES_NBT = "PendingPulses"
		private const val COOLDOWN_NBT = "Cooldown"

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: MechanicalInteractorBlockEntity
		) {
			if (!level.isClientSide) {
				blockEntity.serverTick(level as ServerLevel)
			}
		}
	}

	enum class RedstoneMode(val langKey: String) {
		ALWAYS_ON(ModMenuLang.MECHANICAL_INTERACTOR_ALWAYS_ON),
		WHILE_POWERED(ModMenuLang.MECHANICAL_INTERACTOR_REDSTONE_ON),
		WHILE_UNPOWERED(ModMenuLang.MECHANICAL_INTERACTOR_REDSTONE_OFF),
		ON_PULSE(ModMenuLang.MECHANICAL_INTERACTOR_REDSTONE_PULSE);

		companion object {
			fun fromOrdinal(ordinal: Int): RedstoneMode {
				if (ordinal < 0) return entries.last()
				if (ordinal >= entries.size) return entries.first()

				return entries[ordinal]
			}
		}
	}

	private inner class MechanicalInteractorContainer(size: Int) : ImprovedSimpleContainer(this, size) {
		override fun canPlaceItem(slot: Int, stack: ItemStack): Boolean {
			return canInsertItem(slot, stack)
		}
	}

}