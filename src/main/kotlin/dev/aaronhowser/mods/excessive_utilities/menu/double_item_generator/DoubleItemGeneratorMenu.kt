package dev.aaronhowser.mods.excessive_utilities.menu.double_item_generator

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class DoubleItemGeneratorMenu(
	containerId: Int,
	playerInventory: Inventory,
	val generatorContainer: Container,
	val generatorContainerData: ContainerData
) : MenuWithInventory(ModMenuTypes.SINGLE_ITEM_GENERATOR.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(CONTAINER_SIZE),
				SimpleContainerData(GeneratorBlockEntity.DEFAULT_GENERATOR_CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(generatorContainer, CONTAINER_SIZE)
		checkContainerDataCount(generatorContainerData, GeneratorBlockEntity.DEFAULT_GENERATOR_CONTAINER_DATA_SIZE)

		addSlots()
		addDataSlots(generatorContainerData)
		addPlayerInventorySlots(90)
	}

	fun getMaxEnergy(): Int = generatorContainerData.get(GeneratorBlockEntity.MAX_ENERGY_DATA_INDEX)
	fun getCurrentEnergy(): Int = generatorContainerData.get(GeneratorBlockEntity.CURRENT_ENERGY_DATA_INDEX)
	fun getBurnTimeRemaining(): Int = generatorContainerData.get(GeneratorBlockEntity.BURN_TIME_REMAINING_DATA_INDEX)

	override fun addSlots() {
		val upgradeSlot = Slot(generatorContainer, GeneratorContainer.UPGRADE_SLOT, 8, 42)
		val inputSlots = Slot(generatorContainer, GeneratorContainer.INPUT_SLOT, 69, 42)

		this.addSlot(upgradeSlot)
		this.addSlot(inputSlots)
	}

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return generatorContainer.stillValid(player)
	}

	companion object {
		const val CONTAINER_SIZE = 2
	}
}