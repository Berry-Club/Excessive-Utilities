package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.handler.bag_of_holding_handler.BagOfHoldingHandler
import dev.aaronhowser.mods.excessive_utilities.menu.bag_of_holding.BagOfHoldingMenu
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.MenuProvider
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import java.util.*

class BagOfHoldingItem(properties: Properties) : Item(properties), MenuProvider {

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
		val usedStack = player.getItemInHand(usedHand)

		if (level is ServerLevel) {
			var bagId = usedStack.get(ModDataComponents.BAG_OF_HOLDING_ID)

			if (bagId == null) {
				bagId = UUID.randomUUID()
				usedStack.set(ModDataComponents.BAG_OF_HOLDING_ID, bagId)
			}
		}

		return InteractionResultHolder.sidedSuccess(usedStack, level.isClientSide)
	}

	override fun getDisplayName(): Component = defaultInstance.hoverName

	override fun createMenu(containerId: Int, playerInventory: Inventory, player: Player): AbstractContainerMenu? {
		val level = player.level()
		if (level !is ServerLevel) return null

		val usedStack = player.getItemInHand(player.usedItemHand)
		val bagId = usedStack.get(ModDataComponents.BAG_OF_HOLDING_ID) ?: return null

		val bag = BagOfHoldingHandler.get(level).getBag(bagId)

		return BagOfHoldingMenu(containerId, playerInventory, bag.container)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}