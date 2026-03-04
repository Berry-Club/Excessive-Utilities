package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerHandler
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class PowerManagerItem(properties: Properties) : Item(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResultHolder<ItemStack> {
		val stack = player.getItemInHand(usedHand)

		if (level is ServerLevel) {
			val grid = GridPowerHandler.get(level).getGrid(player)
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties =
			Properties().stacksTo(1)
	}

}