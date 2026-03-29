package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.config.ServerConfig
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerContribution
import dev.aaronhowser.mods.excessive_utilities.handler.grid_power.GridPowerHandler
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.Mth
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity

class RedCoalItem(properties: Properties) : Item(properties) {

	// Actually uses the mixin one below
	override fun getBurnTime(itemStack: ItemStack, recipeType: RecipeType<*>?): Int {
		return 1600
	}

	override fun inventoryTick(
		stack: ItemStack,
		level: Level,
		entity: Entity,
		slotId: Int,
		isSelected: Boolean
	) {
		if (level.isServerSide) {
			stack.set(ModDataComponents.OWNER, entity.uuid)
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: Properties = Properties()

		@JvmStatic
		fun getBurnTime(furnace: AbstractFurnaceBlockEntity, stack: ItemStack): Int {
			val coalTime = 1600
			val owner = stack.get(ModDataComponents.OWNER) ?: return coalTime
			val level = furnace.level as? ServerLevel ?: return coalTime

			val handler = GridPowerHandler.get(level).getGrid(owner)
			val capacity = handler.getCapacity()
			val usage = handler.getUsage()
			val space = capacity - usage

			val requirement = ServerConfig.CONFIG.redCoalGpCost.get()
			if (space < requirement) {
				return coalTime
			}

			val boost = ServerConfig.CONFIG.redCoalBurnTimeMultiplier.get()
			val boostedTime = Mth.ceil(coalTime * boost)
			val lastTick = level.gameTime + boostedTime

			val gpConsumer = object : GridPowerContribution {
				override fun getAmount(): Double = requirement
				override fun isStillValid(): Boolean = level.gameTime < lastTick

				private val stackCopy = stack.copyWithCount(1)
				override fun getDisplayStack(): ItemStack = stackCopy
				override fun getDisplayName(): Component = stackCopy.displayName
				override fun getDisplayText(): Component {
					return ModMenuLang.GP_AT_POS.toComponent(requirement, furnace.blockPos.x, furnace.blockPos.y, furnace.blockPos.z)
				}
			}

			handler.addConsumer(gpConsumer)

			return boostedTime
		}
	}

}