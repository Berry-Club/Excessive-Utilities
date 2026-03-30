package dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.components.ChangingTextButton
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.aaron.menu.textures.ScreenSprite
import dev.aaronhowser.mods.aaron.packet.c2s.ClientClickedMenuButton
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.item.component.ItemFilterFlagsComponent
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

		val buttonWidth = 120

		invertButton = ChangingTextButton(
			x = leftPos - buttonWidth,
			y = topPos + 5,
			width = buttonWidth,
			height = 20,
			messageGetter = { ItemFilterFlagsComponent.Flag.INVERTED.getMessage(menu.isInverted()) },
			onPress = {
				val packet = ClientClickedMenuButton(ItemFilterMenu.TOGGLE_INVERTED_BUTTON_ID)
				packet.messageServer()
			}
		)

		useTagsButton = ChangingTextButton(
			x = leftPos - buttonWidth,
			y = topPos + 5 + 20 + 5,
			width = buttonWidth,
			height = 20,
			messageGetter = { ItemFilterFlagsComponent.Flag.USE_TAGS.getMessage(menu.useTags()) },
			onPress = {
				val packet = ClientClickedMenuButton(ItemFilterMenu.TOGGLE_USE_TAGS_BUTTON_ID)
				packet.messageServer()
			}
		)

//		ignoreDamageButton =
//			ToggleStackButton(
//				x = leftPos + 5,
//				y = topPos + 5 + (20 + 5) * 2,
//				font = font,
//				width = 20,
//				height = 20,
//				itemStackGetter = {
//					if (menu.ignoreDamage()) {
//						Items.WOODEN_PICKAXE.withComponent(DataComponents.DAMAGE, 10)
//					} else {
//						Items.DIAMOND_PICKAXE.defaultInstance
//					}
//				},
//				messageGetter = {
//					ItemFilterFlagsComponent.Flag.IGNORE_DAMAGE.getMessage(menu.ignoreDamage())
//				},
//				isOnGetter = { menu.ignoreDamage() },
//				onPress = {
//					val packet = ClientClickedMenuButton(ItemFilterMenu.TOGGLE_IGNORE_DAMAGE_BUTTON_ID)
//					packet.messageServer()
//				}
//			)

		ignoreDamageButton = ToggleSpriteButton(
			x = leftPos + 5,
			y = topPos + 20,
			width = 20,
			height = 20,
			font = font,
			sprites = Pair(IGNORE_DAMAGE_ON, IGNORE_DAMAGE_OFF),
			messages = Pair(
				Component.literal("Ignore Damage: ON"),
				Component.literal("Ignore Damage: OFF")
			),
			isOnGetter = { menu.ignoreDamage() },
			onPress = {
				val packet = ClientClickedMenuButton(ItemFilterMenu.TOGGLE_IGNORE_DAMAGE_BUTTON_ID)
				packet.messageServer()
			}
		)

		ignoreAllComponentsButton = ChangingTextButton(
			x = leftPos - buttonWidth,
			y = topPos + 5 + (20 + 5) * 3,
			width = buttonWidth,
			height = 20,
			messageGetter = { ItemFilterFlagsComponent.Flag.IGNORE_ALL_COMPONENTS.getMessage(menu.ignoreAllComponents()) },
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

		val IGNORE_DAMAGE_OFF = ScreenSprite(
			ExcessiveUtilities.modResource("filter/ignore_damage_off"),
			16, 16
		)

		val IGNORE_DAMAGE_ON = ScreenSprite(
			ExcessiveUtilities.modResource("filter/ignore_damage_on"),
			16, 16
		)
	}

}