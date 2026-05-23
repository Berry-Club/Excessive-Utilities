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
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.item.ItemEntity
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

		val countdown = stack.get(ModDataComponents.COUNTDOWN) ?: return
		if (entity !is Player) return

		val currentMenuId = getCurrentMenuId(entity)

		if (!stack.has(ModDataComponents.CRAFTED_IN_MENU) && currentMenuId == VANILLA_CRAFTING_MENU_ID) {
			stack.set(ModDataComponents.CRAFTED_IN_MENU, currentMenuId)
		}

		val requiredMenu = stack.get(ModDataComponents.CRAFTED_IN_MENU) ?: return

		val shouldExplode = countdown <= 0 || currentMenuId != requiredMenu
		if (shouldExplode) {
			explode(level, entity, stack)
		} else {
			stack.set(ModDataComponents.COUNTDOWN, countdown - 1)
		}
	}

	override fun onEntityItemUpdate(stack: ItemStack, entity: ItemEntity): Boolean {
		val level = entity.level()
		if (level.isClientSide) return false
		if (!stack.has(ModDataComponents.COUNTDOWN)) return false
		if (!stack.has(ModDataComponents.CRAFTED_IN_MENU)) return false

		explode(level, entity, stack)
		entity.discard()
		return true
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
			val formattedSeconds = "%.1f".format(seconds)
			tooltipComponents += ModMenuLang.SECONDS_UNTIL_EXPLOSION.toComponent(formattedSeconds).withStyle(ChatFormatting.RED)
		}
	}

	companion object {
		const val MAX_COUNTDOWN = 20 * 10
		const val EXPLOSION_RADIUS = 2.0f

		val VANILLA_CRAFTING_MENU_ID: ResourceLocation = ResourceLocation.withDefaultNamespace("crafting")

		private fun getCurrentMenuId(player: Player): ResourceLocation? {
			val menuType = try {
				player.containerMenu.type
			} catch (e: UnsupportedOperationException) {
				return null
			}
			return BuiltInRegistries.MENU.getKey(menuType)
		}

		private fun explode(level: Level, entity: Entity, stack: ItemStack) {
			stack.count = 0
			level.explode(
				entity,
				entity.x, entity.y, entity.z,
				EXPLOSION_RADIUS,
				Level.ExplosionInteraction.MOB
			)
		}

		fun getColor(stack: ItemStack, tintIndex: Int): Int {
			val countdown = stack.get(ModDataComponents.COUNTDOWN) ?: return 0xFFFFFFFF.toInt()
			val percentToExplosion = ((MAX_COUNTDOWN - countdown).toFloat() / MAX_COUNTDOWN).coerceIn(0f, 1f)

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