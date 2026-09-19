package dev.aaronhowser.mods.excessive_utilities.menu.mechanical_miner

import dev.aaronhowser.mods.aaron.menu.components.MultiStageSpriteButton
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.MechanicalInteractorBlockEntity.RedstoneMode
import dev.aaronhowser.mods.excessive_utilities.menu.ExcessiveUtilitiesScreen
import dev.aaronhowser.mods.excessive_utilities.menu.mechanical_interactor.BaseMechanicalInteractorMenu
import dev.aaronhowser.mods.excessive_utilities.menu.mechanical_user.MechanicalUserScreen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory

class MechanicalMinerScreen(
	menu: MechanicalMinerMenu,
	playerInventory: Inventory,
	title: Component
) : ExcessiveUtilitiesScreen<MechanicalMinerMenu>(menu, playerInventory, title) {

	override val background: ScreenBackground = BACKGROUND
	override val titleLabelSprite: ResourceLocation = RED_LABEL

	override fun baseInit() {
		super.baseInit()

		val redstoneModeButton = MultiStageSpriteButton.Builder(font)
			.addStage(
				RedstoneMode.ALWAYS_ON.langKey.toComponent(),
				MechanicalUserScreen.ALWAYS_ON_SPRITE
			)
			.addStage(
				RedstoneMode.WHILE_POWERED.langKey.toComponent(),
				MechanicalUserScreen.REDSTONE_ON_SPRITE
			)
			.addStage(
				RedstoneMode.WHILE_UNPOWERED.langKey.toComponent(),
				MechanicalUserScreen.REDSTONE_OFF_SPRITE
			)
			.addStage(
				RedstoneMode.ON_PULSE.langKey.toComponent(),
				MechanicalUserScreen.REDSTONE_PULSE_SPRITE
			)
			.location(leftPos + 78, topPos + 119)
			.size(20)
			.currentStageGetter { menu.redstoneMode.ordinal }
			.onPress {
				buttonClicked(BaseMechanicalInteractorMenu.CYCLE_REDSTONE_MODE_BUTTON)
			}
			.build()

		addRenderableWidget(redstoneModeButton)
	}

	companion object {
		val BACKGROUND = ScreenBackground(
			ExcessiveUtilities.modResource("textures/gui/mechanical_miner.png"),
			176,
			240
		)
	}

}