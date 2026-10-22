package dev.aaronhowser.mods.excessive_utilities.client.render

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.client.DeltaTracker
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphicsExtractor
import net.minecraft.network.chat.Component
import net.minecraft.resources.Identifier
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.CuriosApi
import kotlin.jvm.optionals.getOrNull

object RingRechargeGuiRenderer {

	val LAYER_NAME: Identifier = ExcessiveUtilities.modResource("ring_recharge_gui_layer")

	//TODO: Render with horse jump bar or whatever instead
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

		val component = Component.literal("$charge / $maxCharge")

		guiGraphics.text(
			Minecraft.getInstance().font,
			component,
			guiGraphics.guiWidth() / 2 + 5,
			guiGraphics.guiHeight() / 2 + 5,
			0xFFFFFFFF.toInt()
		)
	}

}
