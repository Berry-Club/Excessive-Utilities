package dev.aaronhowser.mods.excessive_utilities.client.render

import dev.aaronhowser.mods.aaron.AaronExtensions.getPovResult
import dev.aaronhowser.mods.aaron.AaronExtensions.isBlock
import dev.aaronhowser.mods.aaron.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModItemTagsProvider
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.ClientGridPower
import net.minecraft.ChatFormatting
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.ai.attributes.Attributes

object GridPowerGuiRenderer {

	val LAYER_NAME: ResourceLocation = ExcessiveUtilities.modResource("grid_power_gui_layer")

	fun renderGridPower(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		val player = AaronClientUtil.localPlayer ?: return

		if (!player.isHolding { it.isItem(ModItemTagsProvider.RENDER_GP_WHILE_HOLDING) }) {
			val lookingAt = player.getPovResult(player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE))
			val shouldRender = player.level()
				.getBlockState(lookingAt.blockPos)
				.isBlock(ModBlockTagsProvider.RENDER_GP_WHILE_LOOKING_AT)

			if (!shouldRender) return
		}

		val component = Component.literal("${ClientGridPower.usage} / ${ClientGridPower.capacity} GP")
		if (ClientGridPower.isOverloaded) {
			component.withStyle(ChatFormatting.RED)
		}

		guiGraphics.drawString(
			Minecraft.getInstance().font,
			component,
			guiGraphics.guiWidth() / 2 + 5,
			guiGraphics.guiHeight() / 2 + 5,
			0xFFFFFF
		)
	}

}