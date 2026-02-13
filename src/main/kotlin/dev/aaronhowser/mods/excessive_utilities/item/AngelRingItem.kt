package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerContribution
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerHandler
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.ai.attributes.AttributeModifier
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.NeoForgeMod

class AngelRingItem(properties: Properties) : Item(properties) {

	override fun inventoryTick(
		stack: ItemStack,
		level: Level,
		entity: Entity,
		slotId: Int,
		isSelected: Boolean
	) {

		if (entity is ServerPlayer) {
			addGpConsumer(entity, stack)
			handleAttributeModifier(entity)
		}

	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)
		val ATTRIBUTE_MODIFIER_NAME = ExcessiveUtilities.modResource("angel_ring_flight")
		val ATTRIBUTE_MODIFIER = AttributeModifier(ATTRIBUTE_MODIFIER_NAME, 1.0, AttributeModifier.Operation.ADD_VALUE)

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

					if (!hasStack) {
						val attribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT)
						if (attribute != null && attribute.hasModifier(ATTRIBUTE_MODIFIER_NAME)) {
							attribute.removeModifier(ATTRIBUTE_MODIFIER_NAME)
							player.abilities.flying = false
							player.onUpdateAbilities()
						}
					}

					return hasStack
				}

				override fun getAmount(): Double {
					if (player.hasInfiniteMaterials() || !player.abilities.flying) return 0.0

					return ServerConfig.CONFIG.angelRingGpCost.get()
				}
			}

			handler.addConsumer(new)

			return new
		}

		fun handleAttributeModifier(player: ServerPlayer) {
			val flightAttribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT) ?: return

			val handler = GridPowerHandler.get(player.serverLevel()).getGrid(player)
			if (!handler.isOverloaded()) {
				if (!flightAttribute.hasModifier(ATTRIBUTE_MODIFIER_NAME)) {
					flightAttribute.addTransientModifier(ATTRIBUTE_MODIFIER)
				}
			} else {
				if (flightAttribute.hasModifier(ATTRIBUTE_MODIFIER_NAME)) {
					flightAttribute.removeModifier(ATTRIBUTE_MODIFIER_NAME)
				}
			}
		}

	}

}