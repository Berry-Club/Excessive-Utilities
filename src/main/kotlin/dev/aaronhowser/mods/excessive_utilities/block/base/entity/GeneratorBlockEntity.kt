package dev.aaronhowser.mods.excessive_utilities.block.base.entity

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.getUuidOrNull
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.putUuidIfNotNull
import dev.aaronhowser.mods.excessive_utilities.block.GeneratorBlock
import dev.aaronhowser.mods.excessive_utilities.block.base.ContainerContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block.base.GeneratorType
import dev.aaronhowser.mods.excessive_utilities.handler.rainbow_generator.RainbowGeneratorHandler
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.IntTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.Container
import net.minecraft.world.ContainerHelper
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage
import net.neoforged.neoforge.items.IItemHandlerModifiable
import java.util.*

abstract class GeneratorBlockEntity(
	type: BlockEntityType<*>,
	pos: BlockPos,
	blockState: BlockState
) : BlockEntity(type, pos, blockState), ContainerContainer {

	abstract val generatorType: GeneratorType
	var ownerUuid: UUID? = null

	protected val energyStorage = EnergyStorage(1_000_000)

	protected open val container: GeneratorContainer? =
		object : GeneratorContainer(this@GeneratorBlockEntity, amountInputs = 1) {
			override fun canPlaceInput(stack: ItemStack): Boolean = isValidInput(stack)
			override fun canPlaceSecondaryInput(stack: ItemStack): Boolean = isValidSecondaryInput(stack)
			override fun canPlaceUpgrade(stack: ItemStack): Boolean = isValidUpgrade(stack)
		}

	override fun getContainer(): Container? = container
	open fun getItemHandler(direction: Direction?): IItemHandlerModifiable? = container?.itemHandler

	protected open fun isValidInput(itemStack: ItemStack) = true
	protected open fun isValidSecondaryInput(itemStack: ItemStack) = true
	protected open fun isValidUpgrade(itemStack: ItemStack) = true

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

	protected open fun serverTick(level: ServerLevel) {
		addToNetwork(level)

		val speed = container?.getSpeed() ?: 1
		var success = false
		for (i in 0 until speed) {
			if (generatorTick(level)) success = true
		}

		if (success) {
			effectOnSuccess(level)
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
			val startedBurning = tryStartBurning(level)
			val wasLit = blockState.getValue(GeneratorBlock.LIT)

			if (startedBurning && !wasLit) {
				level.setBlockAndUpdate(worldPosition, blockState.setValue(GeneratorBlock.LIT, true))
			} else if (!startedBurning && wasLit) {
				level.setBlockAndUpdate(worldPosition, blockState.setValue(GeneratorBlock.LIT, false))
			}

			if (burnTimeRemaining <= 0) return false
		}

		val wasLit = blockState.getValue(GeneratorBlock.LIT)
		val generatedPower = generateEnergy(level)

		if (generatedPower && !wasLit) {
			level.setBlockAndUpdate(worldPosition, blockState.setValue(GeneratorBlock.LIT, true))
		} else if (!generatedPower && wasLit) {
			level.setBlockAndUpdate(worldPosition, blockState.setValue(GeneratorBlock.LIT, false))
		}

		return generatedPower
	}

	protected abstract fun tryStartBurning(level: ServerLevel): Boolean

	/**
	 * @return true if it generated energy
	 */
	protected open fun generateEnergy(level: ServerLevel): Boolean {
		val remainingCapacity = energyStorage.maxEnergyStored - energyStorage.energyStored
		if (remainingCapacity <= 0) return false

		val energyToGenerate = fePerTick.coerceAtMost(remainingCapacity)
		energyStorage.receiveEnergy(energyToGenerate, false)

		burnTimeRemaining--
		setChanged()
		return true
	}

	protected open fun clientTick(level: Level) {}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.putInt(BURN_TIME_REMAINING_NBT, burnTimeRemaining)
		tag.putInt(FE_PER_TICK_NBT, fePerTick)
		tag.put(STORED_ENERGY_NBT, energyStorage.serializeNBT(registries))
		tag.putUuidIfNotNull(OWNER_UUID_NBT, ownerUuid)

		val container = container
		if (container != null) {
			ContainerHelper.saveAllItems(tag, container.items, registries)
		}
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		burnTimeRemaining = tag.getInt(BURN_TIME_REMAINING_NBT)
		fePerTick = tag.getInt(FE_PER_TICK_NBT)
		ownerUuid = tag.getUuidOrNull(OWNER_UUID_NBT)

		val storedEnergyTag = tag.get(STORED_ENERGY_NBT)
		if (storedEnergyTag is IntTag) {
			energyStorage.deserializeNBT(registries, storedEnergyTag)
		}

		val container = container
		if (container != null) {
			ContainerHelper.loadAllItems(tag, container.items, registries)
		}
	}

	companion object {
		const val OWNER_UUID_NBT = "OwnerUUID"
		const val BURN_TIME_REMAINING_NBT = "BurnTimeRemaining"
		const val FE_PER_TICK_NBT = "FePerTick"
		const val STORED_ENERGY_NBT = "StoredEnergy"

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

		fun getEnergyCapability(transmitter: GeneratorBlockEntity, direction: Direction?): IEnergyStorage {
			return transmitter.energyStorage
		}
	}

}