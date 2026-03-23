package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.ARGB
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.ChatFormatting
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ResultContainer
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent

class UnstableIngotItem(properties: Properties) : Item(properties) {

	override fun inventoryTick(
		stack: ItemStack,
		level: Level,
		entity: Entity,
		slotId: Int,
		isSelected: Boolean
	) {
		if (level.isClientSide) return

		val isStable = !stack.has(ModDataComponents.COUNTDOWN)
		if (isStable) return

		val countdown = stack.get(ModDataComponents.COUNTDOWN) ?: return

		if (isCheesed(stack)) {
			return
		}

		var shouldExplode = countdown <= 0

		if (entity is Player) {
			val currentMenu = try {
				entity.containerMenu.type
			} catch (e: UnsupportedOperationException) {
				null
			}

			val currentMenuId = if (currentMenu != null) BuiltInRegistries.MENU.getKey(currentMenu) else null

			val oldMenu = stack.get(ModDataComponents.CRAFTED_IN_MENU)
			if (currentMenuId != oldMenu) {
				shouldExplode = true
			}
		}

		val requiredMenu = stack.get(ModDataComponents.CRAFTED_IN_MENU)
		if (requiredMenu == null) {
			shouldExplode = false
		}

		if (shouldExplode) {
			//TODO: Explode
			stack.count = 0
		} else {
			stack.set(ModDataComponents.COUNTDOWN, countdown - 1)
		}
	}

	override fun getName(stack: ItemStack): Component {
		if (stack.has(ModDataComponents.COUNTDOWN)) return super.getName(stack)
		return ModItemLang.MOBIUS_INGOT.toComponent()
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val countdown = stack.get(ModDataComponents.COUNTDOWN)
		if (countdown != null) {
			val seconds = countdown / 20f
			tooltipComponents.add(Component.literal("Time until explosion: %.1f seconds".format(seconds)))
		}
	}

	companion object {
		const val MAX_COUNTDOWN = 20 * 10

		fun getColor(stack: ItemStack, tintIndex: Int): Int {
			val countdown = stack.get(ModDataComponents.COUNTDOWN) ?: return 0xFFFFFFFF.toInt()
			val percentToExplosion = (1f - countdown) / MAX_COUNTDOWN

			val alpha = 255
			val red = 255
			val green = Mth.lerp(percentToExplosion, 255f, 0f).toInt()
			val blue = Mth.lerp(percentToExplosion, 255f, 0f).toInt()

			val argb = ARGB(alpha, red, green, blue)
			return argb.toInt()
		}

		fun isCheesed(stack: ItemStack): Boolean {
			return stack.has(ModDataComponents.COUNTDOWN) && !stack.has(ModDataComponents.CRAFTED_IN_MENU)
		}

		fun handleCraftEvent(event: PlayerEvent.ItemCraftedEvent) {
			val stack = event.crafting
			if (!stack.isItem(ModItems.UNSTABLE_INGOT)) return

			val player = event.entity
			val currentMenu = try {
				player.containerMenu.type
			} catch (e: UnsupportedOperationException) {
				null
			}

			val currentMenuId = if (currentMenu != null) BuiltInRegistries.MENU.getKey(currentMenu) else null

			if (currentMenuId != null) {
				stack.set(ModDataComponents.CRAFTED_IN_MENU, currentMenuId)
			}
		}

		fun handleTooltip(event: ItemTooltipEvent) {
			val stack = event.itemStack
			if (!stack.isItem(ModItems.UNSTABLE_INGOT)) return

			if (isCheesed(stack)) {
				val localPlayer = event.entity ?: return
				val menu = localPlayer.containerMenu

				var isInCraftingOutputSlot = false
				var isInAnySlot = false

				for (slot in menu.slots) {
					val isResultContainer = slot.container is ResultContainer

					if (slot.item === stack) {
						if (isResultContainer) isInCraftingOutputSlot = true
						isInAnySlot = true
						break
					}
				}

				if (!isInAnySlot || isInCraftingOutputSlot) return

				event.toolTip += ModMenuLang.UNSTABLE_INGOT_CHEESED_1.toComponent().withStyle(ChatFormatting.RED)
				event.toolTip += ModMenuLang.UNSTABLE_INGOT_CHEESED_2.toComponent().withStyle(ChatFormatting.RED)
				event.toolTip += ModMenuLang.UNSTABLE_INGOT_CHEESED_3.toComponent().withStyle(ChatFormatting.RED)
			}
		}
	}

}