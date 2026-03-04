package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerHandler
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

// TODO: Make this a menu instead
class PowerManagerItem(properties: Properties) : Item(properties) {

	override fun use(
		level: Level,
		player: Player,
		usedHand: InteractionHand
	): InteractionResultHolder<ItemStack> {
		val stack = player.getItemInHand(usedHand)

		if (level is ServerLevel) {
			val grid = GridPowerHandler.get(level).getGrid(player)

			val producers = grid.getProducers()
			if (producers.isNotEmpty()) {
				player.tell("Producers:")
				for (producer in producers) {
					val component = Component.literal("- ")
						.append(producer.getDisplayName())
						.append(": ")
						.append(producer.getDisplayText())

					player.tell(component)
				}
			}

			val consumers = grid.getConsumers()
			if (consumers.isNotEmpty()) {
				player.tell("Consumers:")
				for (consumer in consumers) {
					val component = Component.literal("- ")
						.append(consumer.getDisplayName())
						.append(": ")
						.append(consumer.getDisplayText())

					player.tell(component)
				}
			}

			if (producers.isEmpty() && consumers.isEmpty()) {
				player.tell("No producers or consumers found.")
			}

		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide)
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties =
			Properties().stacksTo(1)
	}

}