package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.item.component.OpiniumCoreContentsComponent
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModCreativeModeTabs {

	val TABS_REGISTRY: DeferredRegister<CreativeModeTab> =
		DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, ExcessiveUtilities.MOD_ID)

	val MOD_TAB: DeferredHolder<CreativeModeTab, CreativeModeTab> = TABS_REGISTRY.register("creative_tab", Supplier {
		CreativeModeTab.builder()
			.title(ModItemLang.CREATIVE_TAB.toComponent())
			.icon { ModBlocks.ANGEL_BLOCK.toStack() }
			.displayItems { displayContext: CreativeModeTab.ItemDisplayParameters, output: CreativeModeTab.Output ->
				val regularItems: List<Item> = ModItems.ITEM_REGISTRY.entries.map { it.get() }
				val blockItems: Set<BlockItem> = regularItems.filterIsInstance<BlockItem>().toSet()

				for (item in regularItems) {
					if (item is BlockItem) continue

					if (item == ModItems.OPINIUM_CORE.get()) {
						for (tier in OpiniumCoreContentsComponent.getDefaultTiers()) {
							output.accept(tier.getStack())
						}

						continue
					}

					output.accept(item)
				}

				for (blockItem in blockItems) {
					output.accept(blockItem)
				}
			}
			.build()
	})

}