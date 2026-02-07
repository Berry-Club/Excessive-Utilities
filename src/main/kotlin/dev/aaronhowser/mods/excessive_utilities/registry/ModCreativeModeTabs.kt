package dev.aaronhowser.mods.excessive_utilities.registry

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModItemLang
import dev.aaronhowser.mods.excessive_utilities.datagen.language.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.excessive_utilities.item.component.OpiniumCoreContentsComponent
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
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

						var funnyTier = OpiniumCoreContentsComponent(
							Items.NETHERITE_BLOCK.defaultInstance,
							Items.DIAMOND_BLOCK.defaultInstance,
							Component.literal("Haha")
						)

						for (i in 0 until 2) {
							funnyTier = OpiniumCoreContentsComponent(
								funnyTier.getStack(),
								funnyTier.getStack(),
								Component.literal("Test Opinium Core Please Ignore")
							)
						}

						output.accept(funnyTier.getStack())

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