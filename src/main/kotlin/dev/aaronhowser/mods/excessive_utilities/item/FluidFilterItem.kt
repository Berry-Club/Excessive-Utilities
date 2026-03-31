package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isFluid
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.item.component.FluidFilterFlagsComponent
import dev.aaronhowser.mods.excessive_utilities.menu.fluid_filter_menu.FluidFilterMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.ChatFormatting
import net.minecraft.core.NonNullList
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuConstructor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.ItemContainerContents
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.fluids.FluidUtil

class FluidFilterItem(properties: Properties) : Item(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResultHolder<ItemStack?> {
		val usedStack = player.getItemInHand(usedHand)

		if (level.isServerSide) {
			val menuConstructor = MenuConstructor { containerId, playerInventory, player ->
				FluidFilterMenu(containerId, playerInventory, usedHand)
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
		val flags = getFlagComponent(stack).flagList
		for (flag in flags) {
			val component = flag.getMessage(true).withStyle(ChatFormatting.BLUE)
			tooltipComponents.add(component)
		}

		for (y in 0 until 4) {
			val stacks = mutableListOf<FluidStack>()

			for (x in 0 until 4) {
				val slot = y * 4 + x
				val ghostStack = getGhostStack(stack, slot)
				val fluidStack = FluidUtil.getFluidContained(ghostStack).orElse(FluidStack.EMPTY)
				if (!fluidStack.isEmpty) {
					stacks.add(fluidStack)
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
		const val CONTAINER_SIZE = 16

		fun setFlags(filterStack: ItemStack, flags: List<FluidFilterFlagsComponent.Flag>) {
			if (flags.isEmpty()) {
				filterStack.remove(ModDataComponents.FLUID_FILTER_FLAGS)
				return
			}

			val flagComponent = FluidFilterFlagsComponent(flags)
			filterStack.set(ModDataComponents.FLUID_FILTER_FLAGS, flagComponent)
		}

		fun getFlagComponent(filterStack: ItemStack): FluidFilterFlagsComponent {
			return filterStack.get(ModDataComponents.FLUID_FILTER_FLAGS) ?: FluidFilterFlagsComponent()
		}

		fun getGhostStack(filterStack: ItemStack, slot: Int): ItemStack {
			if (!filterStack.isItem(ModItems.FLUID_FILTER)) return ItemStack.EMPTY
			if (slot !in 0 until CONTAINER_SIZE) return ItemStack.EMPTY

			val filterItems = getFilterItems(filterStack)
			return filterItems[slot]
		}

		fun placeGhostInSlot(filterStack: ItemStack, slot: Int, stackToPlace: ItemStack) {
			if (!filterStack.isItem(ModItems.FLUID_FILTER)) return
			if (slot !in 0 until CONTAINER_SIZE) return

			val filterItems = getFilterItems(filterStack)
			filterItems[slot] = stackToPlace.copyWithCount(1)

			val isEmpty = filterItems.all(ItemStack::isEmpty)
			if (isEmpty) {
				filterStack.remove(DataComponents.CONTAINER)
			} else {
				val newComponent = ItemContainerContents.fromItems(filterItems)
				filterStack.set(DataComponents.CONTAINER, newComponent)
			}
		}

		fun getFilterItems(filterStack: ItemStack): NonNullList<ItemStack> {
			val list = NonNullList.withSize(CONTAINER_SIZE, ItemStack.EMPTY)
			val container = filterStack.get(DataComponents.CONTAINER) ?: return list
			container.copyInto(list)
			return list
		}

		fun passesFilter(filterStack: ItemStack, checkedStack: FluidStack): Boolean {
			if (!filterStack.isItem(ModItems.ITEM_FILTER)) return false

			val flags = getFlagComponent(filterStack)

			val isInverted = flags.isInverted
			val ignoreAllComponents = flags.ignoreAllComponents
			val useTags = flags.useTags

			if (checkedStack.isEmpty) return isInverted

			val container = filterStack.get(DataComponents.CONTAINER) ?: return isInverted

			for (slot in 0 until container.slots) {
				val stackInFilter = container.getStackInSlot(slot)
				if (stackInFilter.isEmpty) continue

				if (stackInFilter.isItem(ModItems.FLUID_FILTER)) {
					val passesNestedFilter = passesFilter(stackInFilter, checkedStack)
					return passesNestedFilter != isInverted
				}

				val filterFluid = FluidUtil.getFluidContained(stackInFilter).orElse(FluidStack.EMPTY)
				if (filterFluid.isEmpty) continue

				if (useTags) {
					val tagsMatch = filterFluid.tags.anyMatch { checkedStack.isFluid(it) }
					if (tagsMatch) return !isInverted
				}

				if (ignoreAllComponents) {
					val sameFluid = FluidStack.isSameFluid(filterFluid, checkedStack)
					if (sameFluid) return !isInverted
				}

				val sameWithComponent = FluidStack.isSameFluidSameComponents(filterFluid, checkedStack)
				if (sameWithComponent) return !isInverted
			}

			// To reach here, checkedStack has to match with none of the stacks in the filter,
			// so return false (or true if inverted)

			return isInverted
		}
	}

}