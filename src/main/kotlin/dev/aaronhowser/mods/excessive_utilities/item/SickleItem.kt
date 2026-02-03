package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.DiggerItem
import net.minecraft.world.item.Item
import net.minecraft.world.item.Tier
import net.minecraft.world.item.Tiers
import net.minecraft.world.item.component.Tool

class SickleItem(properties: Properties) : Item(properties) {

	companion object {
		fun getTool(tier: Tier): Tool = tier.createToolProperties(ModBlockTagsProvider.MINEABLE_WITH_SICKLE)

		fun getDefaultProperties(tier: Tier): Properties {
			return Properties()
				.component(
					DataComponents.TOOL,
					getTool(tier)
				)
				.durability(
					when (tier) {
						Tiers.WOOD -> 531
						Tiers.STONE -> 1179
						Tiers.GOLD -> 288
						Tiers.IRON -> 2250
						Tiers.DIAMOND -> 14049
						Tiers.NETHERITE -> 30074
						else -> tier.uses
					}
				)
				.attributes(
					DiggerItem.createAttributes(tier, 0f, -3f)
				)
		}
	}

}