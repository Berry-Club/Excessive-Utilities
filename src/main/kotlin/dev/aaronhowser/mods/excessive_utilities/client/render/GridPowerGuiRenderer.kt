package dev.aaronhowser.mods.excessive_utilities.client.render

import dev.aaronhowser.mods.aaron.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

object GridPowerGuiRenderer {

	val LAYER_NAME: ResourceLocation = ExcessiveUtilities.modResource("grid_power_gui_layer")

	fun renderGridPower(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		val localPlayer = AaronClientUtil.localPlayer ?: return
		if (!localPlayer.isHolding { it.isItem(ModItemTagsProvider.RENDER_GP_WHILE_HOLDING) }) return

	}

}