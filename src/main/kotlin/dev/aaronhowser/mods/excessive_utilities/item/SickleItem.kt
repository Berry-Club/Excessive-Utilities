package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBlockTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.core.BlockPos
import net.minecraft.core.component.DataComponents
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.*
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.state.BlockState

class SickleItem(properties: Properties) : Item(properties) {

	override fun mineBlock(
		stack: ItemStack,
		level: Level,
		state: BlockState,
		pos: BlockPos,
		miningEntity: LivingEntity
	): Boolean {
		return super.mineBlock(stack, level, state, pos, miningEntity)
	}

	companion object {
		fun getDefaultProperties(tier: Tier): Properties {
			return Properties()
				.component(
					DataComponents.TOOL,
					tier.createToolProperties(ModBlockTagsProvider.MINEABLE_WITH_SICKLE)
				)
				.component(
					ModDataComponents.RADIUS.get(),
					when (tier) {
						Tiers.WOOD -> 1u
						Tiers.STONE -> 2u
						Tiers.GOLD -> 1u
						Tiers.IRON -> 3u
						Tiers.DIAMOND -> 4u
						Tiers.NETHERITE -> 5u
						else -> 1u
					}
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