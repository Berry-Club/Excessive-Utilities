package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerContribution
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerHandler
import dev.aaronhowser.mods.excessive_utilities.handler.key_handler.KeyHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

class FlyingSquidRingItem(properties: Properties) : Item(properties) {

	override fun shouldCauseReequipAnimation(
		oldStack: ItemStack,
		newStack: ItemStack,
		slotChanged: Boolean
	): Boolean {
		return slotChanged
	}

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

		if (entity is Player && canPlayerUse(entity)) {
			val charge = stack.getOrDefault(ModDataComponents.CHARGE.get(), 0)
			if (charge <= 0) return

			stack.set(ModDataComponents.CHARGE.get(),  charge - 1)

			val movement = entity.deltaMovement
			val dy = movement.y
			val gravity = entity.gravity
			var newDy = dy + gravity * ServerConfig.CONFIG.flyingSquidRingThrustMultiplier.get()
			newDy = newDy.coerceAtMost(ServerConfig.CONFIG.flyingSquidRingMaxUpwardSpeed.get())

			entity.deltaMovement = Vec3(movement.x, newDy, movement.z)
			entity.resetFallDistance()
		} else {
			val maxCharge = ServerConfig.CONFIG.flyingSquidRingDurationTicks.get()
			val currentCharge = stack.getOrDefault(ModDataComponents.CHARGE.get(), 0)
			if (currentCharge >= maxCharge) return

			val rechargeTime = ServerConfig.CONFIG.flyingSquidRingRechargeTicks.get()
			val chargePerTick = maxCharge.toDouble() / rechargeTime

			val newCharge = (currentCharge + chargePerTick).toInt().coerceAtMost(maxCharge)
			stack.set(ModDataComponents.CHARGE.get(), newCharge)
		}

	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(ModDataComponents.CHARGE.get(), 0)
		}

		private fun canPlayerUse(player: Player): Boolean {
			return KeyHandler.isHoldingSpace(player)
					&& !player.onGround()
					&& !player.isPassenger
					&& !player.abilities.flying
		}

		private fun addGpConsumer(player: ServerPlayer, ringStack: ItemStack): GridPowerContribution.HeldItem {
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
					if (player.hasInfiniteMaterials() || !canPlayerUse(player)) return 0.0

					return ServerConfig.CONFIG.flyingSquidRingGpCost.get()
				}
			}

			handler.addConsumer(new)

			return new
		}

	}

}