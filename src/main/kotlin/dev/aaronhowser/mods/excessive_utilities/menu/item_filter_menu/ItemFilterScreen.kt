package dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.aaron.menu.textures.ScreenSprite
import dev.aaronhowser.mods.aaron.packet.c2s.ClientClickedMenuButton
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.menu.components.ToggleSpriteButton
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class ItemFilterScreen(
	menu: ItemFilterMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<ItemFilterMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val inventoryLabelOffsetY: Int = 73

	private lateinit var invertButton: Button
	private lateinit var useTagsButton: Button
	private lateinit var ignoreDamageButton: Button
	private lateinit var ignoreAllComponentsButton: Button

	override fun baseInit() {
		super.baseInit()

		val buttonY = topPos + 110
		val middleX = leftPos + background.width / 2

		val buttonWidth = 20
		val buttonSpacing = 10

		var buttonX = middleX - (buttonSpacing / 2) - buttonWidth - buttonSpacing - buttonWidth

		invertButton = ToggleSpriteButton(
			x = buttonX,
			y = buttonY,
			width = 20,
			height = 20,
			font = font,
			sprites = Pair(INVERT_ON, INVERT_OFF),
			messages = Pair(
				ModMenuLang.INVERTED_ON.toComponent(),
				ModMenuLang.INVERTED_OFF.toComponent()
			),
			isOnGetter = { menu.isInverted() },
			onPress = {
				val packet = ClientClickedMenuButton(ItemFilterMenu.TOGGLE_INVERTED_BUTTON_ID)
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
			sprites = Pair(USE_TAGS_ON, USE_TAGS_OFF),
			messages = Pair(
				ModMenuLang.USE_TAGS_ON.toComponent(),
				ModMenuLang.USE_TAGS_OFF.toComponent()
			),
			isOnGetter = { menu.useTags() },
			onPress = {
				val packet = ClientClickedMenuButton(ItemFilterMenu.TOGGLE_USE_TAGS_BUTTON_ID)
				packet.messageServer()
			}
		)

		buttonX += buttonWidth + buttonSpacing

		ignoreDamageButton = ToggleSpriteButton(
			x = buttonX,
			y = buttonY,
			width = 20,
			height = 20,
			font = font,
			sprites = Pair(IGNORE_DAMAGE_ON, IGNORE_DAMAGE_OFF),
			messages = Pair(
				ModMenuLang.IGNORE_DAMAGE_ON.toComponent(),
				ModMenuLang.IGNORE_DAMAGE_OFF.toComponent()
			),
			isOnGetter = { menu.ignoreDamage() },
			onPress = {
				val packet = ClientClickedMenuButton(ItemFilterMenu.TOGGLE_IGNORE_DAMAGE_BUTTON_ID)
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
			sprites = Pair(IGNORE_ALL_COMPONENTS_ON, IGNORE_ALL_COMPONENTS_OFF),
			messages = Pair(
				ModMenuLang.IGNORE_ALL_COMPONENTS_ON.toComponent(),
				ModMenuLang.IGNORE_ALL_COMPONENTS_OFF.toComponent()
			),
			isOnGetter = { menu.ignoreAllComponents() },
			onPress = {
				val packet = ClientClickedMenuButton(ItemFilterMenu.TOGGLE_IGNORE_ALL_COMPONENTS_BUTTON_ID)
				packet.messageServer()
			}
		)

		addRenderableWidget(invertButton)
		addRenderableWidget(useTagsButton)
		addRenderableWidget(ignoreDamageButton)
		addRenderableWidget(ignoreAllComponentsButton)
	}

	companion object {
		val BACKGROUND = ScreenBackground(ExcessiveUtilities.modResource("textures/gui/item_filter.png"), 176, 241)

		val INVERT_OFF = ScreenSprite(
			ExcessiveUtilities.modResource("filter/invert_off"),
			16, 16
		)

		val INVERT_ON = ScreenSprite(
			ExcessiveUtilities.modResource("filter/invert_on"),
			16, 16
		)

		val USE_TAGS_OFF = ScreenSprite(
			ExcessiveUtilities.modResource("filter/use_tags_off"),
			16, 16
		)

		val USE_TAGS_ON = ScreenSprite(
			ExcessiveUtilities.modResource("filter/use_tags_on"),
			16, 16
		)

		val IGNORE_DAMAGE_OFF = ScreenSprite(
			ExcessiveUtilities.modResource("filter/ignore_damage_off"),
			16, 16
		)

		val IGNORE_DAMAGE_ON = ScreenSprite(
			ExcessiveUtilities.modResource("filter/ignore_damage_on"),
			16, 16
		)

		val IGNORE_ALL_COMPONENTS_OFF = ScreenSprite(
			ExcessiveUtilities.modResource("filter/ignore_all_components_off"),
			16, 16
		)

		val IGNORE_ALL_COMPONENTS_ON = ScreenSprite(
			ExcessiveUtilities.modResource("filter/ignore_all_components_on"),
			16, 16
		)
	}

}