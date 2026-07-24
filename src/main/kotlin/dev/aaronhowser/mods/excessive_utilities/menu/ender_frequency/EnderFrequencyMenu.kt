package dev.aaronhowser.mods.excessive_utilities.menu.ender_frequency

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isTrue
import dev.aaronhowser.mods.excessive_utilities.item.EnderFrequencyItem
import dev.aaronhowser.mods.excessive_utilities.item.component.EnderFrequencyComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModMenuTypes
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.ItemStack

class EnderFrequencyMenu(
	containerId: Int,
	private val playerInventory: Inventory,
	val hand: InteractionHand,
	val initialName: String,
	val initiallyPrivate: Boolean
) : AbstractContainerMenu(ModMenuTypes.ENDER_FREQUENCY.get(), containerId) {

	constructor(containerId: Int, inventory: Inventory, hand: InteractionHand) : this(
		containerId,
		inventory,
		hand,
		inventory.player
			.getItemInHand(hand)
			.get(ModDataComponents.ENDER_FREQUENCY)
			?.name
			?: "",
		inventory.player
			.getItemInHand(hand)
			.get(ModDataComponents.ENDER_FREQUENCY)
			?.isPrivate
			.isTrue()
	)

	constructor(containerId: Int, inventory: Inventory, data: RegistryFriendlyByteBuf) : this(
		containerId,
		inventory,
		data.readEnum(InteractionHand::class.java),
		data.readUtf(EnderFrequencyComponent.MAX_NAME_LENGTH),
		data.readBoolean()
	)

	override fun stillValid(player: Player): Boolean {
		return player.getItemInHand(hand).item is EnderFrequencyItem
	}

	override fun quickMoveStack(player: Player, index: Int): ItemStack = ItemStack.EMPTY

}