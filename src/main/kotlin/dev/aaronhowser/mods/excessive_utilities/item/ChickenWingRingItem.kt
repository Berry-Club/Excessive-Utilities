package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.ChickenWingRingGridPowerContribution
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
import top.theillusivec4.curios.api.SlotContext
import top.theillusivec4.curios.api.type.capability.ICurioItem

// Lasts 10 seconds, recharges in 5 seconds
class ChickenWingRingItem(properties: Properties) : Item(properties), ICurioItem {

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
		tick(entity, stack)
	}

	override fun curioTick(slotContext: SlotContext, stack: ItemStack) {
		tick(slotContext.entity(), stack)
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
					&& player.deltaMovement.y < 0
		}

		private fun tick(entity: Entity, stack: ItemStack) {
			if (entity is ServerPlayer) {
				addGpConsumer(entity, stack)
			}

			if (entity is Player && canPlayerUse(entity)) {
				val movement = entity.deltaMovement
				val dy = movement.y
				if (dy >= 0) return

				val drainSuccessful = drainCharge(stack)
				if (!drainSuccessful) return

				val maxFallSpeed = ServerConfig.CONFIG.chickenWingRingFallSpeed.get()
				val newDy = maxOf(dy, -maxFallSpeed)

				entity.deltaMovement = Vec3(movement.x, newDy, movement.z)
				entity.resetFallDistance()
			} else {
				recharge(stack)
			}
		}

		private fun drainCharge(stack: ItemStack): Boolean {
			val charge = stack.getOrDefault(ModDataComponents.CHARGE.get(), 0)
			if (charge <= 0) return false

			stack.set(ModDataComponents.CHARGE.get(), charge - 1)
			return true
		}

		private fun recharge(stack: ItemStack) {
			val maxCharge = ServerConfig.CONFIG.chickenWingRingDurationTicks.get()
			val currentCharge = stack.getOrDefault(ModDataComponents.CHARGE.get(), 0)
			if (currentCharge >= maxCharge) return

			val rechargeTime = ServerConfig.CONFIG.chickenWingRingRechargeTicks.get()
			val chargePerTick = maxCharge.toDouble() / rechargeTime

			val newCharge = (currentCharge + chargePerTick).toInt().coerceAtMost(maxCharge)
			stack.set(ModDataComponents.CHARGE.get(), newCharge)
		}

		private fun addGpConsumer(player: ServerPlayer, ringStack: ItemStack): GridPowerContribution.HeldItem {
			val handler = GridPowerHandler.get(player.serverLevel()).getPlayerGrid(player)

			val currentConsumers = handler.getConsumers()
			val existing = currentConsumers
				.filterIsInstance<GridPowerContribution.HeldItem>()
				.firstOrNull { ItemStack.isSameItemSameComponents(it.gpStack, ringStack) }

			if (existing != null) {
				return existing
			}

			val new = ChickenWingRingGridPowerContribution(ringStack, player)

			handler.addConsumer(new)

			return new
		}

	}

}