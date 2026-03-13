package dev.aaronhowser.mods.excessive_utilities.menu.resonator

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.aaron.menu.components.OutputSlot
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.block_entity.ResonatorBlockEntity
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

class ResonatorMenu(
	containerId: Int,
	playerInventory: Inventory,
	val resonatorContainer: Container,
	val resonatorContainerData: ContainerData
) : MenuWithInventory(ModMenuTypes.RESONATOR.get(), containerId, playerInventory) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(ResonatorBlockEntity.CONTAINER_SIZE),
				SimpleContainerData(ResonatorBlockEntity.CONTAINER_DATA_SIZE)
			)

	init {
		checkContainerSize(resonatorContainer, ResonatorBlockEntity.CONTAINER_SIZE)
		checkContainerDataCount(resonatorContainerData, ResonatorBlockEntity.CONTAINER_DATA_SIZE)

		addSlots()
		addDataSlots(resonatorContainerData)
		addPlayerInventorySlots(90)
	}

	override fun addSlots() {
		val inputSlot = Slot(resonatorContainer, ResonatorBlockEntity.INPUT_SLOT, 57, 42)
		val outputSlot = OutputSlot(resonatorContainer, ResonatorBlockEntity.OUTPUT_SLOT, 104, 42)
		val upgradeSlot = object : Slot(resonatorContainer, ResonatorBlockEntity.UPGRADE_SLOT, 152, 42) {
			override fun mayPlace(stack: ItemStack): Boolean {
				return stack.isItem(ModItemTagsProvider.SPEED_UPGRADES)
			}
		}

		this.addSlot(inputSlot)
		this.addSlot(outputSlot)
		this.addSlot(upgradeSlot)
	}

	fun getProgress(): Int = resonatorContainerData.get(ResonatorBlockEntity.PROGRESS_DATA_INDEX)
	fun getMaxProgress(): Int = resonatorContainerData.get(ResonatorBlockEntity.MAX_PROGRESS_DATA_INDEX)
	fun getGpUsage(): Double = resonatorContainerData.get(ResonatorBlockEntity.GP_HUNDREDTHS_COST_DATA_INDEX) / 100.0

	override fun quickMoveStack(player: Player, index: Int): ItemStack {
		return ItemStack.EMPTY
	}

	override fun stillValid(player: Player): Boolean {
		return resonatorContainer.stillValid(player)
	}
}