package dev.aaronhowser.mods.excessive_utilities.menu.furnace

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.FilteredSlot
import dev.aaronhowser.mods.aaron.menu.components.OutputSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.EUFurnaceBlockEntity
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

//TODO: Progress arrow
class EUFurnaceMenu(
	containerId: Int,
	playerInventory: Inventory,
	val furnaceContainer: Container,
	val furnaceContainerData: ContainerData
) : MenuWithInventory(ModMenuTypes.FURNACE.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(EUFurnaceBlockEntity.CONTAINER_SIZE),
				SimpleContainerData(EUFurnaceBlockEntity.CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(furnaceContainer, EUFurnaceBlockEntity.CONTAINER_SIZE)
		checkContainerDataCount(furnaceContainerData, EUFurnaceBlockEntity.CONTAINER_DATA_SIZE)

		addSlots()
		addDataSlots(furnaceContainerData)
		addPlayerInventorySlots(90)
	}

	override fun addSlots() {
		val inputSlot = Slot(furnaceContainer, EUFurnaceBlockEntity.INPUT_SLOT, 63, 42)
		val outputSlot = OutputSlot(furnaceContainer, EUFurnaceBlockEntity.OUTPUT_SLOT, 110, 42)
		val upgradeSlot = FilteredSlot(furnaceContainer, EUFurnaceBlockEntity.UPGRADE_SLOT, 26, 54) {
			it.isItem(ModItemTagsProvider.SPEED_UPGRADES)
		}

		addSlot(outputSlot)
		addSlot(inputSlot)
		addSlot(upgradeSlot)
	}

	fun getCurrentEnergy(): Int = furnaceContainerData.get(EUFurnaceBlockEntity.CURRENT_ENERGY_DATA_INDEX)
	fun getProgress(): Int = furnaceContainerData.get(EUFurnaceBlockEntity.PROGRESS_DATA_INDEX)

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return furnaceContainer.stillValid(player)
	}
}