package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isServerSide
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity

class RedCoalItem(properties: Properties) : Item(properties) {

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

		}
	}

}