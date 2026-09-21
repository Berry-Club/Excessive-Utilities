package dev.aaronhowser.mods.excessive_utilities.menu.mechanical_user

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.nextEnum
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.prevEnum
import dev.aaronhowser.mods.excessive_utilities.block_entity.MechanicalUserBlockEntity
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.MechanicalInteractorBlockEntity
import dev.aaronhowser.mods.excessive_utilities.menu.mechanical_interactor.BaseMechanicalInteractorMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.world.Container
import net.minecraft.world.SimpleContainer
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.ContainerData
import net.minecraft.world.inventory.SimpleContainerData

class MechanicalUserMenu(
	containerId: Int,
	playerInventory: Inventory,
	machineContainer: Container,
	machineData: ContainerData
) : BaseMechanicalInteractorMenu(
	ModMenuTypes.MECHANICAL_USER.get(),
	containerId,
	playerInventory,
	machineContainer,
	machineData
) {

	constructor(containerId: Int, playerInventory: Inventory) :
			this(
				containerId,
				playerInventory,
				SimpleContainer(MechanicalInteractorBlockEntity.BASE_CONTAINER_SIZE),
				SimpleContainerData(MechanicalUserBlockEntity.MENU_DATA_SIZE)
			)

	init {
		initializeMenu(MechanicalUserBlockEntity.MENU_DATA_SIZE)
	}

	var interactionMode: MechanicalUserBlockEntity.InteractionMode
		get() {
			val index = machineData.get(MechanicalUserBlockEntity.INTERACTION_MODE_DATA_INDEX)
			return MechanicalUserBlockEntity.InteractionMode.fromOrdinal(index)
		}
		set(value) {
			machineData.set(MechanicalUserBlockEntity.INTERACTION_MODE_DATA_INDEX, value.ordinal)
		}

	var isLeftClick: Boolean
		get() = machineData.get(MechanicalUserBlockEntity.IS_LEFT_CLICK_DATA_INDEX) != 0
		set(value) {
			machineData.set(MechanicalUserBlockEntity.IS_LEFT_CLICK_DATA_INDEX, if (value) 1 else 0)
		}

	var useUpperLeftSlotOnly: Boolean
		get() = machineData.get(MechanicalUserBlockEntity.USE_UPPER_LEFT_SLOT_ONLY_DATA_INDEX) != 0
		set(value) {
			machineData.set(MechanicalUserBlockEntity.USE_UPPER_LEFT_SLOT_ONLY_DATA_INDEX, if (value) 1 else 0)
		}

	var isSneaking: Boolean
		get() = machineData.get(MechanicalUserBlockEntity.SNEAKING_DATA_INDEX) != 0
		set(value) {
			machineData.set(MechanicalUserBlockEntity.SNEAKING_DATA_INDEX, if (value) 1 else 0)
		}

	override fun getExpectedContainerSize(): Int = MechanicalInteractorBlockEntity.BASE_CONTAINER_SIZE

	override fun handleButtonPressed(buttonId: Int, isShiftDown: Boolean) {
		when (buttonId) {
			CYCLE_INTERACTION_MODE_BUTTON -> interactionMode = if (isShiftDown) {
				interactionMode.prevEnum()
			} else {
				interactionMode.nextEnum()
			}

			TOGGLE_LEFT_CLICK_BUTTON -> isLeftClick = !isLeftClick
			TOGGLE_UPPER_LEFT_SLOT_ONLY_BUTTON -> useUpperLeftSlotOnly = !useUpperLeftSlotOnly
			TOGGLE_SNEAKING_BUTTON -> isSneaking = !isSneaking

			else -> {
				super.handleButtonPressed(buttonId, isShiftDown)
				return
			}
		}
	}

	companion object {
		const val CYCLE_INTERACTION_MODE_BUTTON = 1
		const val TOGGLE_LEFT_CLICK_BUTTON = 2
		const val TOGGLE_UPPER_LEFT_SLOT_ONLY_BUTTON = 3
		const val TOGGLE_SNEAKING_BUTTON = 4
	}

}