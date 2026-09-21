package dev.aaronhowser.mods.excessive_utilities.menu.ender_frequency

import dev.aaronhowser.mods.aaron.menu.HeldItemMenu
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isTrue
import dev.aaronhowser.mods.excessive_utilities.item.EnderFrequencyItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.item.ItemStack

class EnderFrequencyMenu(
	containerId: Int,
	playerInventory: Inventory,
	val hand: InteractionHand
) : HeldItemMenu(ModMenuTypes.ENDER_FREQUENCY.get(), containerId, playerInventory, hand) {

	private val initialFrequency = playerInventory.player
		.getItemInHand(hand)
		.get(ModDataComponents.ENDER_FREQUENCY)

	val initialName: String = initialFrequency?.name ?: ""
	val initiallyPrivate: Boolean = initialFrequency?.isPrivate.isTrue()

	override fun isValidHeldItem(heldItem: ItemStack): Boolean {
		return heldItem.item is EnderFrequencyItem
	}

	companion object {
		fun fromNetwork(
			containerId: Int,
			playerInventory: Inventory,
			data: RegistryFriendlyByteBuf
		): EnderFrequencyMenu {
			val hand = data.readEnum(InteractionHand::class.java)
			return EnderFrequencyMenu(containerId, playerInventory, hand)
		}
	}

}