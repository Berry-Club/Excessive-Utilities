package dev.aaronhowser.mods.excessive_utilities.client.render

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

object RingRechargeGuiRenderer {

	val LAYER_NAME: ResourceLocation = ExcessiveUtilities.modResource("ring_recharge_gui_layer")

	//TODO: Render with horse jump bar or whatever instead
	fun render(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		val player = AaronClientUtil.localPlayer ?: return

		val ringStack = player
			.inventory
			.compartments
			.flatMap { it.toList() }
			.firstOrNull { it.isItem(ModItems.RING_OF_THE_FLYING_SQUID) || it.isItem(ModItems.CHICKEN_WING_RING) }
			?: return

		val maxCharge = when {
			ringStack.isItem(ModItems.RING_OF_THE_FLYING_SQUID) -> ServerConfig.CONFIG.flyingSquidRingDurationTicks.get()
			ringStack.isItem(ModItems.CHICKEN_WING_RING) -> ServerConfig.CONFIG.chickenWingRingDurationTicks.get()
			else -> return
		}

		val charge = ringStack.getOrDefault(ModDataComponents.CHARGE, 0)

		if (charge >= maxCharge) return

		val component = Component.literal("$charge / $maxCharge")

		guiGraphics.drawString(
			Minecraft.getInstance().font,
			component,
			guiGraphics.guiWidth() / 2 + 5,
			guiGraphics.guiHeight() / 2 + 5,
			0xFFFFFF
		)
	}

}