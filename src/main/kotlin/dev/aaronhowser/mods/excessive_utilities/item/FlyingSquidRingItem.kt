package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerContribution
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerHandler
import dev.aaronhowser.mods.excessive_utilities.handler.key_handler.KeyHandler
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

// Weak jetpack
// Lasts 15 seconds, recharges in 10 seconds
class FlyingSquidRingItem(properties: Properties) : Item(properties) {

	override fun inventoryTick(
		stack: ItemStack,
		level: Level,
		entity: Entity,
		slotId: Int,
		isSelected: Boolean
	) {

		if (entity is ServerPlayer) {
			addGpConsumer(entity, stack)
		}

		if (entity is Player && KeyHandler.isHoldingSpace(entity)) {
			val movement = entity.deltaMovement
			val dy = movement.y
			val gravity = entity.gravity
			var newDy = dy + gravity * ServerConfig.CONFIG.flyingSquidRingThrustMultiplier.get()
			newDy = minOf(newDy, ServerConfig.CONFIG.flyingSquidRingMaxUpwardSpeed.get())

			entity.deltaMovement = Vec3(movement.x, newDy, movement.z)
			entity.resetFallDistance()
		}

	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)

		fun addGpConsumer(player: ServerPlayer, ringStack: ItemStack): GridPowerContribution.HeldItem {
			val handler = GridPowerHandler.get(player.serverLevel()).getGrid(player)

			val currentConsumers = handler.getConsumers()
			val existing = currentConsumers
				.filterIsInstance<GridPowerContribution.HeldItem>()
				.firstOrNull { ItemStack.isSameItemSameComponents(it.gpStack, ringStack) }

			if (existing != null) {
				return existing
			}

			val new = object : GridPowerContribution.HeldItem(ringStack.copy(), player) {
				override fun isStillValid(): Boolean {
					if (!player.isAlive || player.isRemoved) return false

					val hasStack = player.inventory.contains { stack -> ItemStack.isSameItemSameComponents(stack, gpStack) }

					return hasStack
				}

				override fun getAmount(): Double {
					if (player.hasInfiniteMaterials() || !KeyHandler.isHoldingSpace(player)) return 0.0

					return ServerConfig.CONFIG.flyingSquidRingGpCost.get()
				}
			}

			handler.addConsumer(new)

			return new
		}

	}

}