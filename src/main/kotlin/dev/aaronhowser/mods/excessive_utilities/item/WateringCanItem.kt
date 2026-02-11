package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isFluid
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.tags.FluidTags
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.ClipContext
import net.minecraft.world.level.Level
import net.minecraft.world.level.material.Fluids
import net.minecraft.world.phys.HitResult
import net.neoforged.neoforge.fluids.FluidStack
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

		if (needsToBeFilled(stack)) {
			return InteractionResultHolder.fail(stack)
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

		val heldWater = stack.get(ModDataComponents.TANK) ?: return true
		return heldWater.amount <= getWaterPerTick()
	}

	private fun isFull(stack: ItemStack): Boolean {
		if (!usesWater()) return true

		val heldWater = stack.get(ModDataComponents.TANK) ?: return false
		return heldWater.amount >= 10_000
	}

	private fun tryCollectWater(player: Player, stack: ItemStack): Boolean {
		if (isFull(stack)) return false
		val level = player.level()

		val blockHitResult = getPlayerPOVHitResult(level, player, ClipContext.Fluid.WATER)
		if (blockHitResult.type != HitResult.Type.BLOCK) return false

		val pos = blockHitResult.blockPos
		if (!level.mayInteract(player, pos)) return false

		if (!level.getFluidState(pos).isFluid(FluidTags.WATER)) return false

		val heldWater = stack.get(ModDataComponents.TANK) ?: return false
		val newAmount = minOf(heldWater.amount + 1000, 10_000)
		val newFluidStack = FluidStack(Fluids.WATER, newAmount)
		stack.set(ModDataComponents.TANK, SimpleFluidContent.copyOf(newFluidStack))

		return true
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(ModDataComponents.TANK, SimpleFluidContent.EMPTY)
		}

		val DEFAULT_REINFORCED_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(ModDataComponents.TANK, SimpleFluidContent.EMPTY)
		}
	}

}