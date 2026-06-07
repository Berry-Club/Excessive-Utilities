package dev.aaronhowser.mods.excessive_utilities.menu.single_fluid_generator

import dev.aaronhowser.mods.aaron.menu.MenuWithInventory
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.generator.GeneratorContainer
import dev.aaronhowser.mods.excessive_utilities.block_entity.generator.MagmaticGeneratorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData
import net.minecraft.world.inventory.Slot

class SingleFluidGeneratorMenu(
	containerId: Int,
	playerInventory: Inventory,
	val generatorContainer: Container,
	val generatorContainerData: ContainerData,
	val blockEntity: MagmaticGeneratorBlockEntity?
) : MenuWithInventory(ModMenuTypes.SINGLE_FLUID_GENERATOR.get(), containerId, playerInventory) {

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

		this.addSlot(upgradeSlot)
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
		): SingleFluidGeneratorMenu {
			val pos = data.readBlockPos()
			val be = playerInventory.player.level().getBlockEntity(pos) as? MagmaticGeneratorBlockEntity

			return SingleFluidGeneratorMenu(
				containerId,
				playerInventory,
				SimpleContainer(CONTAINER_SIZE),
				SimpleContainerData(GeneratorBlockEntity.DEFAULT_GENERATOR_CONTAINER_DATA_SIZE),
				be
			)
		}
	}
}
