package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import top.theillusivec4.curios.api.CuriosApi
import kotlin.jvm.optionals.getOrNull

abstract class HeldRingGridPowerContribution(
	gpStack: ItemStack,
	player: Player
) : GridPowerContribution.HeldItem(gpStack, player) {

	private val displayStack = gpStack.copy()

	override fun isStillValid(): Boolean {
		if (!player.isAlive || player.isRemoved) return false

		for (compartment in player.inventory.compartments) {
			for (stack in compartment) {
				if (stack === gpStack) return true
			}
		}

		val curiosInventory = CuriosApi.getCuriosInventory(player).getOrNull() ?: return false
		val wornCurios = curiosInventory.equippedCurios
		for (slot in 0 until wornCurios.slots) {
			if (wornCurios.getStackInSlot(slot) === gpStack) return true
		}

		return false
	}

	override fun getDisplayStack(): ItemStack = displayStack
	override fun getDisplayName(): Component = displayStack.displayName

	override fun getDisplayText(): Component {
		return Component.literal(getAmount().toString())
	}

}