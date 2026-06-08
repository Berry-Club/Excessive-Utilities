package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toComponent
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.toGrayComponent
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModMenuLang
import dev.aaronhowser.mods.excessive_utilities.item.component.MagicalSnowGlobeProgressComponent
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.Style
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.entity.Entity
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.Level

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

		level.playSound(
			null,
			entity.blockPosition(),
			SoundEvents.ARROW_HIT_PLAYER,
			SoundSource.AMBIENT
		)
	}

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val progress = stack.get(ModDataComponents.MAGICAL_SNOW_GLOBE_PROGRESS) ?: return

		tooltipComponents += ModMenuLang.SNOW_GLOBE_TOOLTIP_1.toComponent().withStyle(ChatFormatting.AQUA)
		tooltipComponents += ModMenuLang.SNOW_GLOBE_TOOLTIP_2.toComponent().withStyle(ChatFormatting.AQUA)

		if (progress.isComplete) {
			tooltipComponents += ModMenuLang.SNOW_GLOBE_READY.toGrayComponent()
			return
		}

		tooltipComponents += ModMenuLang.SNOW_GLOBE_INSTRUCTIONS.toGrayComponent(progress.amountRequired)

		for ((biomeTag, found) in progress.biomes) {
			val name = ModMenuLang.biomeTagLangKey(biomeTag).toComponent()

			if (found) {
				name.withStyle(Style.EMPTY.withColor(ChatFormatting.DARK_BLUE).withStrikethrough(true))
			} else {
				name.withStyle(Style.EMPTY.withColor(ChatFormatting.BLUE))
			}

			val component = Component.literal("- ").append(name)

			tooltipComponents += component
		}
	}

	companion object {
		val DEFAULT_PROPERTIES: () -> Properties = {
			Properties()
				.stacksTo(1)
				.component(
					ModDataComponents.MAGICAL_SNOW_GLOBE_PROGRESS.get(),
					MagicalSnowGlobeProgressComponent.DEFAULT
				)
		}
	}

}