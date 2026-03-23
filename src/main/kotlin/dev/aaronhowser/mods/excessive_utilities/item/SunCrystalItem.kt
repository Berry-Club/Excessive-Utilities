package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.ARGB
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import java.awt.Color

class SunCrystalItem(properties: Properties) : Item(properties) {

	override fun onEntityItemUpdate(stack: ItemStack, entity: ItemEntity): Boolean {
		val level = entity.level()
		if (level.isClientSide) return false

		val charge = stack.getOrDefault(ModDataComponents.CHARGE, 0)
		if (charge >= MAX_CHARGE) return false

		if (!level.isDay) return false
		if (!level.canSeeSky(entity.blockPosition())) return false

		val newCharge = charge + 1
		stack.set(ModDataComponents.CHARGE, newCharge)
		entity.item = stack.copy()

		return false
	}

	override fun getBarColor(stack: ItemStack): Int {
		return Color.YELLOW.rgb
	}

	override fun isBarVisible(stack: ItemStack): Boolean {
		val charge = stack.get(ModDataComponents.CHARGE.get())
		return charge != null && charge > 0 && charge < MAX_CHARGE
	}

	override fun getBarWidth(stack: ItemStack): Int {
		val charge = stack.get(ModDataComponents.CHARGE.get()) ?: return 0

		return (charge / MAX_CHARGE.toFloat() * 13).toInt()
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val charge = stack.getOrDefault(ModDataComponents.CHARGE, 0)
		val percent = (charge / MAX_CHARGE.toFloat() * 100).toInt()
		tooltipComponents.add(Component.literal("Solar power: $percent%"))
	}

	companion object {
		const val MAX_CHARGE = 20 * 30

		val DEFAULT_PROPERTIES: () -> Properties =
			{
				Properties()
					.stacksTo(1)
					.component(ModDataComponents.CHARGE, 0)
			}

		fun getItemColor(stack: ItemStack, tintIndex: Int): Int {
			if (tintIndex != 1) return 0xFFFFFFFF.toInt()

			val charge = stack.getOrDefault(ModDataComponents.CHARGE, 0)

			val percent = charge.toFloat() / MAX_CHARGE.toFloat()

			val startA = 0x3E
			val endA = 0xFF

			val a = (startA + ((endA - startA) * percent)).toInt()
			val r = 0xFF
			val g = 0xFF
			val b = 0xFF

			val argb = ARGB(a, r, g, b)
			return argb.toInt()
		}
	}

}