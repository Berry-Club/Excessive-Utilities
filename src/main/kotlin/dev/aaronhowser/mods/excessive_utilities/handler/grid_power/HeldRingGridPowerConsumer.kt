package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.CuriosApi
import kotlin.jvm.optionals.getOrNull

abstract class HeldRingGridPowerConsumer(
	gpStack: ItemStack,
	player: Player
) : GridPowerContribution.HeldItem(gpStack, player) {

	private val displayStack = gpStack.copy()

	override fun isStillValid(): Boolean {
		if (!player.isAlive || player.isRemoved) return false

		for (slot in 0 until player.inventory.containerSize) {
            if (player.inventory.getItem(slot) === gpStack) return true
        }

		val curiosInventory = CuriosApi.getCuriosInventory(player).getOrNull() ?: return false
		for (handler in curiosInventory.curios.values) {
			val stacks = handler.stacks
			for (slot in 0 until stacks.slots) {
				if (stacks.getStackInSlot(slot) === gpStack) return true
			}
		}

		return false
	}

	override fun getDisplayStack(): ItemStack = displayStack
	override fun getDisplayName(): Component = displayStack.displayName

	override fun getDisplayText(): Component {
		return Component.literal(getAmount().toString())
	}

}