package dev.aaronhowser.mods.excessive_utilities.menu.mechanical_user

import dev.aaronhowser.mods.aaron.menu.components.ChangingTextButton
import dev.aaronhowser.mods.aaron.menu.components.MultiStageSpriteButton
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.aaron.menu.textures.ScreenSprite
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.MechanicalInteractorBlockEntity.RedstoneMode
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.menu.ExcessiveUtilitiesScreen
import dev.aaronhowser.mods.excessive_utilities.menu.base.BaseMechanicalInteractorMenu
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class MechanicalUserScreen(
	menu: MechanicalUserMenu,
	playerInventory: Inventory,
	title: Component
) : ExcessiveUtilitiesScreen<MechanicalUserMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val titleLabelSprite: ResourceLocation = RED_LABEL

	override fun baseInit() {
		super.baseInit()

		val interactionModeButton = ChangingTextButton(
			x = leftPos + 80,
			y = topPos + 104,
			width = 68,
			height = 16,
			messageGetter = { menu.interactionMode.langKey.toComponent() },
			onPress = {
				buttonClicked(MechanicalUserMenu.CYCLE_INTERACTION_MODE_BUTTON)
			}
		)

		val leftClickButton = ChangingTextButton(
			x = leftPos + 8,
			y = topPos + 122,
			width = 68,
			height = 16,
			messageGetter = {
				if (menu.isLeftClick) {
					ModMenuLang.MECHANICAL_USER_CLICK_LEFT.toComponent()
				} else {
					ModMenuLang.MECHANICAL_USER_CLICK_RIGHT.toComponent()
				}
			},
			onPress = {
				buttonClicked(MechanicalUserMenu.TOGGLE_LEFT_CLICK_BUTTON)
			}
		)

		val upperLeftSlotButton = ChangingTextButton(
			x = leftPos + 80,
			y = topPos + 122,
			width = 68,
			height = 16,
			messageGetter = {
				if (menu.useUpperLeftSlotOnly) {
					ModMenuLang.MECHANICAL_USER_SLOT_UPPER_LEFT.toComponent()
				} else {
					ModMenuLang.MECHANICAL_USER_SLOT_RANDOM.toComponent()
				}
			},
			onPress = {
				buttonClicked(MechanicalUserMenu.TOGGLE_UPPER_LEFT_SLOT_ONLY_BUTTON)
			}
		)

		val sneakingButton = ChangingTextButton(
			x = leftPos + 80,
			y = topPos + 140,
			width = 68,
			height = 16,
			messageGetter = {
				if (menu.isSneaking) {
					ModMenuLang.MECHANICAL_USER_SNEAKING_ON.toComponent()
				} else {
					ModMenuLang.MECHANICAL_USER_SNEAKING_OFF.toComponent()
				}
			},
			onPress = {
				buttonClicked(MechanicalUserMenu.TOGGLE_SNEAKING_BUTTON)
			}
		)

		val redstoneModeButton = MultiStageSpriteButton.Builder(font)
			.addStage(
				RedstoneMode.ALWAYS_ON.langKey.toComponent(),
				ALWAYS_ON_SPRITE
			)
			.addStage(
				RedstoneMode.WHILE_POWERED.langKey.toComponent(),
				REDSTONE_ON_SPRITE
			)
			.addStage(
				RedstoneMode.WHILE_UNPOWERED.langKey.toComponent(),
				REDSTONE_OFF_SPRITE
			)
			.addStage(
				RedstoneMode.ON_PULSE.langKey.toComponent(),
				REDSTONE_PULSE_SPRITE
			)
			.location(leftPos + 151, topPos + 24)
			.size(20)
			.currentStageGetter { menu.redstoneMode.ordinal }
			.onPress {
				buttonClicked(BaseMechanicalInteractorMenu.CYCLE_REDSTONE_MODE_BUTTON)
			}
			.build()

		addRenderableWidget(interactionModeButton)
		addRenderableWidget(leftClickButton)
		addRenderableWidget(upperLeftSlotButton)
		addRenderableWidget(sneakingButton)
		addRenderableWidget(redstoneModeButton)
	}

	companion object {
		val ALWAYS_ON_SPRITE = ScreenSprite(
			ExcessiveUtilities.modResource("redstone_mode/always_on"),
			16,
			16
		)
		val REDSTONE_ON_SPRITE = ScreenSprite(
			ExcessiveUtilities.modResource("redstone_mode/redstone_on"),
			16,
			16
		)
		val REDSTONE_OFF_SPRITE = ScreenSprite(
			ExcessiveUtilities.modResource("redstone_mode/redstone_off"),
			16,
			16
		)
		val REDSTONE_PULSE_SPRITE = ScreenSprite(
			ExcessiveUtilities.modResource("redstone_mode/redstone_pulse"),
			16,
			16
		)

		val BACKGROUND = ScreenBackground(
			ExcessiveUtilities.modResource("textures/gui/mechanical_user.png"),
			176,
			240
		)
	}

}