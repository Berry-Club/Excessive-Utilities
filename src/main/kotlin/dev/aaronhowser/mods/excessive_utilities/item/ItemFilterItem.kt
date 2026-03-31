package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isNotEmpty
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.item.component.ItemFilterComponent
import dev.aaronhowser.mods.excessive_utilities.menu.item_filter_menu.ItemFilterMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.ChatFormatting
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuConstructor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
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

			val provider = SimpleMenuProvider(menuConstructor, usedStack.hoverName)
			player.openMenu(provider) { data -> data.writeEnum(usedHand) }
		}

		return InteractionResultHolder.sidedSuccess(usedStack, level.isClientSide)
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val flagComponent = getFilterComponent(stack)
		for (flag in flagComponent.flags) {
			val component = flag.getMessage(true).withStyle(ChatFormatting.BLUE)
			tooltipComponents.add(component)
		}

		for (y in 0 until 4) {
			val stacks = mutableListOf<ItemStack>()

			for (x in 0 until 4) {
				val slot = y * 4 + x
				val ghostStack = getStack(stack, slot)

				if (ghostStack.isNotEmpty()) {
					stacks.add(ghostStack)
				}
			}

			if (stacks.isEmpty()) continue

			val component = Component.empty()
			for ((i, stack) in stacks.withIndex()) {
				component.append(stack.hoverName)

				if (i < stacks.size - 1) {
					component.append(Component.literal(", "))
				}
			}

			tooltipComponents.add(component)
		}

	}

	companion object {
		fun getFilterComponent(filterStack: ItemStack): ItemFilterComponent {
			return filterStack.getOrDefault(ModDataComponents.ITEM_FILTER, ItemFilterComponent())
		}

		fun setFlags(filterStack: ItemStack, flags: List<ItemFilterComponent.Flag>) {
			val currentComponent = getFilterComponent(filterStack)
			val newComponent = currentComponent.withFlags(flags)
			filterStack.set(ModDataComponents.ITEM_FILTER, newComponent)
		}

		fun getStack(filterStack: ItemStack, slot: Int): ItemStack {
			val component = getFilterComponent(filterStack)
			return component.getItem(slot)
		}

		fun setStack(filterStack: ItemStack, slot: Int, stackToPlace: ItemStack): Boolean {
			if (slot !in 0 until ItemFilterComponent.CONTAINER_SIZE) return false

			val currentComponent = getFilterComponent(filterStack)
			val newComponent = currentComponent.getWithSetItem(slot, stackToPlace) ?: return false

			filterStack.set(ModDataComponents.ITEM_FILTER, newComponent)
			return true
		}

		fun getFilterItems(filterStack: ItemStack): NonNullList<ItemStack> {
			val component = getFilterComponent(filterStack)
			return component.getItems()
		}

		fun filterItems(filterStack: ItemStack, items: List<ItemStack>): List<ItemStack> {
			return items.filter { passesFilter(filterStack, it) }
		}

		fun passesFilter(filterStack: ItemStack, checkedStack: ItemStack): Boolean {
			if (!filterStack.isItem(ModItems.ITEM_FILTER)) return false

			val filterComponent = getFilterComponent(filterStack)
			return filterComponent.passesFilter(checkedStack)
		}


	}

}