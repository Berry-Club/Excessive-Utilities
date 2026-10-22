package dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator

import dev.aaronhowser.mods.aaron.container.ContainerContainer
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadItems
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveItems
import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.GpDrainBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.insertEnergy
import dev.aaronhowser.mods.excessive_utilities.block_entity.extractEnergy
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.handler.rainbow_generator.RainbowGeneratorHandler
import dev.aaronhowser.mods.excessive_utilities.item.SpeedUpgradeItem
import dev.aaronhowser.mods.excessive_utilities.menu.single_item_generator.SingleItemGeneratorMenu
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.storage.ValueInput
import net.minecraft.world.level.storage.ValueOutput
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.ResourceHandler
import net.neoforged.neoforge.transfer.item.ItemResource
import net.neoforged.neoforge.transfer.transaction.TransactionContext

abstract class GeneratorBlockEntity(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : GpDrainBlockEntity(type, pos, blockState), ContainerContainer, MenuProvider {

	abstract val generatorType: GeneratorType

	protected open val energyStorage = SimpleEnergyHandler(1_000_000)
	protected open val publicEnergyCapability: EnergyHandler =
		object : EnergyHandler {
			override fun getAmountAsLong(): Long = energyStorage.getAmountAsLong()
			override fun getCapacityAsLong(): Long = energyStorage.getCapacityAsLong()
			override fun insert(amount: Int, transaction: TransactionContext): Int = 0
			override fun extract(amount: Int, transaction: TransactionContext): Int = energyStorage.extract(amount, transaction)
		}

	open fun getEnergyCapability(direction: Direction?): EnergyHandler = publicEnergyCapability

	protected open val container: GeneratorContainer =
		object : GeneratorContainer(this@GeneratorBlockEntity, amountInputs = 1) {
			override fun canPlaceInput(stack: ItemStack): Boolean = isValidInput(stack)
			override fun canPlaceSecondaryInput(stack: ItemStack): Boolean = isValidSecondaryInput(stack)
			override fun canPlaceUpgrade(stack: ItemStack): Boolean = isValidUpgrade(stack)
		}

	override fun getContainers(): List<Container> = listOfNotNull(container)
	open fun getItemHandler(direction: Direction?): ResourceHandler<ItemResource>? = container.itemHandler

	protected open fun isValidInput(itemStack: ItemStack) = true
	protected open fun isValidSecondaryInput(itemStack: ItemStack) = false
	protected open fun isValidUpgrade(itemStack: ItemStack): Boolean {
		return itemStack.isItem(ModItemTagsProvider.SPEED_UPGRADES)
	}

	override fun getGpUsage(): Double {
		if (burnTimeRemaining <= 0) return 0.0

		val amountUpgrades = container.getItem(GeneratorContainer.UPGRADE_SLOT).count
		return SpeedUpgradeItem.getGpCost(amountUpgrades)
	}

	protected open val containerData: ContainerData =
		object : ContainerData {
			override fun getCount(): Int = DEFAULT_GENERATOR_CONTAINER_DATA_SIZE

			override fun get(index: Int): Int {
				return when (index) {
					MAX_ENERGY_DATA_INDEX -> energyStorage.getCapacityAsInt()
					CURRENT_ENERGY_DATA_INDEX -> energyStorage.getAmountAsInt()
					BURN_TIME_REMAINING_DATA_INDEX -> burnTimeRemaining
					else -> -1
				}
			}

			override fun set(index: Int, value: Int) {
				// Cannot set from container data
			}
		}

	protected var lastGeneratedEnergyOnTick: Long = -1L
	open fun isContributingToRainbowGen(): Boolean {
		val currentTick = level?.gameTime ?: return false
		return lastGeneratedEnergyOnTick >= currentTick - 10L
	}

	protected var fePerTick: Int = 0
		set(value) {
			if (field == value) return
			field = value
			setChanged()
		}

	protected var burnTimeRemaining: Int = 0
		set(value) {
			if (field == value) return
			field = value
			setChanged()
		}

	override fun serverTick(level: ServerLevel) {
		super.serverTick(level)

		addToNetwork(level)
		pushOutEnergy(level)

		if (isOverloaded()) {
			val wasLit = blockState.getValue(GeneratorBlock.LIT)
			changeLitState(level, wasLit, false)
			return
		}

		val speed = container.getSpeed()
		var success = false
		for (i in 0 until speed) {
			if (generatorTick(level)) success = true
		}

		if (success) {
			lastGeneratedEnergyOnTick = level.gameTime
			effectOnSuccess(level)
		}
	}

	protected fun pushOutEnergy(level: ServerLevel) {
		for (dir in Direction.entries) {
			val energyCap = level.getCapability(
				Capabilities.Energy.BLOCK,
				blockPos.relative(dir),
				dir.opposite
			) ?: continue

			if (energyCap.getAmountAsLong() >= energyCap.getCapacityAsLong()) continue

			val energyToPush = energyStorage.getAmountAsInt()
			if (energyToPush <= 0) continue

			val pushed = energyCap.insertEnergy(energyToPush, false)
			if (pushed > 0) {
				energyStorage.extractEnergy(pushed, false)
				setChanged()
				if (pushed >= energyToPush) break
			}
		}
	}

	protected open fun addToNetwork(level: ServerLevel) {
		val owner = ownerUuid ?: return
		val network = RainbowGeneratorHandler.get(level).getGeneratorNetwork(owner)
		network.addGenerator(this)
	}

	protected open fun effectOnSuccess(level: ServerLevel) {}

	/**
	 * @return true if it generated energy
	 */
	protected open fun generatorTick(level: ServerLevel): Boolean {
		if (burnTimeRemaining <= 0) {
			fePerTick = 0
			if (!tryStartBurning(level)) {
				val wasLit = blockState.getValue(GeneratorBlock.LIT)
				changeLitState(level, wasLit, false)
				return false
			}
		}

		val wasLit = blockState.getValue(GeneratorBlock.LIT)
		val shouldBeLit = generateEnergy(level)
		changeLitState(level, wasLit, shouldBeLit)

		return shouldBeLit
	}

	protected open fun changeLitState(level: ServerLevel, wasLit: Boolean, shouldBeLit: Boolean) {
		if (wasLit != shouldBeLit) {
			val blockState = level.getBlockState(worldPosition)
			level.setBlockAndUpdate(
				worldPosition,
				blockState.setValue(GeneratorBlock.LIT, shouldBeLit)
			)
		}
	}

	protected abstract fun tryStartBurning(level: ServerLevel): Boolean

	/**
	 * @return true if it generated energy
	 */
	protected open fun generateEnergy(level: ServerLevel): Boolean {
		val remainingCapacity = energyStorage.getCapacityAsInt() - energyStorage.getAmountAsInt()
		if (remainingCapacity <= 0) return false

		val energyToGenerate = fePerTick.coerceAtMost(remainingCapacity)
		energyStorage.insertEnergy(energyToGenerate, false)

		burnTimeRemaining--
		setChanged()
		return true
	}

	override fun clientTick(level: Level) {}

	override fun getDisplayName(): Component = blockState.block.name

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? {
		return SingleItemGeneratorMenu(containerId, playerInventory, container, containerData)
	}

	override fun saveAdditional(tag: ValueOutput) {
		super.saveAdditional(tag)

		tag.putInt(BURN_TIME_REMAINING_NBT, burnTimeRemaining)
		tag.putInt(FE_PER_TICK_NBT, fePerTick)
		tag.saveEnergy(STORED_ENERGY_NBT, energyStorage)
		tag.saveItems(container)
	}

	override fun loadAdditional(tag: ValueInput) {
		super.loadAdditional(tag)

		burnTimeRemaining = tag.getIntOr(BURN_TIME_REMAINING_NBT, 0)
		fePerTick = tag.getIntOr(FE_PER_TICK_NBT, 0)
		tag.loadEnergy(STORED_ENERGY_NBT, energyStorage)
		tag.loadItems(container)
	}

	companion object {
		const val BURN_TIME_REMAINING_NBT = "BurnTimeRemaining"
		const val FE_PER_TICK_NBT = "FePerTick"
		const val STORED_ENERGY_NBT = "StoredEnergy"

		const val DEFAULT_GENERATOR_CONTAINER_DATA_SIZE = 3
		const val MAX_ENERGY_DATA_INDEX = 0
		const val CURRENT_ENERGY_DATA_INDEX = 1
		const val BURN_TIME_REMAINING_DATA_INDEX = 2

		fun tick(
			level: Level,
			blockPos: BlockPos,
			blockState: BlockState,
			blockEntity: GeneratorBlockEntity
		) {
			if (level is ServerLevel) {
				blockEntity.serverTick(level)
			} else {
				blockEntity.clientTick(level)
			}
		}

	}

}
