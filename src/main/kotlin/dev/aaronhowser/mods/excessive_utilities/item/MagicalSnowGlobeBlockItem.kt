package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

class MagicalSnowGlobeBlockItem(properties: Properties) : BlockItem(ModBlocks.MAGICAL_SNOW_GLOBE.get(), properties) {

	override fun inventoryTick(stack: ItemStack, level: Level, entity: Entity, slotId: Int, isSelected: Boolean) {

	}

}