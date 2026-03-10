package dev.aaronhowser.mods.excessive_utilities.item

import dev.aaronhowser.mods.aaron.misc.AaronExtensions.isHolder
import dev.aaronhowser.mods.excessive_utilities.datagen.tag.ModBiomeTagsProvider
import dev.aaronhowser.mods.excessive_utilities.registry.ModDataComponents
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.ChatFormatting
import net.minecraft.client.resources.language.I18n
import net.minecraft.core.Holder
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.level.biome.Biome

class BiomeMarkerItem(properties: Properties) : Item(properties) {

	override fun appendHoverText(
		stack: ItemStack,
		context: TooltipContext,
		tooltipComponents: MutableList<Component>,
		tooltipFlag: TooltipFlag
	) {
		val biomeHolder = stack.get(ModDataComponents.BIOME) ?: return
		val biomeKey = biomeHolder.key ?: return

		val probableTranslationKey = "biome.${biomeKey.location().namespace}.${biomeKey.location().path}"
		val hasTranslation = I18n.exists(probableTranslationKey)

		val component = if (hasTranslation) {
			Component.translatable(probableTranslationKey)
		} else {
			Component.literal(biomeKey.location().toString())
		}.withStyle(ChatFormatting.GRAY)

		tooltipComponents.add(component)
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