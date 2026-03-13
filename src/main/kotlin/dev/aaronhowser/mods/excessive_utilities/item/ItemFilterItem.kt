package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu.ItemFilterMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.component.DataComponents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuConstructor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class ItemFilterItem(properties: Properties) : Item(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResultHolder<ItemStack?> {
		val usedStack = player.getItemInHand(usedHand)

		if (level.isServerSide) {
			val menuConstructor = MenuConstructor { containerId, playerInventory, player ->
				ItemFilterMenu(containerId, playerInventory, usedHand)
			}

			player.openMenu(SimpleMenuProvider(menuConstructor, usedStack.hoverName))
		}

		return InteractionResultHolder.sidedSuccess(usedStack, level.isClientSide)
	}

	companion object {
		fun setFlags(filterStack: ItemStack, vararg flags: Flag) {
			var flagsInt = 0
			for (flag in flags) {
				flagsInt = flagsInt or flag.bit
			}

			filterStack.set(ModDataComponents.ITEM_FILTER_FLAGS, flagsInt)
		}

		fun getFlags(filterStack: ItemStack): Set<Flag> {
			val flagsInt = filterStack.getOrDefault(ModDataComponents.ITEM_FILTER_FLAGS, 0)

			val flags = mutableSetOf<Flag>()
			for (flag in Flag.entries) {
				if (flagsInt and flag.bit != 0) {
					flags.add(flag)
				}
			}

			return flags
		}

		fun passesFilter(filterStack: ItemStack, checkedStack: ItemStack): Boolean {
			if (!filterStack.isItem(ModItems.ITEM_FILTER)) return false

			val flags = getFlags(filterStack)
			val isInverted = Flag.INVERTED in flags
			if (checkedStack.isEmpty) return isInverted

			val ignoreDamage = Flag.IGNORE_DAMAGE in flags
			val ignoreAllComponents = Flag.IGNORE_ALL_COMPONENTS in flags
			val useTags = Flag.USE_TAGS in flags

			val container = filterStack.get(DataComponents.CONTAINER) ?: return isInverted

			for (slot in 0 until container.slots) {
				val stackInFilter = container.getStackInSlot(slot)
				if (stackInFilter.isEmpty) continue

				// Allow for nested Item Filters
				// If it passes the nested Filter, return true (unless the outer Filter is inverted)
				if (stackInFilter.isItem(ModItems.ITEM_FILTER)) {
					val passesNestedFilter = passesFilter(stackInFilter, checkedStack)
					return passesNestedFilter != isInverted
				}

				// If using tags it ignores item + components and just checks if
				// the stack in the filter has any of the tags of the checked stack
				if (useTags) {
					val tagsMatch = stackInFilter.tags.anyMatch { checkedStack.isItem(it) }
					if (tagsMatch) return !isInverted
				}

				// At this point it can only pass it's the same item
				if (!stackInFilter.isItem(checkedStack.item)) continue

				// Everything after this is just comparing components
				// If we're ignoring components, none of that matters
				if (ignoreAllComponents) {
					return isInverted
				}

				// If we're ignoring damage, remove the damage component and compare the rest
				if (ignoreDamage) {
					val leftWithoutDamage = stackInFilter.copy().apply { remove(DataComponents.DAMAGE) }
					val rightWithoutDamage = checkedStack.copy().apply { remove(DataComponents.DAMAGE) }
					val matchesNonDamageComponents = ItemStack.isSameItemSameComponents(leftWithoutDamage, rightWithoutDamage)

					return matchesNonDamageComponents != isInverted
				}

				val matchesAllComponents = ItemStack.isSameItemSameComponents(stackInFilter, checkedStack)
				return matchesAllComponents != isInverted
			}

			// To reach here, checkedStack has to match with none of the stacks in the filter,
			// so return false (or true if inverted)

			return isInverted
		}
	}

	enum class Flag {
		INVERTED,
		USE_TAGS,
		IGNORE_DAMAGE,
		IGNORE_ALL_COMPONENTS,
		;

		val bit: Int = 1 shl ordinal

	}

}