package dev.aaronhowser.mods.excessive_utilities.handler.grid_power

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isTrue
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.item.AngelRingItem
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.neoforged.neoforge.common.NeoForgeMod
import top.theillusivec4.curios.api.CuriosApi
import kotlin.jvm.optionals.getOrNull

class AngelRingGridPowerContribution(
	ringStack: ItemStack,
	player: ServerPlayer
) : GridPowerContribution.HeldItem(ringStack, player) {

	private val displayStack = ringStack.copy()

	override fun isStillValid(): Boolean {
		if (!player.isAlive || player.isRemoved) return false

		val curiosInventory = CuriosApi.getCuriosInventory(player).getOrNull()
		val stillHasRing = curiosInventory?.isEquipped(ModItems.ANGEL_RING.get()).isTrue()
		if (!stillHasRing) {
			removeFlight(player)
		}

		return stillHasRing
	}

	override fun getAmount(): Double {
		if (player.hasInfiniteMaterials() || !player.abilities.flying) return 0.0

		return ServerConfig.CONFIG.angelRingGpCost.get()
	}

	override fun getDisplayStack(): ItemStack = displayStack
	override fun getDisplayName(): Component = displayStack.displayName

	override fun getDisplayText(): Component {
		return Component.literal(getAmount().toString())
	}

	private fun removeFlight(player: Player) {
		val attribute = player.getAttribute(NeoForgeMod.CREATIVE_FLIGHT) ?: return
		if (!attribute.hasModifier(AngelRingItem.ATTRIBUTE_MODIFIER_NAME)) return

		attribute.removeModifier(AngelRingItem.ATTRIBUTE_MODIFIER_NAME)
		player.abilities.flying = false
		player.onUpdateAbilities()
	}

}