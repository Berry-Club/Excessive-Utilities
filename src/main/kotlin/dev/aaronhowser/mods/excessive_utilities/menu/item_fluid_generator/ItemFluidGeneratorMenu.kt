package dev.aaronhowser.mods.excessive_utilities.menu.item_fluid_generator

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block_entity.generator.ItemAndFluidInputDataDrivenGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot
import net.minecraft.world.item.ItemStack

class ItemFluidGeneratorMenu(
	containerId: Int,
	playerInventory: Inventory,
	val generatorContainer: Container,
	val generatorContainerData: ContainerData,
	val blockEntity: ItemAndFluidInputDataDrivenGeneratorBlockEntity?
) : MenuWithInventory(ModMenuTypes.ITEM_FLUID_GENERATOR.get(), containerId, playerInventory) {

	init {
		checkContainerSize(generatorContainer, CONTAINER_SIZE)
		checkContainerDataCount(generatorContainerData, GeneratorBlockEntity.DEFAULT_GENERATOR_CONTAINER_DATA_SIZE)

		addSlots()
		addDataSlots(generatorContainerData)
		addPlayerInventorySlots(96)
	}

	fun getMaxEnergy(): Int = generatorContainerData.get(GeneratorBlockEntity.MAX_ENERGY_DATA_INDEX)
	fun getCurrentEnergy(): Int = generatorContainerData.get(GeneratorBlockEntity.CURRENT_ENERGY_DATA_INDEX)
	fun getBurnTimeRemaining(): Int = generatorContainerData.get(GeneratorBlockEntity.BURN_TIME_REMAINING_DATA_INDEX)

	override fun addSlots() {
		val upgradeSlot = Slot(generatorContainer, GeneratorContainer.UPGRADE_SLOT, 153, 5)
		val inputSlots = Slot(generatorContainer, GeneratorContainer.INPUT_SLOT, 68, 46)

		this.addSlot(upgradeSlot)
		this.addSlot(inputSlots)
	}

	override fun stillValid(player: Player): Boolean {
		return generatorContainer.stillValid(player)
	}

	companion object {
		const val CONTAINER_SIZE = 2

		fun fromNetwork(
			containerId: Int,
			playerInventory: Inventory,
			data: RegistryFriendlyByteBuf
		): ItemFluidGeneratorMenu {
			val pos = data.readBlockPos()
			val be = playerInventory.player.level().getBlockEntity(pos) as? ItemAndFluidInputDataDrivenGeneratorBlockEntity

			return ItemFluidGeneratorMenu(
				containerId,
				playerInventory,
				SimpleContainer(CONTAINER_SIZE),
				SimpleContainerData(GeneratorBlockEntity.DEFAULT_GENERATOR_CONTAINER_DATA_SIZE),
				be
			)
		}
	}
}
