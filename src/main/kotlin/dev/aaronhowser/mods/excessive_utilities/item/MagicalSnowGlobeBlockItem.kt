package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.tell
import dev.aaronhowser.mods.excessive_utilities.item.component.MagicalSnowGlobeProgressComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.network.chat.Component
import net.minecraft.tags.BiomeTags
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level
import net.neoforged.neoforge.common.Tags

class MagicalSnowGlobeBlockItem(properties: Properties) : BlockItem(ModBlocks.MAGICAL_SNOW_GLOBE.get(), properties) {

	override fun inventoryTick(
		stack: ItemStack,
		level: Level,
		entity: Entity,
		slotId: Int,
		isSelected: Boolean
	) {
		if (level.gameTime % 200 != 0L) return
		val progress = stack.get(ModDataComponents.MAGICAL_SNOW_GLOBE_PROGRESS) ?: return

		val biomeIsIn = level.getBiome(entity.blockPosition())
		val newProgress = progress.getWithComplete(biomeIsIn) ?: return

		stack.set(ModDataComponents.MAGICAL_SNOW_GLOBE_PROGRESS.get(), newProgress)

		if (entity is LivingEntity) {
			entity.tell("Is client: ${level.isClientSide}")
		}
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val progress = stack.get(ModDataComponents.MAGICAL_SNOW_GLOBE_PROGRESS) ?: return

		for ((biomeTag, found) in progress.requirements) {
			val component = Component.literal(" - ${biomeTag.location}: ${if (found) "Found" else "Not Found"}")
			tooltipComponents.add(component)
		}

	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(
					ModDataComponents.MAGICAL_SNOW_GLOBE_PROGRESS.get(),
					MagicalSnowGlobeProgressComponent(
						mapOf(
							BiomeTags.IS_END to false,
							BiomeTags.IS_FOREST to false,
							BiomeTags.IS_HILL to false,
							BiomeTags.IS_JUNGLE to false,
							Tags.Biomes.IS_MAGICAL to false,
							BiomeTags.IS_MOUNTAIN to false,
							BiomeTags.IS_NETHER to false,
							BiomeTags.IS_OCEAN to false,
							Tags.Biomes.IS_PLAINS to false,
							Tags.Biomes.IS_SANDY to false,
							Tags.Biomes.IS_SNOWY to false,
							Tags.Biomes.IS_SWAMP to false
						)
					)
				)
		}
	}

}