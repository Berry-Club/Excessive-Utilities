package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.energy.ComponentEnergyStorage
import net.neoforged.neoforge.energy.IEnergyStorage

class HeatingCoilItem(properties: Properties) : Item(properties) {

	override fun getBurnTime(itemStack: ItemStack, recipeType: RecipeType<*>?): Int {
		if (recipeType != RecipeType.SMELTING) {
			return 0
		}

		val requiredEnergy =
			ServerConfig.CONFIG.heatingCoilBurnCost.get()

		val availableEnergy = itemStack.getCapability(Capabilities.EnergyStorage.ITEM, null)?.energyStored ?: 0

		return if (availableEnergy >= requiredEnergy) {
			ServerConfig.CONFIG.heatingCoilBurnTime.get()
		} else {
			0
		}
	}

	override fun isBarVisible(stack: ItemStack): Boolean = true
	override fun getMaxDamage(stack: ItemStack): Int = ServerConfig.CONFIG.heatingCoilMaxEnergy.get()
	override fun getDamage(stack: ItemStack): Int = getMaxDamage(stack) - stack.getOrDefault(ModDataComponents.ENERGY, 0)

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = { Properties().component(ModDataComponents.ENERGY, 0) }

		fun getEnergyCapability(stack: ItemStack, unusedContext: Any?): IEnergyStorage {
			return ComponentEnergyStorage(
				stack,
				ModDataComponents.ENERGY.get(),
				ServerConfig.CONFIG.heatingCoilMaxEnergy.get(),
				10_000,
				10_000
			)
		}

		@JvmStatic
		fun burnInFuelSlot(stack: ItemStack) {
			val capability = stack.getCapability(Capabilities.EnergyStorage.ITEM, null) ?: return
			val requiredEnergy = ServerConfig.CONFIG.heatingCoilBurnCost.get()
			if (capability.energyStored >= requiredEnergy) {
				capability.extractEnergy(requiredEnergy, false)
			}
		}

		fun getItemColor(stack: ItemStack, tintIndex: Int): Int {
			val energy = stack.getOrDefault(ModDataComponents.ENERGY, 0)
			val maxEnergy = ServerConfig.CONFIG.heatingCoilMaxEnergy.get()

			val percent = energy.toFloat() / maxEnergy.toFloat()

			val emptyColor = 0x1E1E1E
			val fullColor = 0xD51E1E

			val startR = (emptyColor shr 16) and 0xFF
			val startG = (emptyColor shr 8) and 0xFF
			val startB = emptyColor and 0xFF

			val endR = (fullColor shr 16) and 0xFF
			val endG = (fullColor shr 8) and 0xFF
			val endB = fullColor and 0xFF

			val r = (startR + ((endR - startR) * percent)).toInt()
			val g = (startG + ((endG - startG) * percent)).toInt()
			val b = (startB + ((endB - startB) * percent)).toInt()

			return (0xFF shl 24) or (r shl 16) or (g shl 8) or b
		}
	}

}