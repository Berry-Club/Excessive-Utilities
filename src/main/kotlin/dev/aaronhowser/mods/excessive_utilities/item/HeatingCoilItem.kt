package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.ARGB
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.transfer.access.ItemAccess
import net.neoforged.neoforge.transfer.energy.EnergyHandler
import net.neoforged.neoforge.transfer.energy.ItemAccessEnergyHandler

class HeatingCoilItem(properties: Properties) : Item(properties), TooltipItem {

	fun getBurnTime(itemStack: ItemStack, recipeType: RecipeType<*>?): Int {
		if (recipeType != RecipeType.SMELTING) {
			return 0
		}

		val requiredEnergy =
			ServerConfig.CONFIG.heatingCoilBurnCost.get()

		val availableEnergy = ItemAccess.forStack(itemStack).getCapability(Capabilities.Energy.ITEM)?.amountAsInt ?: 0

		return if (availableEnergy >= requiredEnergy) {
			ServerConfig.CONFIG.heatingCoilBurnTime.get()
		} else {
			0
		}
	}

	override fun isBarVisible(stack: ItemStack): Boolean = true

	override fun getMaxDamage(stack: ItemStack): Int {
		if (!ServerConfig.CONFIG_SPEC.isLoaded) {
			return 1_500_000
		}

		return ServerConfig.CONFIG.heatingCoilMaxEnergy.get()
	}

	override fun getDamage(stack: ItemStack): Int {
		val max = getMaxDamage(stack)
		val held = stack.getOrDefault(ModDataComponents.ENERGY, 0)

		return max - held
	}

	override fun appendTooltip(
		stack: ItemStack,
		context: TooltipContext,
		display: TooltipDisplay,
		builder: java.util.function.Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val energy = stack.getOrDefault(ModDataComponents.ENERGY, 0)
		val maxEnergy = ServerConfig.CONFIG.heatingCoilMaxEnergy.get()

		val formattedEnergy = "%,d".format(energy)
		val formattedMaxEnergy = "%,d".format(maxEnergy)

		builder.accept(ModMenuLang.FE_WITH_CAPACITY.toComponent(formattedEnergy, formattedMaxEnergy))
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties =
			{
				Properties()
					.stacksTo(1)
					.component(ModDataComponents.ENERGY, 0)
			}

		fun getEnergyCapability(stack: ItemStack, access: ItemAccess): EnergyHandler {
			return ItemAccessEnergyHandler(
				access, ModDataComponents.ENERGY.get(), ServerConfig.CONFIG.heatingCoilMaxEnergy.get(), 100_000, 0
			)
		}

		@JvmStatic
		fun burnInFuelSlot(stack: ItemStack) {
			val requiredEnergy = ServerConfig.CONFIG.heatingCoilBurnCost.get()
			val currentEnergy = stack.getOrDefault(ModDataComponents.ENERGY, 0)
			if (currentEnergy >= requiredEnergy) {
				stack.set(ModDataComponents.ENERGY, currentEnergy - requiredEnergy)
			}
		}

		fun getItemColor(stack: ItemStack, tintIndex: Int): Int {
			if (tintIndex != 1) return 0xFFFFFFFF.toInt()

			val energy = stack.getOrDefault(ModDataComponents.ENERGY, 0)
			val maxEnergy = try {
				ServerConfig.CONFIG.heatingCoilMaxEnergy.get()
			} catch (e: IllegalStateException) {
				1_500_000
			}

			val percent = energy.toFloat() / maxEnergy.toFloat()

			val startR = 0x1E
			val endR = 0xD5

			val a = 0xFF
			val r = (startR + ((endR - startR) * percent)).toInt()
			val g = 0x1E
			val b = 0x1E

			val argb = ARGB(a, r, g, b)
			return argb.toInt()
		}
	}

}












