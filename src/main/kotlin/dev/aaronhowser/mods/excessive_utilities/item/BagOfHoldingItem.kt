package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isItem
import dev.aaronhowser.mods.excessive_utilities.handler.bag_of_holding.BagOfHoldingHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.SimpleMenuProvider
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.AbstractContainerMenu
import net.minecraft.world.inventory.ChestMenu
import net.minecraft.world.inventory.MenuType
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.ItemUtils
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import java.util.*

class BagOfHoldingItem(properties: Properties) : Item(properties) {

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack?> {
		val usedStack = player.getItemInHand(usedHand)

		if (level is ServerLevel) {
			var bagId = usedStack.get(ModDataComponents.BAG_OF_HOLDING_ID)

			if (bagId == null) {
				bagId = UUID.randomUUID()
				usedStack.set(ModDataComponents.BAG_OF_HOLDING_ID, bagId)
			}

			val menuProvider = SimpleMenuProvider(
				{ containerId, playerInventory, _ ->
					createMenu(containerId, playerInventory, bagId!!, usedHand)
				},
				usedStack.hoverName
			)

			player.openMenu(menuProvider)
		}

		return InteractionResultHolder.sidedSuccess(usedStack, level.isClientSide)
	}

	override fun onDestroyed(itemEntity: ItemEntity, damageSource: DamageSource) {
		val level = itemEntity.level()
		if (level is ServerLevel) {
			val bagId = itemEntity.item.get(ModDataComponents.BAG_OF_HOLDING_ID) ?: return
			val items = BagOfHoldingHandler.get(level).removeBag(bagId)
			ItemUtils.onContainerDestroyed(itemEntity, items)
		}
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		if (!tooltipFlag.hasShiftDown()) return

		val bagId = stack.get(ModDataComponents.BAG_OF_HOLDING_ID)
		if (bagId != null) {
			tooltipComponents.add(Component.literal(bagId.toString()))
		}
	}

	private fun createMenu(
		containerId: Int,
		playerInventory: Inventory,
		bagId: UUID,
		usedHand: InteractionHand
	): AbstractContainerMenu {
		val level = playerInventory.player.level() as ServerLevel
		val bag = BagOfHoldingHandler.get(level).getOrCreateBag(bagId)

		return object : ChestMenu(MenuType.GENERIC_9x6, containerId, playerInventory, bag.container, 6) {
			override fun stillValid(player: Player): Boolean {
				val heldStack = player.getItemInHand(usedHand)
				return bag.isActive &&
						heldStack.isItem(this@BagOfHoldingItem) &&
						heldStack.get(ModDataComponents.BAG_OF_HOLDING_ID) == bagId
			}
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
	}

}