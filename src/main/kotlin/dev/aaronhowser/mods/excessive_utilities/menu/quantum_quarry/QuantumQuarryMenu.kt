package dev.aaronhowser.mods.excessive_utilities.menu.quantum_quarry

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.QuantumQuarryBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

class QuantumQuarryMenu(
	containerId: Int,
	playerInventory: Inventory,
	val upgradesContainer: Container,
	val quarryContainerData: ContainerData
) : MenuWithInventory(ModMenuTypes.QUANTUM_QUARRY.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(QuantumQuarryBlockEntity.UPGRADE_CONTAINER_SIZE),
				SimpleContainerData(QuantumQuarryBlockEntity.CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(upgradesContainer, QuantumQuarryBlockEntity.UPGRADE_CONTAINER_SIZE)
		checkContainerDataCount(quarryContainerData, QuantumQuarryBlockEntity.CONTAINER_DATA_SIZE)

		addSlots()
		addDataSlots(quarryContainerData)
		addPlayerInventorySlots(116)
	}

	override fun addSlots() {
		val filterSlot = FilteredSlot(upgradesContainer, QuantumQuarryBlockEntity.ITEM_FILTER_SLOT_INDEX, 10, 24) {
			it.isItem(ModItems.ITEM_FILTER)
		}

		val enchantedBookSlot = FilteredSlot(upgradesContainer, QuantumQuarryBlockEntity.ENCHANTED_BOOK_SLOT_INDEX, 10, 42) {
			it.isItem(Items.ENCHANTED_BOOK)
		}

		val biomeFilterSlot = FilteredSlot(upgradesContainer, QuantumQuarryBlockEntity.BIOME_FILTER_SLOT_INDEX, 10, 60) {
			it.isItem(ModItems.BIOME_MARKER)
		}

		addSlot(filterSlot)
		addSlot(enchantedBookSlot)
		addSlot(biomeFilterSlot)
	}

	fun getCurrentEnergy(): Int = quarryContainerData.get(QuantumQuarryBlockEntity.CURRENT_ENERGY_DATA_INDEX)
	fun getTargetX(): Int = quarryContainerData.get(QuantumQuarryBlockEntity.TARGET_X_DATA_INDEX)
	fun getTargetY(): Int = quarryContainerData.get(QuantumQuarryBlockEntity.TARGET_Y_DATA_INDEX)
	fun getTargetZ(): Int = quarryContainerData.get(QuantumQuarryBlockEntity.TARGET_Z_DATA_INDEX)
	fun getProgress(): Float = quarryContainerData.get(QuantumQuarryBlockEntity.PROGRESS_PERCENT_DATA_INDEX) / 100f
	fun getAmountBlocksBroken(): Int = quarryContainerData.get(QuantumQuarryBlockEntity.AMOUNT_BLOCKS_BROKEN_DATA_INDEX)
	fun getBiomeId(): Int = quarryContainerData.get(QuantumQuarryBlockEntity.BIOME_ID_DATA_INDEX)

	override fun stillValid(player: Player): Boolean {
		return upgradesContainer.stillValid(player)
	}
}