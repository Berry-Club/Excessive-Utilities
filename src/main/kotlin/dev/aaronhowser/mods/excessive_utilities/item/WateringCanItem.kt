package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import net.neoforged.neoforge.fluids.SimpleFluidContent

class WateringCanItem(
	val isReinforced: Boolean,
	properties: Properties
) : Item(properties) {

	override fun getUseDuration(stack: ItemStack, entity: LivingEntity): Int = Int.MAX_VALUE
	override fun getUseAnimation(stack: ItemStack): UseAnim = UseAnim.BOW

	override fun use(level: Level, player: Player, usedHand: InteractionHand): InteractionResultHolder<ItemStack> {
		val stack = player.getItemInHand(usedHand)

		if (tryCollectWater(player, stack)) {
			return InteractionResultHolder.consume(stack)
		}

		player.startUsingItem(usedHand)

		return InteractionResultHolder.consume(stack)
	}

	private fun getWaterPerTick(): Int {
		return if (isReinforced) {
			ServerConfig.CONFIG.reinforcedWateringCanWaterUsagePerTick.get()
		} else {
			ServerConfig.CONFIG.wateringCanWaterUsagePerTick.get()
		}
	}

	private fun usesWater(): Boolean = getWaterPerTick() > 0

	private fun needsToBeFilled(stack: ItemStack): Boolean {
		if (!usesWater()) return false

		val heldWater = stack.get(ModDataComponents.WATER) ?: return true
		return heldWater.amount <= getWaterPerTick()
	}

	private fun tryCollectWater(player: Player, stack: ItemStack): Boolean {
		if (!needsToBeFilled(stack)) return false

		return true
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(ModDataComponents.WATER, SimpleFluidContent.EMPTY)
		}

		val DEFAULT_REINFORCED_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(ModDataComponents.WATER, SimpleFluidContent.EMPTY)
		}
	}

}