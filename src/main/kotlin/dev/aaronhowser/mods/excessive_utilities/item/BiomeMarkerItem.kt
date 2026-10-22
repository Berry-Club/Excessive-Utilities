package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.client.AaronClientUtil
import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBiomeTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.ChatFormatting
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.TooltipDisplay
import net.minecraft.world.item.context.UseOnContext
import net.minecraft.world.level.biome.Biome

class BiomeMarkerItem(properties: Properties) : Item(properties), TooltipItem {

	override fun useOn(context: UseOnContext): InteractionResult {
		val level = context.level
		val pos = context.clickedPos
		val biome = level.getBiome(pos)

		val stack = context.itemInHand
		stack.set(ModDataComponents.BIOME, biome)

		return InteractionResult.SUCCESS
	}

	override fun appendTooltip(
		stack: ItemStack,
		context: TooltipContext,
		display: TooltipDisplay,
		builder: java.util.function.Consumer<Component>,
		tooltipFlag: TooltipFlag
	) {
		val biomeHolder = stack.get(ModDataComponents.BIOME) ?: return
		val biomeKey = biomeHolder.key ?: return

		val component = AaronClientUtil.getBiomeDisplay(biomeKey).withStyle(ChatFormatting.GRAY)

		builder.accept(component)
	}

	companion object {

		fun getAllCrystals(registries: HolderLookup.Provider): List<ItemStack> {
			return registries
				.lookupOrThrow(Registries.BIOME)
				.listElements()
				.toList()
				.filter { !it.isHolder(ModBiomeTagsProvider.BIOME_MARKER_BLACKLIST) }
				.map(::getMarker)
		}

		fun getMarker(biomeHolder: Holder<Biome>): ItemStack {
			val stack = ModItems.BIOME_MARKER.toStack()
			stack.set(ModDataComponents.BIOME, biomeHolder)
			return stack
		}

	}

}






