package dev.aaronhowser.mods.excessive_utilities.menu.fluid_filter_menu

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.components.ChangingTextButton
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.aaron.packet.c2s.ClientClickedMenuButton
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.ItemFilterItem
import dev.aaronhowser.mods.excessive_utilities.item.component.FluidFilterFlagsComponent
import dev.aaronhowser.mods.excessive_utilities.item.component.ItemFilterFlagsComponent
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class FluidFilterScreen(
	menu: FluidFilterMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<FluidFilterMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND

	private lateinit var invertButton: ChangingTextButton
	private lateinit var useTagsButton: ChangingTextButton
	private lateinit var ignoreAllComponentsButton: ChangingTextButton

	override fun baseInit() {
		super.baseInit()

		inventoryLabelY = 16

		val buttonWidth = 120

		invertButton = ChangingTextButton(
			x = rightPos - buttonWidth - 5,
			y = topPos + 5,
			width = buttonWidth,
			height = 20,
			messageGetter = { FluidFilterFlagsComponent.Flag.INVERTED.getMessage(menu.isInverted()) },
			onPress = {
				val packet = ClientClickedMenuButton(FluidFilterMenu.TOGGLE_INVERTED_BUTTON_ID)
				packet.messageServer()
			}
		)

		useTagsButton = ChangingTextButton(
			x = rightPos - buttonWidth - 5,
			y = topPos + 5 + 20 + 5,
			width = buttonWidth,
			height = 20,
			messageGetter = { FluidFilterFlagsComponent.Flag.USE_TAGS.getMessage(menu.useTags()) },
			onPress = {
				val packet = ClientClickedMenuButton(FluidFilterMenu.TOGGLE_USE_TAGS_BUTTON_ID)
				packet.messageServer()
			}
		)

		ignoreAllComponentsButton = ChangingTextButton(
			x = rightPos - buttonWidth - 5,
			y = topPos + 5 + (20 + 5) * 2,
			width = buttonWidth,
			height = 20,
			messageGetter = { FluidFilterFlagsComponent.Flag.IGNORE_ALL_COMPONENTS.getMessage(menu.ignoreAllComponents()) },
			onPress = {
				val packet = ClientClickedMenuButton(FluidFilterMenu.TOGGLE_IGNORE_ALL_COMPONENTS_BUTTON_ID)
				packet.messageServer()
			}
		)

		addRenderableWidget(invertButton)
		addRenderableWidget(useTagsButton)
		addRenderableWidget(ignoreAllComponentsButton)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/item_transfer_node.png"), 176, 173)
	}

}