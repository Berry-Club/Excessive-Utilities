package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.item.component.FluidFilterComponent
import dev.aaronhowser.mods.excessive_utilities.menu.fluid_filter_menu.FluidFilterMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.core.NonNullList
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResult
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.MenuConstructor
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.FluidStack
import net.neoforged.neoforge.transfer.fluid.FluidUtil

class FluidFilterItem(properties: Properties) : Item(properties), TooltipItem {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResult {
		val usedStack = player.getItemInHand(usedHand)

		if (level.isServerSide) {
			val menuConstructor = MenuConstructor { containerId, playerInventory, player ->
				FluidFilterMenu(containerId, playerInventory, usedHand)
			}

			val provider = SimpleMenuProvider(menuConstructor, usedStack.hoverName)
			player.openMenu(provider) { data -> data.writeEnum(usedHand) }
		}

		return InteractionResult.SUCCESS
	}

	override fun appendTooltip(
		stack: ItemStack,
		context: TooltipContext,
		display: TooltipDisplay,
		builder: java.util.function.Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val filterComponent = getFilterComponent(stack)
		for (flag in filterComponent.flags) {
			val component = flag.getMessage(true).withStyle(ChatFormatting.BLUE)
			builder.accept(component)
		}

		val filterStacks = filterComponent.getItems()

		for (y in 0 until 4) {
			val stacks = mutableListOf<FluidStack>()

			for (x in 0 until 4) {
				val slot = y * 4 + x
				val ghostStack = filterStacks.getOrNull(slot) ?: continue
				val fluidStack = FluidUtil.getFirstStackContained(ghostStack)
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

			builder.accept(component)
		}
	}

	companion object {
		fun getFilterComponent(filterStack: ItemStack): FluidFilterComponent {
			return filterStack.get(ModDataComponents.FLUID_FILTER) ?: FluidFilterComponent()
		}

		fun setFlags(filterStack: ItemStack, flags: List<FluidFilterComponent.Flag>) {
			val currentComponent = getFilterComponent(filterStack)
			val newComponent = currentComponent.withFlags(flags)
			filterStack.set(ModDataComponents.FLUID_FILTER, newComponent)
		}

		fun setStack(filterStack: ItemStack, slot: Int, stackToPlace: ItemStack): Boolean {
			if (slot !in 0 until FluidFilterComponent.CONTAINER_SIZE) return false

			val currentComponent = getFilterComponent(filterStack)
			val newComponent = currentComponent.withSetItem(slot, stackToPlace)

			filterStack.set(ModDataComponents.FLUID_FILTER, newComponent)
			return true
		}

		fun getFilterItems(filterStack: ItemStack): NonNullList<ItemStack> {
			val component = getFilterComponent(filterStack)
			return component.getItems()
		}

		fun passesFilter(filterStack: ItemStack, checkedStack: FluidStack): Boolean {
			val component = getFilterComponent(filterStack)
			return component.passesFilter(checkedStack)
		}
	}

}







