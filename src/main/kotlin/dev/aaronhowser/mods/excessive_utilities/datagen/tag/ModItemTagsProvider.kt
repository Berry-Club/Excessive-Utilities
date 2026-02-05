package dev.aaronhowser.mods.excessive_utilities.datagen.tag

import dev.aaronhowser.mods.excessive_utilities.ExcessiveUtilities
import dev.aaronhowser.mods.excessive_utilities.registry.ModBlocks
import dev.aaronhowser.mods.excessive_utilities.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModItemTagsProvider(
	pOutput: PackOutput,
	pLookupProvider: CompletableFuture<HolderLookup.Provider>,
	pBlockTags: CompletableFuture<TagLookup<Block>>,
	existingFileHelper: ExistingFileHelper
) : ItemTagsProvider(pOutput, pLookupProvider, pBlockTags, ExcessiveUtilities.MOD_ID, existingFileHelper) {

	override fun addTags(provider: HolderLookup.Provider) {
		tag(RENDER_GP_WHILE_HOLDING)
			.add(
				ModItems.SPEED_UPGRADE.get(),
				ModItems.POWER_MANAGER.get(),
				ModBlocks.MANUAL_MILL.asItem(),
				ModBlocks.WATER_MILL.asItem(),
				ModBlocks.WIND_MILL.asItem(),
				ModBlocks.FIRE_MILL.asItem(),
				ModBlocks.LAVA_MILL.asItem(),
				ModBlocks.SOLAR_PANEL.asItem(),
				ModBlocks.LUNAR_PANEL.asItem(),
				ModBlocks.DRAGON_EGG_MILL.asItem(),
				ModBlocks.CREATIVE_MILL.asItem(),
			)
	}

	companion object {
		private fun create(id: String): TagKey<Item> = ItemTags.create(ExcessiveUtilities.modResource(id))
		private fun common(id: String): TagKey<Item> = ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", id))

		val RENDER_GP_WHILE_HOLDING = create("render_gp_while_holding")
	}

}