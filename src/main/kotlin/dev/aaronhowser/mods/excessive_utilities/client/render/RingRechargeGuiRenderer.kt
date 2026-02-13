package dev.aaronhowser.mods.excessive_utilities.client.render

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation

object RingRechargeGuiRenderer {

	val LAYER_NAME: ResourceLocation = ExcessiveUtilities.modResource("ring_recharge_gui_layer")

	fun render(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		val player = AaronClientUtil.localPlayer ?: return

		val angelRingStack = player
			.inventory
			.compartments
			.flatMap { it.toList() }
			.firstOrNull { it.isItem(ModItems.RING_OF_THE_FLYING_SQUID) || it.isItem(ModItems.CHICKEN_WING_RING) }
			?: return


	}

}