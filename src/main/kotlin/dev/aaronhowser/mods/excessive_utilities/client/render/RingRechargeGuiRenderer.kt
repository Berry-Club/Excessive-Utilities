package dev.aaronhowser.mods.excessive_utilities.client.render

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.CuriosApi
import kotlin.jvm.optionals.getOrNull

object RingRechargeGuiRenderer {

	val LAYER_NAME: Identifier = ExcessiveUtilities.modResource("ring_recharge_gui_layer")
	private val JUMP_BAR_BACKGROUND: Identifier = ExcessiveUtilities.modResource("hud/jump_bar_background")
	private val JUMP_BAR_PROGRESS: Identifier = ExcessiveUtilities.modResource("hud/jump_bar_progress")

	fun render(guiGraphics: GuiGraphicsExtractor, deltaTracker: DeltaTracker) {
		val player = AaronClientUtil.localPlayer ?: return

		var ringStack = ItemStack.EMPTY

		for (slot in 0 until player.inventory.containerSize) {
			val stack = player.inventory.getItem(slot)
			if (stack.isItem(ModItems.RING_OF_THE_FLYING_SQUID) || stack.isItem(ModItems.CHICKEN_WING_RING)) {
				ringStack = stack
				break
			}
		}

		if (ringStack == ItemStack.EMPTY) {
			val curiosInventory = CuriosApi.getCuriosInventory(player).getOrNull() ?: return
			curioSlots@ for (handler in curiosInventory.curios.values) {
				val stacks = handler.stacks
				for (slot in 0 until stacks.slots) {
					val stack = stacks.getStackInSlot(slot)
					if (stack.isItem(ModItems.RING_OF_THE_FLYING_SQUID) || stack.isItem(ModItems.CHICKEN_WING_RING)) {
						ringStack = stack
						break@curioSlots
					}
				}
			}

			if (ringStack == ItemStack.EMPTY) return
		}

		val maxCharge = when {
			ringStack.isItem(ModItems.RING_OF_THE_FLYING_SQUID) -> ServerConfig.CONFIG.flyingSquidRingDurationTicks.get()
			ringStack.isItem(ModItems.CHICKEN_WING_RING) -> ServerConfig.CONFIG.chickenWingRingDurationTicks.get()
			else -> return
		}

		val charge = ringStack.getOrDefault(ModDataComponents.CHARGE, 0)
		if (charge >= maxCharge) return

		val left = guiGraphics.guiWidth() / 2 - 91
		val top = guiGraphics.guiHeight() - 49
		val progressWidth = (charge.toFloat() / maxCharge * 183).toInt()

		guiGraphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			JUMP_BAR_BACKGROUND,
			left,
			top,
			182,
			5
		)

		if (progressWidth <= 0) return

		guiGraphics.blitSprite(
			RenderPipelines.GUI_TEXTURED,
			JUMP_BAR_PROGRESS,
			182,
			5,
			0,
			0,
			left,
			top,
			progressWidth,
			5
		)
	}

}