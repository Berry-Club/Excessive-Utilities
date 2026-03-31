package dev.aaronhowser.mods.excessive_utilities.menu.fluid_filter_menu

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.aaron.packet.c2s.ClientClickedMenuButton
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.menu.components.ToggleSpriteButton
import dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu.ItemFilterScreen
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

//TODO: Texture and move buttons around
class FluidFilterScreen(
	menu: FluidFilterMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<FluidFilterMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val inventoryLabelOffsetY: Int = 55

	private lateinit var invertButton: Button
	private lateinit var useTagsButton: Button
	private lateinit var ignoreAllComponentsButton: Button

	override fun baseInit() {
		super.baseInit()

		val buttonY = topPos + 110
		val middleX = leftPos + background.width / 2

		val buttonWidth = 20
		val buttonSpacing = 10

		var buttonX = middleX - buttonSpacing - buttonWidth - buttonSpacing

		invertButton = ToggleSpriteButton(
			x = buttonX,
			y = buttonY,
			width = 20,
			height = 20,
			font = font,
			sprites = Pair(ItemFilterScreen.INVERT_ON, ItemFilterScreen.INVERT_OFF),
			messages = Pair(
				ModMenuLang.INVERTED_ON.toComponent(),
				ModMenuLang.INVERTED_OFF.toComponent()
			),
			isOnGetter = { menu.isInverted() },
			onPress = {
				val packet = ClientClickedMenuButton(FluidFilterMenu.TOGGLE_INVERTED_BUTTON_ID)
				packet.messageServer()
			}
		)


		buttonX += buttonWidth + buttonSpacing

		useTagsButton = ToggleSpriteButton(
			x = buttonX,
			y = buttonY,
			width = 20,
			height = 20,
			font = font,
			sprites = Pair(ItemFilterScreen.USE_TAGS_ON, ItemFilterScreen.USE_TAGS_OFF),
			messages = Pair(
				ModMenuLang.USE_TAGS_ON.toComponent(),
				ModMenuLang.USE_TAGS_OFF.toComponent()
			),
			isOnGetter = { menu.useTags() },
			onPress = {
				val packet = ClientClickedMenuButton(FluidFilterMenu.TOGGLE_USE_TAGS_BUTTON_ID)
				packet.messageServer()
			}
		)

		buttonX += buttonWidth + buttonSpacing

		ignoreAllComponentsButton = ToggleSpriteButton(
			x = buttonX,
			y = buttonY,
			width = 20,
			height = 20,
			font = font,
			sprites = Pair(ItemFilterScreen.IGNORE_ALL_COMPONENTS_ON, ItemFilterScreen.IGNORE_ALL_COMPONENTS_OFF),
			messages = Pair(
				ModMenuLang.IGNORE_ALL_COMPONENTS_ON.toComponent(),
				ModMenuLang.IGNORE_ALL_COMPONENTS_OFF.toComponent()
			),
			isOnGetter = { menu.ignoreAllComponents() },
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
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/fluid_filter.png"), 176, 223)
	}

}