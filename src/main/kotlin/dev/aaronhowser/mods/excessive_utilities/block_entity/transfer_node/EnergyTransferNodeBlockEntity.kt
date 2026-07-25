package dev.aaronhowser.mods.excessive_utilities.block_entity.transfer_node

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.loadEnergy
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.saveEnergy
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.TransferNodeBlockEntity
import dev.aaronhowser.mods.excessive_utilities.menu.energy_transfer_node.EnergyTransferNodeMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlockEntityTypes
import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.level.block.state.BlockState
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.EnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage

class EnergyTransferNodeBlockEntity(
	pos: BlockPos,
	blockState: BlockState
) : TransferNodeBlockEntity(ModBlockEntityTypes.ENERGY_TRANSFER_NODE.get(), pos, blockState) {

	private val bufferEnergyStorage: EnergyStorage = EnergyStorage(1_000_000)

	private fun getParentEnergyStorage(level: ServerLevel): IEnergyStorage? {
		return level.getCapability(Capabilities.EnergyStorage.BLOCK, placedOnPos, placedOnDirection.opposite)
	}

	override fun getBufferAmount(): Int = bufferEnergyStorage.energyStored

	override fun pullFromPingPos(level: ServerLevel) {
		if (hasConfiguredReceiver()) return

		val neighborStorages = getEnergyStorageAroundPing(level)
		if (neighborStorages.isEmpty()) return

		val amountThatCanFit = bufferEnergyStorage.maxEnergyStored - bufferEnergyStorage.energyStored
		if (amountThatCanFit <= 0) return

		val maxAmountToPull = getAmountPerTick()
		val amountToExtract = maxAmountToPull.coerceAtMost(amountThatCanFit)

		for (storage in neighborStorages) {
			val simulated = storage.extractEnergy(amountToExtract, true)
			if (simulated <= 0) continue

			val actualExtracted = storage.extractEnergy(simulated, false)
			if (actualExtracted <= 0) continue

			bufferEnergyStorage.receiveEnergy(actualExtracted, false)
			didWorkThisTick = true
		}
	}

	override fun pushIntoParent(level: ServerLevel) {
		val parentEnergyStorage = getParentEnergyStorage(level) ?: return

		val energyToPush = bufferEnergyStorage.extractEnergy(bufferEnergyStorage.energyStored, true)
		if (energyToPush <= 0) return

		val actualAmountPushed = parentEnergyStorage.receiveEnergy(energyToPush, false)
		if (!hasCreativeUpgrade()) {
			bufferEnergyStorage.extractEnergy(actualAmountPushed, false)
		}

		didWorkThisTick = true
	}

	private fun getEnergyStorageAroundPing(level: ServerLevel): List<IEnergyStorage> {
		return getCapabilitiesAroundPing(level) { neighborPos, side ->
			level.getCapability(Capabilities.EnergyStorage.BLOCK, neighborPos, side)
		}
	}

	override fun pushIntoPingPos(level: ServerLevel) {
		var energyToPush = bufferEnergyStorage.extractEnergy(bufferEnergyStorage.energyStored, true)
		if (energyToPush <= 0) return

		visitEnderReceivers(level) { receiver ->
			val accepted = transferToEnderReceiver(receiver, energyToPush)
			energyToPush -= accepted
			accepted > 0
		}

		if (energyToPush <= 0) return

		for (storage in getEnergyStorageAroundPing(level)) {
			val accepted = storage.receiveEnergy(energyToPush, false)
			if (accepted <= 0) continue

			if (!hasCreativeUpgrade()) {
				bufferEnergyStorage.extractEnergy(accepted, false)
			}

			energyToPush -= accepted
			didWorkThisTick = true

			if (energyToPush <= 0) break
		}
	}

	override fun pullFromParent(level: ServerLevel) {
		val parentStorage = getParentEnergyStorage(level) ?: return

		val amountThatCanFit = bufferEnergyStorage.maxEnergyStored - bufferEnergyStorage.energyStored
		if (amountThatCanFit <= 0) return

		val maxAmountToPull = getAmountPerTick()
		val amountToExtract = maxAmountToPull.coerceAtMost(amountThatCanFit)
		if (amountToExtract <= 0) return

		val simulated = parentStorage.extractEnergy(amountToExtract, true)
		if (simulated <= 0) return

		val actualExtracted = parentStorage.extractEnergy(simulated, false)
		if (actualExtracted <= 0) return

		bufferEnergyStorage.receiveEnergy(actualExtracted, false)
		didWorkThisTick = true
	}

	private fun getAmountPerTick(): Int {
		return if (hasStackUpgrade()) 192_000 else 3_000
	}

	override fun getContainerDataCount(): Int = CONTAINER_DATA_SIZE

	override fun getContainerData(index: Int): Int {
		return when (index) {
			STORED_ENERGY_DATA_INDEX -> bufferEnergyStorage.energyStored
			MAX_ENERGY_DATA_INDEX -> bufferEnergyStorage.maxEnergyStored
			else -> super.getContainerData(index)
		}
	}

	fun receiveEnderTransfer(maxAmount: Int): Int {
		return bufferEnergyStorage.receiveEnergy(maxAmount, false)
	}

	private fun transferToEnderReceiver(
		receiver: TransferNodeBlockEntity,
		maxAmount: Int
	): Int {
		if (receiver !is EnergyTransferNodeBlockEntity) return 0

		val accepted = receiver.receiveEnderTransfer(maxAmount)
		if (accepted <= 0) return 0

		if (!hasCreativeUpgrade()) {
			bufferEnergyStorage.extractEnergy(accepted, false)
		}

		didWorkThisTick = true
		return accepted
	}

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu {
		return EnergyTransferNodeMenu(containerId, playerInventory, upgradeContainer, containerData)
	}

	override fun saveAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.saveAdditional(tag, registries)

		tag.saveEnergy(BUFFER_ENERGY_NBT, bufferEnergyStorage, registries)
	}

	override fun loadAdditional(tag: CompoundTag, registries: HolderLookup.Provider) {
		super.loadAdditional(tag, registries)

		tag.loadEnergy(BUFFER_ENERGY_NBT, bufferEnergyStorage, registries)
	}

	companion object {
		const val BUFFER_ENERGY_NBT = "BufferEnergy"

		const val CONTAINER_DATA_SIZE = 5
		const val STORED_ENERGY_DATA_INDEX = 3
		const val MAX_ENERGY_DATA_INDEX = 4
	}

}