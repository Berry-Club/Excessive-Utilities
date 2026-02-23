package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity

class WirelessHeatingCoilItem(properties: Properties) : Item(properties) {

	override fun getBurnTime(itemStack: ItemStack, recipeType: RecipeType<*>?): Int {
		return 1
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties().stacksTo(1)

		@JvmStatic
		fun burnInFuelSlot(abstractFurnaceBlockEntity: AbstractFurnaceBlockEntity, stack: ItemStack): Boolean {
			val owner = stack.get(ModDataComponents.OWNER) ?: return false

			val requiredEnergy = ServerConfig.CONFIG.heatingCoilBurnCost.get()

			return true
		}

	}

}