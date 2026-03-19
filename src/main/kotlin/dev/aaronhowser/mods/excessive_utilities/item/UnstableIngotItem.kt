package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

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

		var shouldExplode = countdown <= 0

		if (entity is Player) {
			val currentMenu = try {
				entity.containerMenu.type
			} catch (e: UnsupportedOperationException) {
				null
			}

			val currentMenuId = if (currentMenu != null) BuiltInRegistries.MENU.getKey(currentMenu) else null

			val oldMenu = stack.get(ModDataComponents.CRAFTED_IN_MENU)
			if (oldMenu == null) {
				stack.set(ModDataComponents.CRAFTED_IN_MENU, currentMenuId)
			} else {
				if (currentMenuId != oldMenu) {
					shouldExplode = true
				}
			}
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

			return (alpha shl 24) or (red shl 16) or (green shl 8) or blue
		}

		fun isCheesed(stack: ItemStack): Boolean {
			return stack.has(ModDataComponents.COUNTDOWN) && !stack.has(ModDataComponents.CRAFTED_IN_MENU)
		}
	}

}