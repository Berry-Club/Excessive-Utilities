package dev.aaronhowser.mods.excessive_utilities.menu.mechanical_miner

import dev.aaronhowser.mods.aaron.menu.BaseScreen
import dev.aaronhowser.mods.aaron.menu.components.MultiStageSpriteButton
import dev.aaronhowser.mods.aaron.menu.textures.ScreenBackground
import dev.aaronhowser.mods.aaron.menu.textures.ScreenSprite
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.block_entity.base.MechanicalInteractorBlockEntity.RedstoneMode
import dev.aaronhowser.mods.excessive_utilities.menu.mechanical_interactor.BaseMechanicalInteractorMenu
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Inventory

class MechanicalMinerScreen(
	menu: MechanicalMinerMenu,
	playerInventory: Inventory,
	title: Component
) : BaseScreen<MechanicalMinerMenu>(menu, playerInventory, title) {
	override val background: ScreenBackground = BACKGROUND

	override fun baseInit() {
		super.baseInit()

		val redstoneModeButton = MultiStageSpriteButton.Builder(font)
			.addStage(
				RedstoneMode.ALWAYS_ON.langKey.toComponent(),
				ScreenSprite(ExcessiveUtilities.modResource("redstone_mode/always_on"), 16, 16)
			)
			.addStage(
				RedstoneMode.WHILE_POWERED.langKey.toComponent(),
				ScreenSprite(ExcessiveUtilities.modResource("redstone_mode/redstone_on"), 16, 16)
			)
			.addStage(
				RedstoneMode.WHILE_UNPOWERED.langKey.toComponent(),
				ScreenSprite(ExcessiveUtilities.modResource("redstone_mode/redstone_off"), 16, 16)
			)
			.addStage(
				RedstoneMode.ON_PULSE.langKey.toComponent(),
				ScreenSprite(ExcessiveUtilities.modResource("redstone_mode/redstone_pulse"), 16, 16)
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